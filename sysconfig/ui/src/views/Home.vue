<template>
  <div class="page flex flex-col items-center">
    <Header>
      <template v-slot:left>
        <div class="flex items-center h-full" v-if="configuration.ddns || configuration.network">
          <router-link to="/connect" v-if="configuration.network">
            <Button icon-ratio="12px" icon="wifi-quality-4.svg">
              <template v-slot:content>
                Networks
              </template>
            </Button>
          </router-link>
          <router-link to="/domain" v-if="configuration.ddns">
            <Button icon-ratio="12px" icon="domain.svg">
              <template v-slot:content>
                Bot Name
              </template>
            </Button>
          </router-link>
        </div>
      </template>
      <template v-slot:right>
        <Button icon="dots.svg" icon-ratio="12px">
          <template v-slot:content>
            <Toggle parent-position>
              <template v-slot:options>
                <button @click="logout">Exit</button>
                <hr v-if="configuration.reboot || configuration.shutdown">
                <span v-if="configuration.reboot || configuration.shutdown">System</span>
                <button v-if="configuration.reboot">
                  Reboot
                  <Popup is-children title="Reboot System">
                    <template v-slot:content>
                      Rebooting the system will imply the standard procedure of rebooting the system where it is
                      allocated the chonOS operating system.
                    </template>
                    <template v-slot:action>
                      <Button role="pop-up-closer">
                        <template v-slot:content>
                          Cancel
                        </template>
                      </Button>
                      <Button main-color @click="resetSystem">
                        <template v-slot:content>
                          Yes, reboot system.
                        </template>
                      </Button>
                    </template>
                  </Popup>
                </button>
                <button class="severe" v-if="configuration.shutdown">
                  Shutdown
                  <Popup is-children title="Desligar sistema">
                    <template v-slot:content>
                      Shutting down the system will carry out the standard shutdown procedure of the system where it is located.
                      allocated the chonOS operating system.
                    </template>
                    <template v-slot:action>
                      <Button role="pop-up-closer">
                        <template v-slot:content>
                          Cancel
                        </template>
                      </Button>
                      <Button color="var(--pallete-color-red-1)" @click="turnOffSystem">
                        <template v-slot:content>
                          Yes, shutdown system.
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
    <div class="project__list">
      <div class="project"
           v-for="(project, index) in projects" :key="project.id"
      >
        <div class="project__image-container">
          <div class="project__image" :style="getProjectColor(project.name[0].toUpperCase())" ref="project"
               @click="goProject(project.id)">
            {{ project.name[0].toUpperCase() }}
          </div>
          <button class="project__options" ref="projectOptionsButton">
            <Toggle type="click" parent-position>
              <template v-slot:options>
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
                      <Button color="var(--pallete-color-red-1)" @click="deleteProject(project.id, index)"
                              role="pop-up-closer">
                        <template v-slot:content>
                          Yes, delete project.
                        </template>
                      </Button>
                    </template>
                  </Popup>
                </button>
                <button @click="selectCreatedProject(index)">Rename</button>
              </template>
            </Toggle>
          </button>
        </div>
        <input class="project__name" v-model="project.name" @keyup="renamingProject(project, $event, index)" readonly
               ref="inputProject" spellcheck="false"/>
        <Toggle type="contextmenu" click-position>
          <template v-slot:options>
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
                  <Button color="var(--pallete-color-red-1)" @click="deleteProject(project.id, index)"
                          role="pop-up-closer">
                    <template v-slot:content>
                      Yes, delete project.
                    </template>
                  </Button>
                </template>
              </Popup>
            </button>
            <button @click="selectCreatedProject(index)">Rename</button>
          </template>
        </Toggle>
      </div>
      <div class="project">
        <div class="project__image" ref="letterNewProject" @click="selectNewProject">+</div>
        <input class="project__name" v-model="newProjectName" readonly="true"
               ref="inputNewProject" spellcheck="false"/>
      </div>
    </div>
  </div>
</template>

<script>
import {API, EndPoints} from "@/domain/API";
import router, {Routes} from "@/router";
import Util from "@/domain/Util";
import {AppEvent, Key, MessageType} from "@/domain/Enums";
import Toggle from "@/components/Toggle";
import projectNamesColor from "@/assets/css/project-names-color.json";
import Header from "@/layout/Header";
import Button from "@/components/Button";
import Popup from "@/components/Popup";
import {removeRipple, useRipple} from "@/composable/Ripple";

