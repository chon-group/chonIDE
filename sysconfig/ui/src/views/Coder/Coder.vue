<template>
  <div class="coder u-column">
    <Popup title="Resposta da placa" ref="boardResponse">
      <template v-slot:content>
        <div class="coder__compiled-response">
          {{ boardResponse }}
        </div>
      </template>
    </Popup>
    <div class="coder__header u-row u-justify-i-between u-align-i-center">
      <div class="u-row u-align-i-center u-height-cover u-gap-3">
        <h2 class="coder__header__logo u-row">chonIDE</h2>
        <div class="u-row u-align-i-center u-height-cover">
          <router-link to="/connect" class="u-height-cover">
            <Button height="100%" icon-ratio="12px" no-border icon="wifi-quality-4.svg">
              <template v-slot:content>
                Redes
              </template>
            </Button>
          </router-link>
          <router-link to="/domain" class="u-height-cover">
            <Button height="100%" icon-ratio="12px" no-border icon="domain.svg">
              <template v-slot:content>
                Nome do bot
              </template>
            </Button>
          </router-link>
        </div>
      </div>
      <div class="u-row u-align-i-center u-height-cover">
        <Button icon="start.svg" icon-ratio="11px" width="35px" height="100%" @click="startMas"
                :is-loading="startingMas"/>
        <Button icon="stop.svg" icon-ratio="10px" width="35px" height="100%" @click="stopMas"
                :is-loading="stopingMas"/>
        <Button icon="download.svg" icon-ratio="12px" width="35px" height="100%" @click="downloadMas"
                :is-loading="downloadingMas"/>
        <hr class="coder__header__hr">
        <Button height="100%" no-border
                v-if="domain != null" :link="mindInspectorUrl">
          <template v-slot:content>
            Mind Inspector
          </template>
        </Button>
        <Button height="100%" no-border
                v-if="domain != null" :link="logsUrl">
          <template v-slot:content>
            Logs do SMA
          </template>
        </Button>
        <hr class="coder__header__hr">
        <Button no-border height="100%" width="35px" icon="dots.svg">
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
      </div>
    </div>

    <div class="u-row">
      <div class="coder__explorer u-column">
        <div class="coder__header-bar coder__explorer__project-name u-row">
          <input type="text" v-model="projectName" placeholder="Nome do projeto">
          <div class="coder__project-status is-aside">
            <Loading v-if="savingProject" border-width="1px" ratio="12px" main-color="var(--pallete-text-main)"/>
            <img v-else src="@/assets/media/icon/check.svg" style="width: 12px">
          </div>
        </div>
        <div class="coder__explorer__main">
          <ExplorerFolder name="Sistema multiagente" :has-add="false">
            <template v-slot:content>
              <ExplorerFolder name="Agentes" @add="addAgentFileAction">
                <template v-slot:content>
                  <ExplorerFile v-for="(agent, index) in agents"
                                :key="index" :file="agent" icon="Ag"
                                @delete="removeFileAction(index, agents)"
                                @edit="(editedAgent) => agent = editedAgent"
                                @show="
                                  currentFile = agent;
                                  firmwareFileIsOpen = false;
                                  agentFileIsOpen = true;
                                "/>
                </template>
              </ExplorerFolder>
            </template>
          </ExplorerFolder>
          <ExplorerFolder name="Firmwares" @add="addFirmwareFileAction">
            <template v-slot:content>
              <ExplorerFile v-for="(firmware, index) in firmwares"
                            :key="index" :file="firmware" icon="C++"
                            @delete="removeFileAction(index, firmwares)"
                            @edit="(editedFirmware) => firmware = editedFirmware"
                            @show="
                                  currentFile = firmware;
                                  firmwareFileIsOpen = true;
                                  agentFileIsOpen = false;
                                "/>
              <ExplorerFolder name="Bibliotecas" @add="importLibrary"
                              add-message="Nova biblioteca" has-refresh @refresh="loadLibraries(true)">
                <template v-slot:content>
                  <ExplorerFile v-for="(library, index) in libraries"
                                :key="index" :file="library" icon="L" :can-rename="false"/>
                </template>
              </ExplorerFolder>
            </template>
          </ExplorerFolder>
        </div>
      </div>
      <div class="coder__coding u-column">
        <div class="coder__coding__controller u-row u-align-i-center u-justify-i-between">
          <div type="text" class="u-row u-height-cover u-align-i-center">
            <span class="coder__coding__file-name">
              {{ currentFile != null ? currentFile.name : "Nenhum arquivo" }}
            </span>
            <Button icon="toggle.svg" icon-ratio="8px" side-padding="8px"
                    height="100%" icon-sense="right" v-if="agentFileIsOpen" margin="4px 5px">
              <template v-slot:content>
                {{ this.currentFile.archClass }}
                <Toggle parent-position select
                        @select="(selected) => this.currentFile.archClass = selected"
                        :selected="this.currentFile.archClass">
                  <template v-slot:options>
                    <button v-for="(agentType, index) in this.agentTypes"
                            :key="index">
                      {{ agentType }}
                    </button>
                  </template>
                </Toggle>
              </template>
            </Button>
          </div>
          <div class="u-row u-height-cover" v-if="firmwareFileIsOpen">
            <Button icon="upload.svg" height="100%" icon-ratio="10px"
                    no-border @click="compileSketch" :is-loading="compilingSketch">
              <template v-slot:content>
                Compilar
              </template>
            </Button>
            <Button icon="white-check.svg" height="100%" icon-ratio="12px"
                    no-border @click="deploySketch" :is-loading="deployingSketch">
              <template v-slot:content>
                Deploy
              </template>
            </Button>
          </div>
        </div>
        <div class="coder__writer u-row">
          <div class="coder__writer__lines u-column" ref="coderLines">
            <div v-for="index in lineQuantity" :key="index" class="coder__writer__line">{{ index }}</div>
          </div>
          <textarea v-if="currentFile != null" class="coder__writer__text" ref="coder" v-model="currentFile.sourceCode"
                    spellcheck="false" :readonly="currentFile != null ? false : true"></textarea>
          <textarea v-else class="coder__writer__text" ref="coder" readonly></textarea>
        </div>
      </div>
      <div class="coder__boards u-column" v-if="firmwareFileIsOpen">
        <div class="coder__header-bar u-row u-justify-i-between">
          <span class="coder__header-bar__title">Placas disponíveis</span>
          <Button icon="refresh.svg" icon-ratio="13px" side-padding="12px" height="100%" @click="loadBoards(true)"/>
        </div>
        <div class="u-total-center u-height-cover u-width-cover" v-if="loadingBoards">
          <Loading border-width="2px" main-color="var(--pallete-text-main)" ratio="25px"/>
        </div>
        <div class="u-total-center u-height-cover u-width-cover" v-else-if="boards.length === 0 && !loadingBoards">
          <span class="is-aside">Não foram encontradas placas disponíveis</span>
        </div>
        <Board v-else v-for="(board, index) in boards" :key="index"
               @select="currentBoard = board;" :is-current="currentBoard == board"/>
      </div>
    </div>

  </div>

