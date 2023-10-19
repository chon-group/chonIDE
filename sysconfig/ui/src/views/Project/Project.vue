<template>
  <div class="project flex flex-col h-screen">
    <Popup title="Sketch compiled" ref="boardResponse" can-close width="var(--container-width-1)">
      <template v-slot:content>
        <div class="project__compiled-response">
          {{ boardResponse }}
        </div>
      </template>
    </Popup>

    <Popup can-close ref="deployBoardUnknownPopup" title="Choose board model">
      <template v-slot:content>
        <p v-if="this.isCurrentBoardUnknown">The Arduino board model cannot be identified.</p>
        <p> Perform deploy as...</p>
      </template>
      <template v-slot:action>
        <div class="flex gap-2 flex-col w-full">
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:uno', this.deploySketch,
                            $refs.deployBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Uno
            </template>
          </Button>
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:mega', this.deploySketch,
                            $refs.deployBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Mega
            </template>
          </Button>
        </div>
      </template>
    </Popup>

    <Popup can-close ref="compileBoardUnknownPopup" title="Choose board model">
      <template v-slot:content>
        <p v-if="this.isCurrentBoardUnknown">The Arduino board model cannot be identified.</p>
        <p> Perform compile as...</p>
      </template>
      <template v-slot:action>
        <div class="flex gap-2 flex-col w-full">
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:uno', this.compileSketch,
                            $refs.compileBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Uno
            </template>
          </Button>
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:mega', this.compileSketch,
                            $refs.compileBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Mega
            </template>
          </Button>
        </div>
      </template>
    </Popup>

    <Header>
      <template v-slot:left>
        <div class="flex items-center h-full">
          <router-link to="/home">
            <Button icon-ratio="11px" icon="back.svg"/>
          </router-link>
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
                Bot name
              </template>
            </Button>
          </router-link>
        </div>
      </template>
      <template v-slot:right>
        <div class="flex items-center gap-1.5 h-full">
          <Button v-if="configuration.startMAS" icon="start.svg" icon-ratio="11px" @click="startMas"
                  :is-loading="startingMas"/>
          <Button v-if="configuration.stopMAS" icon="stop.svg" icon-ratio="11px" @click="stopMas"
                  :is-loading="stopingMas"/>
          <div class="project-action-separator"></div>
          <Button v-if="domain != null && configuration.mindInspector"
                  :link="mindInspectorUrl"
                  icon="mindinspector.svg"
                  icon-ratio="14px"
                  text="Mind Inspector"/>
          <Button v-if="domain != null && configuration.logMAS"
                  :link="logsUrl"
                  icon="terminal.svg"
                  icon-ratio="14px"
                  text="Logs"/>
        </div>
      </template>
    </Header>
    <div class="flex">
      <div class="project__explorer">
        <div class="project__header-bar">
          <span class="project__header-bar__title">{{ project.name }}</span>
          <div class="project__project-status">
            <Loading v-if="savingProject" border-width="1px" ratio="12px" main-color="var(--pallete-text-main)"/>
            <img v-else src="@/assets/media/icon/check.svg" style="width: 11px">
          </div>
        </div>
        <div class="project__explorer__main">
          <ExplorerFolder v-if="configuration.reasoningLayer" name="Multi-Agent System" :has-add="false" icon="sma.svg"
                          icon-ratio="13px" :has-download="true" @download="downloadMas">
            <template v-slot:content>
              <ExplorerFolder name="Agents" @add="addAgentFileAction" add-message="New agent" icon="agents.svg"
                              icon-ratio="15px">
                <template v-slot:content>
                  <ExplorerFile v-for="(agent, index) in project.agents"
                                :key="index" :file="agent"
                                :icon="getAgentIcon(agent.archClass)"
                                icon-ratio="13px"
                                @delete="removeFileAction(index, project.agents)"
                                @edit="(editedAgent) => agent = editedAgent"
                                :selected="currentFile === agent"
                                ref="agents"
                                @show="
                                  currentFile = agent;
                                  firmwareFileIsOpen = false;
                                  agentFileIsOpen = true;
                                "/>

                </template>
              </ExplorerFolder>
            </template>
          </ExplorerFolder>
          <ExplorerFolder v-if="configuration.firmwareLayer" name="Firmware" @add="addFirmwareFileAction"
                          add-message="New firmware"
                          icon="firmwares.svg" icon-ratio="12px">
            <template v-slot:content>
              <ExplorerFile v-for="(firmware, index) in project.firmwares"
                            :key="index" :file="firmware"
                            @delete="removeFileAction(index, project.firmwares)"
                            @edit="(editedFirmware) => firmware = editedFirmware"
                            icon="ino.svg"
                            icon-ratio="17px"
                            ref="firmwares"
                            :selected="currentFile === firmware"
                            @show="
                                  currentFile = firmware;
                                  firmwareFileIsOpen = true;
                                  agentFileIsOpen = false;
                                "/>
              <ExplorerFolder v-if="configuration.firmwareLibraries" name="Libraries"
                              @add="importLibrary"
                              icon="libraries.svg"
                              icon-ratio="12px"
                              add-message="New library" has-refresh @refresh="loadLibraries(true)">
                <template v-slot:content>
                  <ExplorerFile v-for="(library, index) in libraries"
                                :key="index" :file="library" :can-rename="false"
                                :can-delete="library.name !== 'Javino'"
                                @delete="deleteLibrary(library)"
                                icon="library.svg"
                                icon-ratio="11px"/>
                </template>
              </ExplorerFolder>
            </template>
          </ExplorerFolder>
        </div>
      </div>
      <div class="project__coding flex flex-col">
        <div class="project__coding__controller">
          <div type="text" class="project__coding__file">
            <div class="project__coding__file__selected"></div>
            <span class="project__coding__file__name">
              {{ currentFile.name }}
            </span>
            <div class="project-action-separator" v-if="currentFile.name !== 'No file'"></div>
            <Button icon="toggle.svg" icon-ratio="8px" icon-sense="right" v-if="agentFileIsOpen">
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
            <div class="flex" v-if="firmwareFileIsOpen">
              <Button icon="check.svg" icon-ratio="11px" @click="compileSketch" :is-loading="compilingSketch"
                      text="Compile"/>
              <Button icon="upload.svg" icon-ratio="11px" @click="deploySketch" :is-loading="deployingSketch"
                      v-if="currentBoard != null" text="Deploy"/>
            </div>
          </div>
        </div>
        <div class="project__coder">
          <div class="project__coder__lines" ref="coderLines">
            <div v-for="index in lineQuantity" :key="index" class="project__coder__line pl-5">{{ index }}</div>
          </div>
          <textarea class="project__coder__text p-5" ref="coder" v-model="currentFile.sourceCode"
                    spellcheck="false"></textarea>
        </div>
      </div>
      <div class="project__boards" v-if="firmwareFileIsOpen">
        <div class="project__header-bar">
          <span class="project__header-bar__title">Boards available</span>
          <Button icon="refresh.svg" icon-ratio="13px" side-padding="12px" @click="loadBoards(true)"/>
        </div>
        <div class="flex items-center justify-center h-full w-full" v-if="loadingBoards">
          <Loading border-width="2px" main-color="var(--pallete-text-main)" ratio="25px"/>
        </div>
        <div class="flex items-center justify-center h-full w-full" v-else-if="boards.length === 0 && !loadingBoards">
          <span class="text-aside">No available boards found</span>
        </div>
        <div v-else class="flex flex-col gap-1.5 p-1.5">
          <Board v-for="(board, index) in boards" :key="index"
                 @select="() => {currentBoard = currentBoard === board ? null : board}"
                 :is-current="currentBoard === board"
                 :board="board"/>
        </div>
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
import router, {Routes} from "@/router";

