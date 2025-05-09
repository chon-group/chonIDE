<template>
  <div class="page flex flex-col items-center">
    <NetworksPopup @message="$emit('message', $event)" v-if="networkOpen" @closed="networkOpen = false"/>
    <Header>
      <template v-slot:left>
        <div class="flex items-center h-full">
          <Button icon="wifi-quality-3.svg" icon-ratio="15px" v-if="configuration != null && configuration.network"
                  text="Network" @click="networkOpen = true"/>
          <Button icon="domain.svg" icon-ratio="13px" text="Bot name"
                  v-if="configuration != null && configuration.ddns">
            <template v-slot:content>
              Domain
              <DomainPopup @message="$emit('message', $event)"/>
            </template>
          </Button>
        </div>
      </template>
      <template v-slot:center>
        <span v-if="configuration === null" class="text-aside">Loading resources...</span>
      </template>
      <template v-slot:right>
        <Button icon="dots.svg" icon-ratio="12px">
          <template v-slot:content>
            <Toggle parent-position>
              <template v-slot:options>
                <button @click="logout">Exit</button>
                <button v-if="configuration != null && configuration.reboot">
                  Reboot
                  <Popup is-children title="Reboot System" ref="rebootSystemPopup">
                    <template v-slot:content>
                      Rebooting the system will imply the standard procedure of rebooting the system where it is
                      allocated the chonOS operating system.
                    </template>
                    <template v-slot:action>
                      <Button role="pop-up-closer" color="var(--pallete-color-black-3)">
                        <template v-slot:content>
                          Cancel
                        </template>
                      </Button>
                      <Button main-color @click="resetSystem" role="pop-up-closer">
                        <template v-slot:content>
                          Yes, reboot system
                        </template>
                      </Button>
                    </template>
                  </Popup>
                </button>
                <button v-if="configuration != null && configuration.shutdown" class="severe">
                  Shutdown
                  <Popup is-children title="Desligar sistema" ref="shutdownSystemPopup">
                    <template v-slot:content>
                      Shutting down the system will carry out the standard shutdown procedure of the system where it is
                      located.
                      allocated the chonOS operating system.
                    </template>
                    <template v-slot:action>
                      <Button role="pop-up-closer" color="var(--pallete-color-black-3)">
                        <template v-slot:content>
                          Cancel
                        </template>
                      </Button>
                      <Button color="var(--pallete-color-red-1)" @click="turnOffSystem" role="pop-up-closer">
                        <template v-slot:content>
                          Yes, shutdown system
                        </template>
                      </Button>
                    </template>
                  </Popup>
                </button>
              </template>
            </Toggle>
          </template>
        </Button>
      </template>
    </Header>
    <div v-if="loadingProjects" class="flex justify-center items-center h-full w-full m-auto">
      <Loading/>
    </div>
    <div v-show="!loadingProjects" class="project__list">
      <div v-for="(project, index) in projects"
           :key="project.id" class="project"
      >
        <div class="project__image-container">
          <div ref="project" :style="getProjectColor(project.name[0].toUpperCase())" class="project__image"
               @click="goProject(project.id)">
            {{ project.name[0].toUpperCase() }}
          </div>
          <button ref="projectOptionsButton" class="project__options">
            <Toggle parent-position type="click">
              <template v-slot:options>
                <button @click="downloadProject(project)">Export</button>
                <button @click="selectCreatedProject(index)">Rename</button>
                <button class="severe">
                  Delete
                  <Popup is-children title="Delete project">
                    <template v-slot:content>
                      Deleting the project you will lose all agents and firmware developed in that project and the same.
                      Sure you want to continue?
                    </template>
                    <template v-slot:action>
                      <Button role="pop-up-closer">
                        <template v-slot:content>
                          Cancel
                        </template>
                      </Button>
                      <Button color="var(--pallete-color-red-1)" role="pop-up-closer"
                              @click="deleteProject(project.id, index)">
                        <template v-slot:content>
                          Yes, delete project.
                        </template>
                      </Button>
                    </template>
                  </Popup>
                </button>
              </template>
            </Toggle>
          </button>
        </div>
        <input ref="inputProject" v-model="project.name" class="project__name" readonly
               spellcheck="false" @keyup="renamingProject(project, $event, index)"/>
        <Toggle click-position type="contextmenu">
          <template v-slot:options>
            <button @click="downloadProject(project)">Export</button>
            <button @click="selectCreatedProject(index)">Rename</button>
            <button class="severe">
              Delete
              <Popup is-children title="Delete project">
                <template v-slot:content>
                  Deleting the project you will lose all agents and firmware developed in that project and the same.
                  Sure you want to continue?
                </template>
                <template v-slot:action>
                  <Button role="pop-up-closer">
                    <template v-slot:content>
                      Cancel
                    </template>
                  </Button>
                  <Button color="var(--pallete-color-red-1)" role="pop-up-closer"
                          @click="deleteProject(project.id, index)">
                    <template v-slot:content>
                      Yes, delete project.
                    </template>
                  </Button>
                </template>
              </Popup>
            </button>
          </template>
        </Toggle>
      </div>
      <div class="project">
        <div ref="letterNewProject" class="project__image" @click="selectNewProject">+</div>
        <input ref="inputNewProject" v-model="createProjectName" class="project__name"
               readonly="true" spellcheck="false"/>
      </div>
      <div class="project">
        <div class="project__image" @click="importProject">+</div>
        <span class="project__name">Import project</span>
      </div>
    </div>
  </div>