</template>

<script>
import Util from "@/domain/Util";
import Button from "@/components/Button";
import {AgentType, AgentTypes, AppEvent, MessageType} from "@/domain/Enums";
import Loading from "@/components/Loading";
import Popup from "@/components/Popup";
import router, {Routes} from "@/router";
import {API, EndPoints, Headers} from "@/domain/API";
import defaultSourceCode from "@/domain/content/default-source-codes.json";
import ExplorerFile from "@/views/Coder/ExplorerFile";
import ExplorerFolder from "@/views/Coder/ExplorerFolder";
import Board from "@/views/Coder/Board";
import Toggle from "@/components/Toggle";

const LINE_BREAK_CHAR = "\n", TAB_CHAR = "\t", POS_CHAR = "$";
const CODER_DIFF_HEIGHT = 18;
const AGENT_DEFAULT_FILE_NAME = "newAgent", FIRMWARE_DEFAULT_FILE_NAME = "newSketch";
const DEFAULT_LINKS_PROTOCOL = "http://";

export default {
  name: "Coder",
  components: {Toggle, Board, ExplorerFolder, ExplorerFile, Loading, Button, Popup},
  data() {
    return {
      projectName: "",
      currentFile: null,
      currentBoard: null,
      firmwareFileIsOpen: false,
      agentFileIsOpen: false,
      domain: null,
      agents: [],
      firmwares: [],
      boards: [],
      libraries: [],
      agentTypes: AgentTypes,
      boardResponse: null,
      savingProject: false,
      importingMas: false,
      startingMas: false,
      stopingMas: false,
      compilingSketch: false,
      deployingSketch: false,
      loadingBoards: false
    }
  },
  watch: {
    agents: {
      handler() {
        this.saveProject();
      },
      deep: true
    },
    firmwares: {
      handler() {
        this.saveProject();
      },
      deep: true
    },
    projectName(newValue) {
      this.projectName = Util.mantainJustRegularCharacters(newValue);
      this.saveProject();
    }
  },
  computed: {
    mindInspectorUrl() {
      return DEFAULT_LINKS_PROTOCOL + this.domain.domain + ":3272";
    },
    logsUrl() {
      return DEFAULT_LINKS_PROTOCOL + this.domain.domain + ":3271";
    },
    lineQuantity() {
      try {
        if (this.currentFile.sourceCode === '') {
          return 1;
        }
        return this.currentFile.sourceCode.split(LINE_BREAK_CHAR).length;
      } catch (error) {
        return 1;
      }
    }
  },
  setup() {
    Util.setTitle("Criando SMA");
    API.loadToken();
  },
  mounted() {
    API.get(EndPoints.PROJECTS).then((response) => {
      this.agents = response.data.data.agents;
      this.firmwares = response.data.data.firmwares;
      this.projectName = response.data.data.name;
      this.currentFile = this.agents[0];
      this.agentFileIsOpen = true;
    });

    this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight + (2 * CODER_DIFF_HEIGHT)) + "px";

    API.get(EndPoints.DOMAINS).then((response) => {
      this.domain = response.data.data;
    });

    this.loadBoards();
    this.loadLibraries();

    // Implementação do codador.
    this.$refs.coder.addEventListener("keydown", (event) => {
      if (event.which === 13) {
        this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight + CODER_DIFF_HEIGHT) + "px";
      } else if (event.which === 8) {
        this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight - CODER_DIFF_HEIGHT) + "px";
      } else if (event.which === 219) {
        if (event.shiftKey) {
          this.writeAction(event, '{' + LINE_BREAK_CHAR + TAB_CHAR + POS_CHAR + LINE_BREAK_CHAR + '}', true);
        } else {
          this.writeAction(event, `[${POS_CHAR}]`, true);
        }
      } else if (event.which === 57 && event.shiftKey) {
        this.writeAction(event, `(${POS_CHAR})`, true);
      } else if (event.which === 9) {
        event.preventDefault();
        this.writeAction(event, TAB_CHAR, false);
      } else if (event.which === 222) {
        if (event.shiftKey) {
          this.writeAction(event, `"${POS_CHAR}"`, true);
        } else {
          this.writeAction(event, `'${POS_CHAR}'`, true);
        }
      }
    });
  },
  methods: {
    projectIsInvalid() {
      if (this.agents.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "Não é possível inicar o SMA sem agentes", type: MessageType.ERROR});
        return true;
      }
      if (this.projectName.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "O nome do projeto não pode ser vazio", type: MessageType.ERROR});
        return true;
      }
      return false;
    },
    compileSketch() {
      if (this.currentBoard == null) {
        this.$emit(AppEvent.MESSAGE, {content: "Nenhuma placa foi selecionada", type: MessageType.WARNING});
        return;
      }
      this.compilingSketch = true;
      API.post(EndPoints.SKETCH_COMPILE, {
        params: {
          boardName: this.currentBoard.fqbn,
          code: this.currentFile.sourceCode
        }
      }).then((response) => {
        this.boardResponse = response.data.data;
        this.compilingSketch = false;
        this.$refs.boardResponse.showing(true);
      });
    },
    deploySketch() {
      if (this.currentBoard == null) {
        this.$emit(AppEvent.MESSAGE, {content: "Não existe nenhuma placa selecionada", type: MessageType.WARNING});
        return;
      }
      this.deployingSketch = true;
      API.post(EndPoints.SKETCH_DEPLOY, {
        params: {
          serialPort: this.currentBoard.port,
          boardName: this.currentBoard.fqbn
        }
      }).then((response) => {
        this.$refs.boardResponse.showing(true);
        this.boardResponse = response.data.data;
        this.deployingSketch = false;
      });
    },
    importLibrary() {
      let libraryInput = document.createElement('input');
      libraryInput.type = 'file';
      libraryInput.click();
      libraryInput.onchange = (event) => {
        let files = event.target.files;
        if (files.length === 0) {
          this.$emit(AppEvent.MESSAGE, {content: "Nenhum arquivo selecionado", type: MessageType.WARNING});
          return;
        }
        let libraryFile = files[0];
        if (Util.isFileInvalid(libraryFile)) {
          this.$emit(AppEvent.MESSAGE, {content: "O nome do arquivo não pode ter espaço", type: MessageType.ERROR});
          return;
        }
        API.post(EndPoints.LIBRARIES_IMPORT, Headers.MULTIPART_CONFIG, {file: libraryFile}).then((response) => {
          if (response.status === 200) {
            this.$emit(AppEvent.MESSAGE, {
              content: response.data.data,
              type: MessageType.SUCCESS
            });
            this.loadLibraries(true);
          } else {
            this.$emit(AppEvent.MESSAGE, {
              content: response.data.data,
              type: MessageType.ERROR
            });
          }
        });
      }
    },
    saveProject() {
      this.savingProject = true;
      return API.put(EndPoints.PROJECTS, {}, {
        name: this.projectName,
        agents: this.agents,
        firmwares: this.firmwares
      }).then(() => {
        setTimeout(() => {
          this.savingProject = false;
        }, 100);
      });
    },
    startMas() {
      if (this.projectIsInvalid()) {
        return;
      }
      this.startingMas = true;
      API.put(EndPoints.MAS, {
        params: {
          action: "start"
        }
      }, {
        name: this.projectName,
        agents: this.agents
      }).then((response) => {
        this.$emit(AppEvent.MESSAGE, {
          content: response.data.message,
          type: MessageType.SUCCESS
        });
        this.startingMas = false;
      });
    },
    stopMas() {
      this.stopingMas = true;
      API.put(EndPoints.MAS, {params: {action: "stop"}}).then((response) => {
        if (response.status == 202) {
          this.$emit(AppEvent.MESSAGE, {content: response.data.message, type: MessageType.WARNING});
        } else {
          this.$emit(AppEvent.MESSAGE, {content: response.data.message, type: MessageType.SUCCESS});
        }
        this.stopingMas = false;
      });
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
    loadLibraries(refresh = false) {
      return API.get(EndPoints.LIBRARIES, refresh).then((response) => {
        this.libraries = response.data.data;
      });
    },
    loadBoards(refresh) {
      this.currentBoard = null;
      this.loadingBoards = true;
      API.get(EndPoints.BOARDS, refresh).then((response) => {
        this.boards = response.data.data;
        this.loadingBoards = false;
        if (this.boards.length != 0) {
          this.currentBoard = this.boards[0];
        }
      });
    },
    logout() {
      API.delete(EndPoints.USERS);
      router.push(Routes.LOGIN);
    },
    removeFileAction(index, files) {
      if (this.currentFile == files[index]) {
        if (index == 0) {
          files.splice(index, 1);
          if (files.length == 0) {
            this.currentFile = null;
          }
        } else {
          this.currentFile = files[index - 1];
          files.splice(index, 1);
        }
      } else {
        files.splice(index, 1);
      }
    },
    addFirmwareFileAction() {
      this.firmwares.push({
        name: FIRMWARE_DEFAULT_FILE_NAME,
        sourceCode: defaultSourceCode.firmware
      });
    },
    addAgentFileAction() {
      this.agents.push({
        name: AGENT_DEFAULT_FILE_NAME,
        archClass: AgentType.JASON,
        sourceCode: defaultSourceCode.agent
      });
    },
    writeAction(event, text, setPositionInner) {
      event.preventDefault();
      setTimeout(() => {
        let selectionStart = this.$refs.coder.selectionStart;
        let beforeText = this.currentFile.sourceCode.substring(0, selectionStart);
        let afterText = this.currentFile.sourceCode.substring(selectionStart);
        this.currentFile.sourceCode = beforeText + text + afterText;
        let selectionPosition;

        if (setPositionInner) {
          selectionPosition = this.currentFile.sourceCode.indexOf(POS_CHAR);
          this.currentFile.sourceCode = this.currentFile.sourceCode.replace(POS_CHAR, "");
        } else {
          selectionPosition = selectionStart + text.length;
        }
        setTimeout(() => {
          this.$refs.coder.setSelectionRange(selectionPosition, selectionPosition);
        }, 0)
      }, 0);
    },
  }
}
</script>

