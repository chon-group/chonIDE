<template>
  <div class="page flex flex-col items-center">
    <Header>
      <template v-slot:left>
        <div class="flex items-center h-full">
          <router-link to="/connect" class="h-full">
            <Button height="100%" icon-ratio="12px" no-border icon="wifi-quality-4.svg">
              <template v-slot:content>
                Redes
              </template>
            </Button>
          </router-link>
          <router-link to="/domain" class="h-full">
            <Button height="100%" icon-ratio="12px" no-border icon="domain.svg">
              <template v-slot:content>
                Nome do bot
              </template>
            </Button>
          </router-link>
        </div>
      </template>
      <template v-slot:right>
        <Button no-border height="100%" icon="dots.svg">
          <template v-slot:content>
            <Toggle parent-position>
              <template v-slot:options>
                <button @click="logout">Sair</button>
                <hr>
                <span>Sistema</span>
                <button>
                  Reiniciar
                  <Popup is-children title="Reiniciar sistema">
                    <template v-slot:content>
                      Reiniciar o sistema implicará no procedimento padrão de reinicialização do sistema onde está
                      alocado o sistema operacional chonOS.
                    </template>
                    <template v-slot:action>
                      <Button role="pop-up-closer">
                        <template v-slot:content>
                          Cancelar
                        </template>
                      </Button>
                      <Button main-color @click="resetSystem">
                        <template v-slot:content>
                          Sim, reiniciar sistema.
                        </template>
                      </Button>
                    </template>
                  </Popup>
                </button>
                <button class="severe">
                  Desligar
                  <Popup is-children title="Desligar sistema">
                    <template v-slot:content>
                      Desligar o sistema implicará no procedimento padrão de desligar do sistema onde está
                      alocado o sistema operacional chonOS.
                    </template>
                    <template v-slot:action>
                      <Button role="pop-up-closer">
                        <template v-slot:content>
                          Cancelar
                        </template>
                      </Button>
                      <Button color="var(--pallete-color-red-1)" @click="turnOffSystem">
                        <template v-slot:content>
                          Sim, desligar sistema.
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
           @click="goProject(project.id)"
      >
        <div class="project__image" :style="getProjectColor(project.name[0].toUpperCase())">
          {{ project.name[0].toUpperCase() }}
        </div>
        <input class="project__name" v-model="project.name" @keyup="renamingProject(project, $event, index)" readonly
               ref="inputProject" spellcheck="false"/>
        <Toggle type="contextmenu" click-position>
          <template v-slot:options>
            <button class="severe">
              Excluir
              <Popup is-children title="Excluir projeto">
                <template v-slot:content>
                  Excluindo o projeto você perderá todo os agentes e firmwares desenvolvidos nesse projeto e o mesmo.
                  Certeza que deseja continuar?
                </template>
                <template v-slot:action>
                  <Button role="pop-up-closer">
                    <template v-slot:content>
                      Cancelar
                    </template>
                  </Button>
                  <Button color="var(--pallete-color-red-1)" @click="deleteProject(project.id, index)" role="pop-up-closer">
                    <template v-slot:content>
                      Sim, excluir projeto.
                    </template>
                  </Button>
                </template>
              </Popup>
            </button>
            <button @click="selectCreatedProject(index)">Renomear</button>
          </template>
        </Toggle>
      </div>
      <div class="project" @click="selectNewProject">
        <div class="project__image" ref="letterNewProject">+</div>
        <input class="project__name" v-model="newProjectName" readonly
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

export default {
  name: "Home",
  components: {Header, Toggle, Button, Popup},
  data() {
    return {
      projects: [],
      newProjectName: "Novo projeto"
    }
  },
  watch: {
    newProjectName(newValue) {
      if (newValue != "Novo projeto") {
        this.newProjectName = Util.mantainJustLetters(newValue);
      }
    }
  },
  setup() {
    Util.setTitle("Home");
    API.loadToken();
  },
  mounted() {
    API.get(EndPoints.PROJECTS).then((response) => {
      this.projects = response.data.data;
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
      this.newProjectName = "Novo projeto";
      this.$refs.letterNewProject.innerText = "+";
      this.$refs.inputNewProject.readOnly = false;
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
        this.$emit("message", {content: "O nome do projeto não pode ser vazio", type: MessageType.WARNING});
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
        if (response.status == 200) {
          this.projects.splice(projectIndex, 1);
        }
      });
    },
    createNewProject() {
      if (this.newProjectName.length == 0) {
        this.$emit("message", {type: MessageType.WARNING, content: "O nome do projeto não pode ser vazio"});
        return;
      }
      if (this.projects.filter((project) => project.name == this.newProjectName).length > 0) {
        this.$emit("message", {type: MessageType.WARNING, content: "Já existe um projeto com esse nome"});
        return;
      }
      API.post(EndPoints.PROJECTS, {params: {projectName: this.newProjectName}}).then((response) => {
        if (response.status == 200) {
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
      this.$emit(AppEvent.MESSAGE, {content: "Desligando sistema", type: MessageType.WARNING});
      API.put(EndPoints.SYSTEM_POWEROFF);
      setTimeout(() => {
        router.push(Routes.LOGIN);
      }, 2000);
    },
    resetSystem() {
      this.$emit(AppEvent.MESSAGE, {content: "Reiniciando sistema", type: MessageType.WARNING});
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
  height: 80px;
  width: 80px;
  background-color: var(--pallete-color-black-4);
  transition: transform 0.2s;
  @apply flex items-center justify-center rounded-sm text-2xl font-black cursor-default;
}

.project__image:hover {
  transform: scale(1.03);
}

.project:focus-within > .project__image {
  transform: scale(1.03);
}

.project:focus-within > .project__name {
  background-color: var(--pallete-color-black-4);
}

.project__name {
  color: var(--pallete-text-main);
  width: 120px;
  transition: transform 0.3s;
  @apply border-none bg-transparent cursor-default text-center p-0.5 rounded-sm;
}
</style>