</template>

<script>
import {Api, DataHandler} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {Headers} from "@/services/request";
import GeneralUtil from "@/utils/generalUtil";
import {AppEvent, Key, MessageType} from "@/utils/enums";
import Toggle from "@/components/general/Toggle.vue";
import projectNamesColor from "@/assets/css/name-colors.json";
import Header from "@/components/Header.vue";
import Button from "@/components/general/Button.vue";
import Popup from "@/components/general/Popup.vue";
import {removeRipple, useRipple} from "@/assets/js/effects/ripple";
import {Routes} from "@/router/routes";
import Loading from "@/components/general/Loading.vue";
import NetworksPopup from "@/components/popups/networksPopup/NetworksPopup.vue";
import DomainPopup from "@/components/popups/DomainPopup.vue";

const PROJECT_EXTENSION = ".chon";

export default {
  name: "Home",
  computed: {
    AppEvent() {
      return AppEvent
    }
  },
  emits: ["message"],
  components: {DomainPopup, NetworksPopup, Loading, Header, Toggle, Button, Popup},
  data() {
    return {
      projects: [],
      loadingProjects: true,
      createProjectName: "New project",
      configuration: null,
      networkOpen: false
    }
  },
  watch: {
    createProjectName(newValue) {
      if (newValue !== "New project") {
        let projectName = newValue.replace(/[^a-zA-Z0-9]/g, "");
        let firstLetter = projectName.substring(0, 1);

        if (isNaN(Number(firstLetter))) {
          this.createProjectName = firstLetter.toLowerCase() + projectName.substring(1);
        } else {
          this.createProjectName = projectName.substring(1);
        }
      }
    }
  },
  setup() {
    GeneralUtil.setTitle("Home");
    Api.loadToken();
  },
  beforeUnmount() {
    removeRipple(this.$refs.letterNewProject);
    this.$refs.project.forEach((project) => {
      removeRipple(project);
    });
    this.$refs.projectOptionsButton.forEach((button) => {
      removeRipple(button);
    })
  },
  mounted() {
    Api.get(EndPoints.PROJECTS).then((response) => {
      this.projects = response.data.data;
    }).then(() => {
      this.loadingProjects = false;
      useRipple(this.$refs.letterNewProject);
      this.$refs.project.forEach((project) => {
        useRipple(project);
      });
      this.$refs.projectOptionsButton.forEach((button) => {
        useRipple(button);
      })
    });

    if (DataHandler.configuration == null) {
      Api.get(EndPoints.CONFIGURATION).then((response) => {
        this.configuration = response.data.data;
        DataHandler.configuration = this.configuration;
      });
    } else {
      this.configuration = DataHandler.configuration;
    }


    this.$refs.inputNewProject.onblur = () => {
      this.setDefaultNewProjet();
    }
    this.$refs.inputNewProject.onkeyup = (event) => {
      if (event.code === Key.ENTER) {
        this.createNewProject();
      } else if (this.createProjectName.length === 0) {
        this.$refs.letterNewProject.innerText = "+";
      } else {
        this.$refs.letterNewProject.innerText = this.createProjectName[0].toUpperCase();
      }
    }
  },
  methods: {
    importProject() {
      let projectInput = document.createElement('input');
      projectInput.type = 'file';
      projectInput.click();
      projectInput.onchange = (event) => {
        let files = event.target.files;
        if (files.length === 0) {
          this.$emit(AppEvent.MESSAGE, {content: "No file selected", type: MessageType.WARNING});
          return;
        }
        let projectFile = files[0];
        Api.post(EndPoints.PROJECTS_IMPORT, Headers.MULTIPART_CONFIG, {file: projectFile}).then((response) => {
          if (response.status === 201) {
            this.projects.push(response.data.data);
            this.$emit(AppEvent.MESSAGE, {
              content: response.data.message,
              type: MessageType.SUCCESS
            });
          }
        }).catch((error) => {
          this.$emit(AppEvent.MESSAGE, {
            content: error.response.data.message,
            type: MessageType.ERROR
          });
        });
      }
    },
    setDefaultNewProjet() {
      this.createProjectName = "New project";
      this.$refs.letterNewProject.innerText = "+";
      this.$refs.inputNewProject.readOnly = true;
      this.$refs.inputNewProject.blur();
    },
    goProject(projectId) {
      this.$router.push(Routes.PROJECT + projectId);
    },
    selectNewProject() {
      this.$refs.inputNewProject.readOnly = false;
      this.$refs.inputNewProject.focus();
      this.createProjectName = "";
    },
    renamingProject(project, event, index) {
      if (project.name.length === 0) {
        this.$emit("message", {content: "Project name cannot be empty", type: MessageType.WARNING});
        return;
      }
      if (event.code === Key.ENTER) {
        Api.put(EndPoints.PROJECTS, {}, project);
        this.$refs.inputProject[index].blur();
      } else {
        project.name = GeneralUtil.mantainJustRegularCharacters(project.name);
      }
    },
    selectCreatedProject(index) {
      let inputProjectElement = this.$refs.inputProject[index];
      inputProjectElement.readOnly = false;
      inputProjectElement.focus();
      inputProjectElement.onblur = () => {
        inputProjectElement.readOnly = true;
      }
    },
    downloadProject(project) {
      Api.get(EndPoints.PROJECTS, {
        params: {projectId: project.id, getType: 2}, responseType:
            'blob'
      })
          .then((response) => {
            if (response.status === 200) {
              const filename = project.name + PROJECT_EXTENSION;
              const blob = new Blob([response.data]);

              const url = window.URL.createObjectURL(blob);

              const a = document.createElement('a');
              a.style.display = 'none';
              a.href = url;
              a.download = filename;
              document.body.appendChild(a);
              a.click();

              window.URL.revokeObjectURL(url);
            }
          })
    },
    deleteProject(projectId, projectIndex) {
      Api.delete(EndPoints.PROJECTS, {params: {projectId: projectId}}).then((response) => {
        if (response.data.status == 200) {
          this.projects.splice(projectIndex, 1);
        }
      });
    },
    createNewProject() {
      if (this.createProjectName.length == 0) {
        this.$emit("message", {type: MessageType.WARNING, content: "Project name cannot be empty"});
        return;
      }
      if (this.projects.filter((project) => project.name == this.createProjectName).length > 0) {
        this.$emit("message", {type: MessageType.WARNING, content: "A project with that name already exists"});
        return;
      }
      Api.post(EndPoints.PROJECTS, {params: {projectName: this.createProjectName}}).then((response) => {
        if (response.data.status == 200) {
          this.projects.push(response.data.data);
          this.setDefaultNewProjet();
        }
      });
    },
    getProjectColor(letter) {
      return {
        backgroundColor: projectNamesColor[letter]
      }
    },
    turnOffSystem() {
      this.$emit(AppEvent.MESSAGE, {content: "Shutting down system", type: MessageType.WARNING});
      Api.put(EndPoints.SYSTEM_POWEROFF).then(() => {
        this.$refs.shutdownSystemPopup.close();
        setTimeout(() => {
          this.$router.push(Routes.LOGIN);
        }, 2000);
      });
    },
    resetSystem() {
      this.$emit(AppEvent.MESSAGE, {content: "Rebooting system", type: MessageType.WARNING});
      Api.put(EndPoints.SYSTEM_REBOOT).then(() => {
        this.$refs.rebootSystemPopup.close();
      });
      setTimeout(() => {
        this.$router.push(Routes.LOGIN);
      }, 2000);
    },
    logout() {
      Api.delete(EndPoints.USERS).then(() => {
        DataHandler.configuration = null;
        this.$router.push(Routes.LOGIN);
      });
    },
  }
}
</script>