export default {
  name: "Home",
  components: {Header, Toggle, Button, Popup},
  data() {
    return {
      projects: [],
      newProjectName: "New project",
      configuration: {}
    }
  },
  watch: {
    newProjectName(newValue) {
      if (newValue != "New project") {
        this.newProjectName = Util.mantainJustLetters(newValue);
      }
    }
  },
  setup() {
    Util.setTitle("Home");
    API.loadToken();
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
    API.get(EndPoints.PROJECTS).then((response) => {
      this.projects = response.data.data;
    }).then(() => {
      useRipple(this.$refs.letterNewProject);
      this.$refs.project.forEach((project) => {
        useRipple(project);
      });
      this.$refs.projectOptionsButton.forEach((button) => {
        useRipple(button);
      })
    });
    API.get(EndPoints.CONFIGURATION, false).then((response) => {
      if (response.data.status == 200) {
        this.configuration = response.data.data;
      }
    });
    this.$refs.inputNewProject.onblur = () => {
      this.setDefaultNewProjet();
    }
    this.$refs.inputNewProject.onkeyup = (event) => {
      if (event.code == Key.ENTER) {
        this.createNewProject();
      } else if (this.newProjectName.length == 0) {
        this.$refs.letterNewProject.innerText = "+";
      } else {
        this.$refs.letterNewProject.innerText = this.newProjectName[0].toUpperCase();
      }
    }
  },
  methods: {
    setDefaultNewProjet() {
      this.newProjectName = "New project";
      this.$refs.letterNewProject.innerText = "+";
      this.$refs.inputNewProject.readOnly = true;
      this.$refs.inputNewProject.blur();
    },
    goProject(projectId) {
      router.push(Routes.PROJECT + projectId);
    },
    selectNewProject() {
      this.$refs.inputNewProject.readOnly = false;
      this.$refs.inputNewProject.focus();
      this.newProjectName = "";
    },
    renamingProject(project, event, index) {
      if (project.name.length == 0) {
        this.$emit("message", {content: "Project name cannot be empty", type: MessageType.WARNING});
        return;
      }
      if (event.code == Key.ENTER) {
        API.put(EndPoints.PROJECTS, {}, project);
        this.$refs.inputProject[index].blur();
      } else {
        project.name = Util.mantainJustRegularCharacters(project.name);
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
    deleteProject(projectId, projectIndex) {
      API.delete(EndPoints.PROJECTS, {params: {projectId: projectId}}).then((response) => {
        if (response.data.status == 200) {
          this.projects.splice(projectIndex, 1);
        }
      });
    },
    createNewProject() {
      if (this.newProjectName.length == 0) {
        this.$emit("message", {type: MessageType.WARNING, content: "Project name cannot be empty"});
        return;
      }
      if (this.projects.filter((project) => project.name == this.newProjectName).length > 0) {
        this.$emit("message", {type: MessageType.WARNING, content: "A project with that name already exists"});
        return;
      }
      API.post(EndPoints.PROJECTS, {params: {projectName: this.newProjectName}}).then((response) => {
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
      API.put(EndPoints.SYSTEM_POWEROFF);
      setTimeout(() => {
        router.push(Routes.LOGIN);
      }, 2000);
    },
    resetSystem() {
      this.$emit(AppEvent.MESSAGE, {content: "Rebooting system", type: MessageType.WARNING});
      API.put(EndPoints.SYSTEM_REBOOT);
      setTimeout(() => {
        router.push(Routes.LOGIN);
      }, 2000);
    },
    logout() {
      API.delete(EndPoints.USERS).then(() => {
        router.push(Routes.LOGIN);
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
  opacity: 50%;
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

.project__options{
  opacity: 0%;
  background-image: url("@/assets/media/icon/vertical-dots.svg");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 5%;
  height: 24px;
  aspect-ratio: 1/1;
  top: 2px;
  right: 2px;
  transition: opacity 0.1s;
  @apply rounded-md border-none bg-transparent cursor-pointer absolute;
}

.project__options:is(:hover, :focus) {
  opacity: 100% !important;
}
</style>