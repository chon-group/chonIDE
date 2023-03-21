<template>
  <div class="project flex flex-col h-screen">
    <Popup title="Resposta da placa" ref="boardResponse">
      <template v-slot:content>
        <div class="project__compiled-response">
          {{ boardResponse }}
        </div>
      </template>
    </Popup>
    <Header>
      <template v-slot:left>
        <router-link to="/home" class="h-full">
          <Button height="100%" icon-ratio="12px" no-border>
            <template v-slot:content>
              Home
            </template>
          </Button>
        </router-link>
      </template>
      <template v-slot:right>
        <div class="flex items-center h-full">
          <Button icon="start.svg" icon-ratio="11px" width="35px" height="100%" @click="startMas"
                  :is-loading="startingMas"/>
          <Button icon="stop.svg" icon-ratio="10px" width="35px" height="100%" @click="stopMas"
                  :is-loading="stopingMas"/>
          <Button icon="download.svg" icon-ratio="12px" width="35px" height="100%" @click="downloadMas"
                  :is-loading="downloadingMas"/>
          <Button height="100%" no-border
                  v-if="domain != null" :link="mindInspectorUrl">
            <template v-slot:content>
              Mind Inspector
            </template>
          </Button>
        </div>
      </template>
    </Header>

    <div class="flex">
      <div class="project__explorer">
        <div class="project__header-bar">
          <span class="project__header-bar__title">{{projectName}}</span>
          <div class="project__project-status">
            <Loading v-if="savingProject" border-width="1px" ratio="12px" main-color="var(--pallete-text-main)"/>
            <img v-else src="@/assets/media/icon/check.svg" style="width: 12px">
          </div>
        </div>
        <div class="project__explorer__main">
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
      <div class="project__coding flex flex-col">
        <div class="project__coding__controller">
          <div type="text" class="flex h-full items-center">
            <span class="project__coding__file-name">
              {{ currentFile.name }}
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
          <div class="flex h-full" v-if="firmwareFileIsOpen">
            <Button icon="white-check.svg" height="100%" icon-ratio="11px"
                    no-border @click="compileSketch" :is-loading="compilingSketch">
              <template v-slot:content>
                Compilar
              </template>
            </Button>
            <Button icon="upload.svg" height="100%" icon-ratio="12px"
                    no-border @click="deploySketch" :is-loading="deployingSketch">
              <template v-slot:content>
                Deploy
              </template>
            </Button>
          </div>
        </div>
        <div class="project__coder">
          <div class="project__coder__lines flex flex-col py-5" ref="coderLines">
            <div v-for="index in lineQuantity" :key="index" class="project__coder__line pl-5">{{ index }}</div>
          </div>
          <textarea class="project__coder__text p-5" ref="coder" v-model="currentFile.sourceCode"
                    spellcheck="false"></textarea>
        </div>
      </div>
      <div class="project__boards flex flex-col" v-if="firmwareFileIsOpen">
        <div class="project__header-bar justify-between">
          <span class="project__header-bar__title">Placas disponíveis</span>
          <Button icon="refresh.svg" icon-ratio="13px" side-padding="12px" height="100%" @click="loadBoards(true)"/>
        </div>
        <div class="flex items-center justify-center h-full w-full" v-if="loadingBoards">
          <Loading border-width="2px" main-color="var(--pallete-text-main)" ratio="25px"/>
        </div>
        <div class="flex items-center justify-center h-full w-full" v-else-if="boards.length === 0 && !loadingBoards">
          <span class="text-aside">Não foram encontradas placas disponíveis</span>
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
import {AgentType, AgentTypes, AppEvent, Key, MessageType} from "@/domain/Enums";
import Loading from "@/components/Loading";
import Popup from "@/components/Popup";
import {API, EndPoints, Headers} from "@/domain/API";
import defaultSourceCode from "@/domain/content/default-source-codes.json";
import ExplorerFile from "@/views/Project/ExplorerFile";
import ExplorerFolder from "@/views/Project/ExplorerFolder";
import Board from "@/views/Project/Board";
import Toggle from "@/components/Toggle";
import Header from "@/layout/Header";

const LINE_BREAK_CHAR = "\n", TAB_CHAR = "\t", POS_CHAR = "$";
const project_DIFF_HEIGHT = 18;
const AGENT_DEFAULT_FILE_NAME = "newAgent", FIRMWARE_DEFAULT_FILE_NAME = "newSketch";
const DEFAULT_LINKS_PROTOCOL = "http://";
const MIND_INSPECTOR_PORT = ":3272", SMA_PORT_PORT = ":3271";