<style scoped>

.project__list {
  max-width: 1000px;
  width: 100%;
  @apply flex justify-center items-center flex-wrap gap-5 p-10 m-auto;
}

.project {
  @apply flex flex-col items-center justify-center gap-2.5 p-2.5 text-base cursor-default;
}

.project__image {
  height: 90px;
  width: 90px;
  background-color: var(--pallete-color-black-4);
  @apply flex items-center justify-center rounded-lg text-2xl font-black cursor-pointer;
}

.project__image-container {
  transition: transform 0.2s;
  @apply relative;
}

.project__image-container:hover {
  transform: scale(1.03);
}

.project__image-container:hover > .project__options {
  opacity: 100%;
}

.project__image-container:focus-within {
  transform: scale(1.03);
}

.project__name {
  color: var(--pallete-text-main);
  width: 120px;
  transition: transform 0.3s;
  @apply border-none bg-transparent cursor-default text-center p-0.5 rounded-lg;
}

.project__options {
  opacity: 0;
  background-image: url("@/assets/media/icon/vertical-dots.svg");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 7%;
  height: 25px;
  aspect-ratio: 1/1;
  top: 0;
  right: -5px;
  transform: translateX(100%);
  transition: opacity 0.1s;
  @apply rounded-md border-none bg-transparent cursor-pointer absolute;
}

.project__options:is(:hover) {
  opacity: 100%;
}
</style>