<style scoped>

::-moz-selection {
  background: var(--pallete-color-main-2);
}

::selection {
  background: var(--pallete-color-main-2);
}

/* Codador */

.coder {
  --explorer-width: 350px;
  --bar-height: 30px;
  width: 100vw;
  height: 100vh;
}

.coder__compiled-response {
  padding: var(--ratio-3);
  background-color: var(--pallete-color-black-3);
  border-radius: var(--border-radius-item);
  line-height: 1.7;
}

.coder *:not(input, textarea) {
  cursor: default;
}

.coder__header {
  background-color: var(--pallete-color-black-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
  height: var(--bar-height);
}

.coder__header__hr {
  height: 100%;
  width: 1px;
  background-color: var(--pallete-color-black-4);
  border: none;
  margin: 0 var(--ratio-4);
}

.coder__header__logo {
  font-size: 14px;
  margin-left: var(--ratio-3);
}

.coder__coding {
  width: calc(100vw - var(--explorer-width));
  border-left: 1px solid var(--pallete-color-black-1);
}

.coder__coding__controller {
  width: 100%;
  height: var(--bar-height);
  background-color: var(--pallete-color-black-2);
  border-bottom: 1px solid var(--pallete-color-black-1);
}

.coder__project-status {
  margin: auto 0 auto auto;
  user-select: none;
}

.coder__coding__file-name {
  height: calc(var(--bar-height) - 1px);
  max-width: 125px;
  background-color: var(--pallete-color-black-3);
  padding: var(--ratio-4) var(--ratio-3);
  padding-top: 6px;
  color: var(--pallete-text-main);
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden !important;
  box-shadow: inset 0 -2px 0 var(--pallete-color-main-2);
}

.coder__coding__agent-type {
  padding: ;
}

.coder__writer {
  width: 100%;
  height: calc(100vh - calc(2 * var(--bar-height)));
  overflow-y: scroll;
  --writer-font-size: var(--text-size-normal);
}

.coder__writer__lines {
  min-height: 100%;
  height: fit-content;
  min-width: 68px;
  background-color: var(--pallete-color-black-2);
  padding: var(--ratio-2) 0;
  color: var(--pallete-text-aside);
  border-right: var(--ratio-4) solid var(--pallete-color-black-3);
}

.coder__writer__line {
  font-size: var(--writer-font-size);
  text-align: left;
  padding-left: var(--ratio-2);
}

.coder__writer__text {
  height: 100%;
  width: 100%;
  border: none;
  background-color: var(--pallete-color-black-1);
  color: var(--pallete-text-main);
  resize: none;
  padding: var(--ratio-2);
  word-spacing: 2px;
  letter-spacing: 1px;
  font-weight: 1000;
  overflow-y: hidden;
  font-size: var(--writer-font-size);
}

/* Lateral */

.coder__explorer {
  min-width: var(--explorer-width);
  background-color: var(--pallete-color-black-2);
}

.coder__explorer__main {
  overflow-y: auto;
  height: calc(100vh - calc(2 * var(--bar-height)));
}

.coder__header-bar {
  background-color: var(--pallete-color-black-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
  min-height: var(--bar-height);
  height: var(--bar-height);
}

.coder__header-bar__title {
  margin: auto auto auto var(--ratio-3);
}

.coder__explorer__project-name {
  padding-right: var(--ratio-3);
}

.coder__explorer__project-name > input {
  background-color: transparent;
  border: 0;
  padding: 0 var(--ratio-3);
  color: var(--pallete-text-main);
  flex-grow: 1;
}

.coder__explorer__project-name:hover {
  background-color: var(--pallete-color-black-4);
}

.coder__explorer__project-name:hover > .coder__project-status {
  display: none;
}

.coder__explorer__project-name:focus {
  background-color: var(--pallete-color-black-4);
}

.coder__boards {
  width: 350px;
  background-color: var(--pallete-color-black-2);
  border-left: 1px solid var(--pallete-color-black-1);
  height: calc(100vh - var(--bar-height));
  overflow-y: auto;
}

</style>