const LINE_BREAK_CHAR = "\n", TAB_CHAR = "\t", POS_CHAR = "$";
const project_DIFF_HEIGHT = 18;
const AGENT_DEFAULT_FILE_NAME = "newAgent", FIRMWARE_DEFAULT_FILE_NAME = "newSketch";
const DEFAULT_LINKS_PROTOCOL = "http://";
const MIND_INSPECTOR_PORT = ":3272", SMA_PORT_PORT = ":3271";
const UNKNOW_BOARD_MODEL_STRING = 'unknown:unknown:unknown';

export default {
  name: "Project",
  components: {Header, Toggle, Board, ExplorerFolder, ExplorerFile, Loading, Button, Popup},
  data() {
    return {
      project: {name: "", agents: [], firmwares: []},
      currentFile: {name: "No file", sourceCode: ""},
      boards: [],
      currentBoard: null,
      libraries: [],
      domain: null,
      agentTypes: AgentTypes,
      firmwareFileIsOpen: false,
      agentFileIsOpen: false,
      boardResponse: null,
      savingProject: false,
      importingMas: false,
      startingMas: false,
      stopingMas: false,
      compilingSketch: false,
      deployingSketch: false,
      loadingBoards: false,
      terminalIsOpen: true,
      configuration: {}
    }
  },
  watch: {
    project: {
      handler(projectValue) {
        this.project.name = Util.mantainJustRegularCharacters(projectValue.name);
        this.saveProject();
      },
      deep: true
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
    },
    isCurrentBoardUnknown() {
      if (this.currentBoard == null || this.currentBoard.fqbn == null) {
        return false;
      }
      return this.currentBoard.fqbn.toLowerCase().includes(UNKNOW_BOARD_MODEL_STRING);
    }
  },
  setup() {
    Util.setTitle("Project");
    API.loadToken();
  },
  mounted() {
    API.get(EndPoints.PROJECTS, true, {params: {projectId: this.$route.params.id, getType: 1}}).then((response) => {
      if (response.data.status === 200) {
        this.project = response.data.data;
        this.project.id = this.$route.params.id;

        Util.setTitle(this.project.name);

        if (this.project.agents.length > 0) {
          this.currentFile = this.project.agents[0];
          this.agentFileIsOpen = true;
        }
      }
    }).then(() => {
      this.$refs.coder.style.height = this.$refs.coderLines.scrollHeight + "px";
    }).catch(() => {
      router.push(Routes.HOME);
    });

    API.get(EndPoints.CONFIGURATION, false).then((response) => {
      if (response.data.status === 200) {
        this.configuration = response.data.data;
      }
    });

    API.get(EndPoints.DOMAINS).then((response) => {
      this.domain = response.data.data;
    });

    this.loadLibraries();
    this.loadBoards();

    // Implementação do codador.

    this.$refs.coder.addEventListener("keydown", (event) => {
      if (event.key === Key.ENTER) {
        this.$refs.coder.style.height = this.$refs.coderLines.scrollHeight + "px";
      } else if (event.key === Key.BACKSPACE) {
        this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight - project_DIFF_HEIGHT) + "px";
      } else if (event.key === Key.BRACKET_RIGHT) {
        this.writeAction(event, `{${POS_CHAR}}`, true);
      } else if (event.key === Key.SQUARE_BRACKET_RIGHT) {
        this.writeAction(event, `[${POS_CHAR}]`, true);
      } else if (event.key === Key.PARENTESIS_RIGHT) {
        this.writeAction(event, `(${POS_CHAR})`, true);
      } else if (event.key === Key.TAB) {
        event.preventDefault();
        this.writeAction(event, TAB_CHAR, false);
      } else if (event.key === Key.DOUBLE_BACKQUOTE) {
        this.writeAction(event, `"${POS_CHAR}"`, true);
      } else if (event.key === Key.BACKQUOTE) {
        this.writeAction(event, `'${POS_CHAR}'`, true);
      }
    });
  },
  methods: {
    getAgentIcon(agentType) {
      if (agentType === "Argo") {
        return "argo-agent.svg";
      } else if (agentType === "Communicator") {
        return "communicator-agent.svg";
      } else {
        return "jason-agent.svg";
      }
    },
    projectIsInvalid() {
      if (this.project.agents.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "Unable to start SMA without agents", type: MessageType.ERROR});
        return true;
      }

      let hasSameName = false;
      for (let agent in this.project.agents) {
        for(let anotherAgent in this.project.agents) {
          if (agent !== anotherAgent && agent.name === anotherAgent.name) {
            hasSameName = true;
            break;
          }
        }
        if (hasSameName) {
          return;
        }
      }

      if (hasSameName) {
        this.$emit(AppEvent.MESSAGE, {content: "There are agents with the same name", type: MessageType.ERROR});
        return true;
      }

      return false;
    },
    performBoardOperationWithDifferentFqbn(fqbn, operation, popUpToClose) {
      popUpToClose.showing(false);
      if (this.currentBoard == null) {
        this.currentBoard = {
          fqbn: fqbn
        }
      } else {
        this.currentBoard.fqbn = fqbn;
      }

      this.$refs.deployBoardUnknownPopup.showing(false);
      operation();

      if (this.currentBoard.name == null) {
        this.currentBoard = null;
      } else {
        this.currentBoard.fqbn = UNKNOW_BOARD_MODEL_STRING;
      }
    },
    compileSketch() {
      if (this.currentBoard == null || this.isCurrentBoardUnknown) {
        this.$refs.compileBoardUnknownPopup.showing(true);
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
        this.$emit(AppEvent.MESSAGE, {content: "There is no board selected", type: MessageType.WARNING});
        return;
      }

      if (this.isCurrentBoardUnknown) {
        this.$refs.deployBoardUnknownPopup.showing(true);
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
          this.$emit(AppEvent.MESSAGE, {content: "No file selected", type: MessageType.WARNING});
          return;
        }
        let libraryFile = files[0];
        if (Util.isFileInvalid(libraryFile)) {
          this.$emit(AppEvent.MESSAGE, {content: "File name cannot have space", type: MessageType.ERROR});
          return;
        }
        API.post(EndPoints.LIBRARIES_IMPORT, Headers.MULTIPART_CONFIG, {file: libraryFile}).then((response) => {
          if (response.data.status === 200) {
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
      return API.put(EndPoints.PROJECTS, {}, this.project).then(() => {
        setTimeout(() => {
          this.savingProject = false;
        }, 100);
      });
    },
    downloadMas() {
      if (this.projectIsInvalid()) {
          return;
      }
      API.post(EndPoints.MAS,{responseType: 'blob'}, this.project).then((response) => {
        if (response.status === 200) {
          const filename = this.project.name + ".zip";
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
      }, this.project).then((response) => {
        this.$emit(AppEvent.MESSAGE, {
          content: response.data.data.message,
          type: MessageType.SUCCESS
        });
      }).finally(() => {
        this.startingMas = false;
      });
    },
    stopMas() {
      this.stopingMas = true;
      API.put(EndPoints.MAS, {params: {action: "stop"}}).then((response) => {
        if (response.data.status === 202) {
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
        if (this.boards.length !== 0) {
          this.currentBoard = this.boards[0];
        }
      }).finally(() => {
        this.loadingBoards = false;
      });
    },
    removeFileAction(index, files) {
      if (this.currentFile === files[index]) {
        if (index === 0) {
          files.splice(index, 1);
          if (files.length === 0) {
            this.currentFile = {name: "No file", sourceCode: ""};
            this.agentFileIsOpen = false;
            this.firmwareFileIsOpen = false;
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
      this.project.firmwares.push({
        name: FIRMWARE_DEFAULT_FILE_NAME,
        sourceCode: defaultSourceCode.firmware
      });
    },
    addAgentFileAction() {
      this.project.agents.push({
        name: AGENT_DEFAULT_FILE_NAME + (this.project.agents.length === 0 ? '' : this.project.agents.length + 1),
        archClass: AgentType.JASON,
        sourceCode: defaultSourceCode.agent
      });
    },
    deleteLibrary(library) {
      API.delete(EndPoints.LIBRARIES, {params: {name: library.name}}).then(() => {
          this.loadLibraries(true);
      }).catch(() => {
          this.$emit(AppEvent.MESSAGE, {content: `It was not possible delete library ${library.name}`, type:
              MessageType.ERROR});
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
  --bar-height: 42px;
  @apply overflow-y-hidden;
}

.project__compiled-response {
  background-color: var(--pallete-color-black-3);
  word-break: break-word;
  @apply p-2.5 rounded-sm text-lg;
}

.project__project-status {
  @apply select-none mr-1.5;
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

.project-action-separator {
  height: 20px;
  width: 1px;
  background-color: var(--pallete-color-black-4);
}

.project__coding__file {
  background-color: var(--pallete-color-black-3);
  @apply flex h-full items-center p-1.5 pl-2.5 gap-1.5 rounded-r-md;
}

.project__coding__file__selected {
  width: 8px;
  height: 8px;
  background-color: var(--pallete-color-main-1);
  @apply rounded-full mr-1.5;
}

.project__coding__file__name {
  max-width: 125px;
  background-color: var(--pallete-color-black-3);
  color: var(--pallete-text-main);
  text-overflow: ellipsis;
  @apply overflow-hidden whitespace-nowrap shrink-0 mr-2.5 select-none;
}

.project__coder {
  font-family: 'JetBrains Mono', monospace;
  height: calc(100vh - calc(2 * var(--bar-height)));
  --writer-font-size: 14px;
  background-color: var(--pallete-color-black-1);
  @apply flex overflow-y-scroll;
}

.project__coder__lines {
  min-width: 68px;
  background-color: var(--pallete-color-black-2);
  color: var(--pallete-text-aside);
  border-right: 5px solid var(--pallete-color-black-3);
  @apply min-h-full h-fit flex flex-col py-5;
}

.project__coder__line {
  font-size: var(--writer-font-size);
}

.project__coder__text {
  background-color: var(--pallete-color-black-1);
  color: var(--pallete-text-main);
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
  @apply overflow-y-auto p-1.5;
}

.project__header-bar {
  background-color: var(--pallete-color-black-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
  min-height: var(--bar-height);
  height: var(--bar-height);
  @apply flex justify-between items-center p-1.5;
}

.project__header-bar__title {
  @apply ml-1.5 select-none;
}

.project__boards {
  width: 350px;
  background-color: var(--pallete-color-black-2);
  border-left: 1px solid var(--pallete-color-black-1);
  height: calc(100vh - var(--bar-height));
  @apply flex flex-col overflow-y-auto;
}

</style>