export default {
  name: "Project",
  components: {Header, Toggle, Board, ExplorerFolder, ExplorerFile, Loading, Button, Popup},
  data() {
    return {
      projectName: "",
      currentFile: {name: "Nenhum arquivo", sourceCode: ""},
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
      loadingBoards: false,
      terminalIsOpen: true
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
      return DEFAULT_LINKS_PROTOCOL + this.domain.domain + MIND_INSPECTOR_PORT;
    },
    logsUrl() {
      return DEFAULT_LINKS_PROTOCOL + this.domain.domain + SMA_PORT_PORT;
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
    Util.setTitle("SMA");
    API.loadToken();
  },
  mounted() {
    API.get(EndPoints.PROJECTS, true, {params: {projectId: this.$route.params.id}}).then((response) => {
      this.agents = response.data.data.agents;
      this.firmwares = response.data.data.firmwares;
      this.projectName = response.data.data.name;
      Util.setTitle(this.projectName);
      if (this.agents.length > 0) {
        this.currentFile = this.agents[0];
        this.agentFileIsOpen = true;
      }
    });

    this.$refs.coder.style.height = this.$refs.coderLines.scrollHeight + "px";

    API.get(EndPoints.DOMAINS).then((response) => {
      this.domain = response.data.data;
    });

    this.loadBoards();
    this.loadLibraries();

    // Implementação do codador.
    this.$refs.coder.addEventListener("keydown", (event) => {
      if (event.code == Key.ENTER) {
        this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight + project_DIFF_HEIGHT) + "px";
      } else if (event.which === 8) {
        this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight - project_DIFF_HEIGHT) + "px";
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
        this.$refs.boardResponse.showing(true);
      }).finally(() => {
        this.compilingSketch = false;
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
      }).finally(() => {
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
        id: this.$route.params.id,
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
      }).finally(() => {
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
      }).finally(() => {
        this.stopingMas = false;
      });
    },
    loadLibraries(refresh = false) {
      API.get(EndPoints.LIBRARIES, refresh).then((response) => {
        this.libraries = response.data.data;
      });
    },
    loadBoards(refresh) {
      this.currentBoard = null;
      this.loadingBoards = true;
      API.get(EndPoints.BOARDS, refresh).then((response) => {
        this.boards = response.data.data;
        if (this.boards.length != 0) {
          this.currentBoard = this.boards[0];
        }
      }).finally(() => {
        this.loadingBoards = false;
      });
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

/* Codador */

.project {
  --explorer-width: 300px;
  --bar-height: 30px;
  @apply overflow-y-hidden;
}

.project *:not(input, textarea) {
  @apply cursor-default;
}

.project__compiled-response {
  background-color: var(--pallete-color-black-3);
  line-height: 1.7;
  @apply p-2.5 rounded-sm;
}

.project__project-status {
  @apply select-none m-auto mr-2.5;
}

.project__coding {
  width: calc(100vw - var(--explorer-width));
  border-left: 1px solid var(--pallete-color-black-1);
}

.project__coding__controller {
  width: 100%;
  height: var(--bar-height);
  background-color: var(--pallete-color-black-2);
  border-bottom: 1px solid var(--pallete-color-black-1);
  @apply flex items-center justify-between;
}

.project__coding__file-name {
  height: calc(var(--bar-height) - 1px);
  max-width: 125px;
  background-color: var(--pallete-color-black-3);
  padding-top: 6px;
  color: var(--pallete-text-main);
  text-overflow: ellipsis;
  box-shadow: inset 0 -2px 0 var(--pallete-color-main-1);
  @apply overflow-hidden whitespace-nowrap py-1.5 px-2.5;
}

.project__coder {
  height: calc(100vh - calc(2 * var(--bar-height)));
  --writer-font-size: var(--text-size-normal);
  @apply flex overflow-y-scroll;
}

.project__coder__lines {
  min-width: 68px;
  background-color: var(--pallete-color-black-2);
  color: var(--pallete-text-aside);
  border-right: 5px solid var(--pallete-color-black-3);
  @apply min-h-full h-fit;
}

.project__coder__line {
  font-size: var(--writer-font-size);
}

.project__coder__text {
  background-color: var(--pallete-color-black-1);
  color: var(--pallete-text-main);
  word-spacing: 2px;
  letter-spacing: 1px;
  font-weight: 1000;
  font-size: var(--writer-font-size);
  @apply min-h-full w-full border-none resize-none overflow-y-hidden;
}

/* Lateral */

.project__explorer {
  min-width: var(--explorer-width);
  background-color: var(--pallete-color-black-2);
  @apply flex flex-col;
}

.project__explorer__main {
  height: calc(100vh - calc(2 * var(--bar-height)));
  @apply overflow-y-auto;
}

.project__header-bar {
  background-color: var(--pallete-color-black-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
  min-height: var(--bar-height);
  height: var(--bar-height);
  @apply flex;
}

.project__header-bar__title {
  @apply m-auto ml-2.5;
}

.project__explorer__project-name > input {
  color: var(--pallete-text-main);
  @apply grow px-2.5 border-none bg-transparent;
}

.project__boards {
  width: 350px;
  background-color: var(--pallete-color-black-2);
  border-left: 1px solid var(--pallete-color-black-1);
  height: calc(100vh - var(--bar-height));
  @apply overflow-y-auto;
}

</style>