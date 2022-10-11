<template>
  <div class="coder u-column">
    <Popup :title="'Compilado para ' + currentBoard.board" v-if="boardResponse != null" type="now">
      <template v-slot:content>
        <div class="coder__compiled-response is-small">
          {{ boardResponse }}
        </div>
      </template>
    </Popup>

    <div class="coder__header u-row u-justify-i-between u-align-i-center">
      <h2 class="coder__header__logo u-row">
        chonIDE
      </h2>
      <div class="u-row u-height-cover">
        <Button transparent no-border adjust side-padding="10" icon-ratio="12" @click="showLogMonitor = true"
                v-if="domain != null" class="sma-logs-button" :link="'http://' + domain.domain + ':3271'">
          <template v-slot:content>
            Logs do SMA
          </template>
        </Button>
        <Button icon="start.svg" transparent no-border adjust side-padding="10" icon-ratio="12" @click="startMas"
                :is-loading="startingMas">
          <template v-slot:content>
            Iniciar SMA
          </template>
        </Button>
        <Button icon="stop.svg" transparent no-border adjust side-padding="10" icon-ratio="12" @click="stopMas"
                :is-loading="stopingMas">
          <template v-slot:content>
            Parar SMA
          </template>
        </Button>
        <Button icon="upload.svg" transparent no-border adjust side-padding="10" @click="importMas" icon-ratio="12"
                :is-loading="importingMas">
          <template v-slot:content>
            Importar SMA
          </template>
        </Button>
      </div>
    </div>

    <div class="u-row">
      <div class="coder__explorer u-column">
        <div class="coder__header-bar coder__explorer__project-name u-row">
          <input type="text" v-model="projectName"
                 placeholder="Nome do projeto" ref="masName">
          <div class="coder__project-status is-aside">
            <div v-if="savingProject" class="u-row u-gap-3">
              <Loading border-width="1" ratio="14" main-color="var(--pallete-text-main)"/>
              Salvando projeto
            </div>
            <div v-else class="u-row u-gap-3">
              <img src="@/assets/media/icon/check.svg" style="width: 13px">
              Projeto salvo
            </div>
          </div>
        </div>
        <div class="coder__explorer__main">
          <span class="coder__explorer__item first-level u-row u-align-i-center" @click="masOpen = !masOpen">
            <img src="@/assets/media/icon/toggle.svg" class="coder__explorer__item__toggle"
                 :class="masOpen ? 'open' : ''">
            Sistema multiagente
          </span>
          <div class="u-column" v-show="masOpen">
            <div class="coder__explorer__item second-level u-row u-align-i-center">
              <div class="u-height-cover u-width-cover u-row u-align-i-center" @click="agentsOpen = !agentsOpen">
                <img src="@/assets/media/icon/toggle.svg" class="coder__explorer__item__toggle" :class="agentsOpen ?
                  'open' : ''">
                <span>Agentes</span>
              </div>
              <button class="coder__action is-add" @click="addAgentFile"></button>
            </div>
            <div v-for="(agent,index) in agents" :key="index"
                 class="coder__explorer__item third-level u-row u-align-i-center u-gap-3" v-show="agentsOpen">
              <div class="u-height-cover u-width-cover u-row u-align-i-center u-gap-3" @click="showAgentFile(index)">
                <span class="coder__explorer__item__icon">Ag</span>
                <select class="is-agent-type coder__action" v-model="agent.archClass">
                  <option v-for="(type, index) in agentTypes" :key="index" :value="type"
                          :selected="agent.archClass === type">
                    {{ type }}
                  </option>
                </select>
                <span class="coder__explorer__item__name">{{ agent.name }}</span>
              </div>
              <button class="coder__action is-remove"
                      @click="removeAgentFile(index)" v-if="index !== 0">
              </button>
            </div>
            <div class="coder__explorer__item second-level u-row u-align-i-center">
              <div class="u-height-cover u-width-cover u-row u-align-i-center" @click="firmwaresOpen = !firmwaresOpen">
                <img src="@/assets/media/icon/toggle.svg" class="coder__explorer__item__toggle" :class="firmwaresOpen ?
          'open' : ''">
                <span>Firmwares</span>
              </div>
              <button class="coder__action is-add" @click="addFirmwareFile"></button>
            </div>
            <div v-for="(firmware,index) in firmwares" :key="index"
                 class="coder__explorer__item third-level u-row u-align-i-center u-gap-3" v-show="firmwaresOpen">
              <div class="u-height-cover u-width-cover u-row u-align-i-center u-gap-3" @click="showFirmwareFile(index)">
                <span class="coder__explorer__item__icon">C++</span>
                <span class="coder__explorer__item__name">{{ firmware.name }}</span>
              </div>
              <button class="coder__action is-remove"
                      @click="removeFirmwareFile(index)" v-if="index !== 0"></button>
            </div>
          </div>
        </div>
        <div class="coder__explorer__libraries">
          <div class="coder__header-bar u-row u-justify-i-between">
            <span class="coder__header-bar__title">
              Bibliotecas
            </span>
            <div class="u-row u-width-fit u-height-cover">
              <input type="file" style="display: none" id="add-library" @change="importLibrary($event)" accept=".zip">
              <label type="file" class="coder__action is-add"
                     for="add-library"></label>
              <div class="coder__action is-refresh" @click="loadLibraries(true)"></div>
            </div>
          </div>
          <div class="u-total-center u-height-cover u-width-cover" v-if="loadingLibraries">
            <Loading border-width="2" main-color="var(--pallete-text-main)" ratio="25"/>
          </div>
          <div class="coder__explorer__item first-level u-row u-align-i-center" v-for="(library, index) in libraries"
               :key="index">
            <span>{{ library.name }}</span>
          </div>
        </div>
      </div>
      <div class="coder__coding u-column">
        <div class="coder__coding__controller u-row u-justify-i-between">
          <input type="text" class="coder__coding__file-name" ref="fileName"
                 placeholder="Nome do arquivo"
                 v-model="currentFile.name"/>
          <div class="u-row">
            <Button v-if="firmwareFileIsOpen" icon="upload.svg" transparent adjust icon-ratio="12"
                    no-border side-padding="10" no-pointer @click="compileSketch" :is-loading="compilingSketch">
              <template v-slot:content>
                Compilar
              </template>
            </Button>
            <Button v-if="firmwareFileIsOpen" icon="white-circle.svg" transparent adjust icon-ratio="12"
                    no-border side-padding="10" no-pointer @click="deploySketch" :is-loading="deployingSketch">
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
          <textarea class="coder__writer__text" ref="coder" v-model="currentFile.sourceCode"
                    spellcheck="false"></textarea>
        </div>
      </div>
      <div class="coder__boards u-column" v-if="firmwareFileIsOpen">
        <div class="coder__header-bar u-row u-justify-i-between">
          <span class="coder__header-bar__title">Placas disponíveis</span>
          <div class="coder__action is-refresh" @click="loadBoards(true)" style="background-size: 45%"></div>
        </div>
        <div class="u-total-center u-height-cover u-width-cover" v-if="loadingBoards">
          <Loading border-width="2" main-color="var(--pallete-text-main)" ratio="25"/>
        </div>
        <div class="u-total-center u-height-cover u-width-cover" v-else-if="boards.length === 0 && !loadingBoards">
          <span class="is-aside">Não foram encontradas placas disponíveis</span>
        </div>
        <div class="coder__board u-column u-justify-i-between"
             v-else
             v-for="(board, index) in boards" :key="index"
             @click="currentBoard = board"
        >
          <div class="u-row u-justify-i-between">
            <span class="u-column">
              <span>{{ board.board }}</span>
              <span class="is-aside">{{ board.fqbn }}</span>
            </span>
            <div class="coder__board__select" :class="board === currentBoard ? 'is-selected' : ''"></div>
          </div>
          <span class="is-aside">{{ board.port }}</span>
        </div>
      </div>
    </div>

  </div>

</template>

<script>
import PageUtils from "@/assets/js/util/PageUtils";
import Button from "@/components/Button";
import axios from "axios";
import {MessageType} from "@/assets/js/model/Enums";
import Loading from "@/components/Loading";
import Popup from "@/components/Popup";

const LINE_BREAK_CHAR = "\n", TAB_CHAR = "\t", POS_CHAR = "$";
const CODER_DIFF_HEIGHT = 18;
const AGENT_TYPE_ARGO = "Argo", AGENT_TYPE_JASON = "Jason", AGENT_TYPE_COMMUNICATOR = "Communicator";
const AGENT_DEFAULT_FILE_NAME = "Agente", FIRMWARE_DEFAULT_FILE_NAME = "sketch";

export default {
  name: "Coder",
  components: {Loading, Button, Popup},
  data() {
    return {
      projectName: "",
      currentFile: {},
      currentBoard: null,
      firmwareFileIsOpen: false,
      domain: null,
      agents: [],
      firmwares: [],
      boards: [],
      libraries: [],
      masOpen: true,
      agentsOpen: true,
      firmwaresOpen: true,
      agentTypes: [AGENT_TYPE_ARGO, AGENT_TYPE_JASON, AGENT_TYPE_COMMUNICATOR],
      boardResponse: null,
      savingProject: false,
      importingMas: false,
      startingMas: false,
      stopingMas: false,
      compilingSketch: false,
      deployingSketch: false,
      loadingBoards: false,
      loadingLibraries: false,
      showLogMonitor: false
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
      this.projectName = newValue.replace("-", "").replace(" ", "");
      this.saveProject();
    }
  },
  computed: {
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
    PageUtils.setTitle("Criando SMA");
  },
  mounted() {
    axios.get("/sysconfig/projects").then((response) => {
      this.agents = response.data.agents;
      this.firmwares = response.data.firmwares;
      this.projectName = response.data.name;
      this.currentFile = this.agents[0];
    });

    axios.get("/sysconfig/domains").then((response) => {
      this.domain = response.data;
    });

    this.loadBoards(false).then(() => {
      if (this.boards.length !== 0) {
        this.currentBoard = this.boards[0];
      }
    });

    this.loadLibraries(false);

    // Implementação do codador.
    this.$refs.coder.addEventListener("keydown", (event) => {
      if (event.keyCode === 13) {
        this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight + CODER_DIFF_HEIGHT) + "px";
      } else if (event.keyCode === 8) {
        this.$refs.coder.style.height = (this.$refs.coderLines.scrollHeight - CODER_DIFF_HEIGHT) + "px";
      } else if (event.keyCode === 221) {
        if (event.shiftKey) {
          this.write(event, '{' + LINE_BREAK_CHAR + TAB_CHAR + POS_CHAR + LINE_BREAK_CHAR + '}', true);
        } else {
          this.write(event, `[${POS_CHAR}]`, true);
        }
      } else if (event.keyCode === 57 && event.shiftKey) {
        this.write(event, `(${POS_CHAR})`, true);
      } else if (event.keyCode === 9) {
        event.preventDefault();
        this.write(event, TAB_CHAR, false);
      } else if (event.keyCode === 192) {
        this.write(event, `"${POS_CHAR}"`, true);
      }
    });
  },
  methods: {
    projectIsInvalid() {
      if (this.projectName.length === 0) {
        this.$emit("message", {content: "O nome do projeto não pode ser vazio", type: MessageType.ERROR});
        return true;
      }
      return false;
    },
    compileSketch() {
      if (this.currentBoard == null) {
        this.$emit("message", {content: "Não existe nenhuma placa selecionada", type: MessageType.WARNING});
        return;
      }
      this.compilingSketch = true;
      axios.post("/sysconfig/sketchs/compile", {}, {
        params: {
          boardName: this.currentBoard.fqbn,
          code: this.currentFile.sourceCode
        }
      }).then((response) => {
        this.boardResponse = response.data;
        this.compilingSketch = false;
      });
    },
    deploySketch() {
      if (this.currentBoard == null) {
        this.$emit("message", {content: "Não existe nenhuma placa selecionada", type: MessageType.WARNING});
        return;
      }
      this.deployingSketch = true;
      axios.post("/sysconfig/sketchs/deploy", {}, {
        params: {
          serialPort: this.currentBoard.port,
          boardName: this.currentBoard.fqbn
        }
      }).then((response) => {
        this.boardResponse = response.data;
        this.deployingSketch = false;
      });
    },
    saveProject() {
      this.savingProject = true;
      return axios.put("/sysconfig/projects", {
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
      this.startingMas = true;
      axios.put("/sysconfig/mas/start").then((response) => {
        this.$emit("message", {
          content: response.data.message,
          type: MessageType.SUCCESS
        });
        this.startingMas = false;
      });
    },
    stopMas() {
      this.stopingMas = true;
      axios.put("/sysconfig/mas/stop").then((response) => {
        console.log(response);
        if (response.data === '') {
          this.$emit("message", {content: response.data, type: MessageType.SUCCESS});
        } else {
          this.$emit("message", {content: "SMA já foi parado", type: MessageType.WARNING});
        }
        this.stopingMas = false;
      });
    },
    importMas() {
      if (this.projectIsInvalid()) {
        return;
      }
      this.importingMas = true;
      this.saveProject().then(() => {
        axios.post("/sysconfig/mas/import", {
          name: this.projectName,
          agents: this.agents
        }).then(() => {
          this.$emit("message", {content: "SMA importado com sucesso", type: MessageType.SUCCESS});
          this.importingMas = false;
        });
      });
    },
    removeAgentFile(index) {
      if (this.currentFile === this.agents[index]) {
        this.currentFile = this.agents[index - 1];
      }
      this.agents.splice(index, 1);
    },
    removeFirmwareFile(index) {
      if (this.currentFile === this.firmwares[index]) {
        this.currentFile = this.firmwares[index - 1];
      }
      this.firmwares.splice(index, 1);
    },
    showAgentFile(index) {
      this.currentFile = this.agents[index];
      this.firmwareFileIsOpen = false;
    },
    showFirmwareFile(index) {
      this.currentFile = this.firmwares[index];
      this.firmwareFileIsOpen = true;
    },
    addFirmwareFile() {
      this.firmwares.push({
        name: FIRMWARE_DEFAULT_FILE_NAME + " " + (this.firmwares.length + 1),
        sourceCode: "void setup() {\n"
            + "  // put your setup code here, to run once:\n" + "\n" + "}\n" + "\n" + "void loop() {\n"
            + "  // put your main code here, to run repeatedly:\n" + "\n" + "}",
      });
    },
    addAgentFile() {
      this.agents.push({
        name: AGENT_DEFAULT_FILE_NAME + " " + (this.agents.length + 1),
        archClass: AGENT_TYPE_JASON,
        sourceCode: "/* Initial beliefs and rules */\n" + "\n" + "/* Initial goals */\n" + "\n" + "!start.\n" + "\n"
            + "/* Plans */\n" + "\n" + "+!start <- .print(\"Hello world!\").",
      });
    },
    importLibrary(event) {
      let files = event.target.files;
      if (files.length === 0) {
        return;
      }
      if (files[0].name.includes(" ")) {
        this.$emit("message", {content: "O nome do arquivo não pode ter espaço", type: MessageType.ERROR});
        return;
      }

      this.loadingLibraries = true;
      axios.post("/sysconfig/libraries/import", {file: files[0]}, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }).then((response) => {
        if (response.status === 200) {
          this.$emit("message", {
            content: response.data,
            type: MessageType.SUCCESS
          });
          this.loadLibraries(true).then(() => {
            this.loadingLibraries = true;
          });
        } else {
          this.$emit("message", {
            content: response.data,
            type: MessageType.ERROR
          });
          this.loadingLibraries = true;
        }
      });
    },
    loadLibraries(refresh) {
      this.loadingLibraries = true;
      return axios.get("/sysconfig/libraries", {params: {refresh: refresh}}).then((response) => {
        this.libraries = response.data;
      }).then(() => {
        this.loadingLibraries = false;
      });
    },
    loadBoards(refresh) {
      this.currentBoard = null;
      this.loadingBoards = true;
      return axios.get("/sysconfig/boards", {params: {refresh: refresh}}).then((response) => {
        this.boards = response.data;
        this.loadingBoards = false;
        this.currentBoard = this.boards[0];
      });
    },
    write(event, text, setPositionInner) {
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
  --base-height: 27px;
  --base-font-size: 11px;
  --file-name-selected-height: 4px;
  --bar-height: calc(var(--base-height) + var(--file-name-selected-height));
  font-size: 11px;
  width: 100vw;
  height: 100vh;
}

.coder__log-monitor {
  height: 500px;
  min-width: 1000px;
  border: 0;
  border-radius: var(--border-radius-item);
}

.coder__compiled-response {
  padding: var(--ratio-3);
  background-color: var(--pallete-color-black-3);
  border-radius: var(--border-radius-item);
}

.coder *:not(input, textarea) {
  cursor: default;
}

.coder__header {
  background-color: var(--pallete-color-black-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
  height: var(--bar-height);
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
  height: calc(var(--base-height) + var(--file-name-selected-height));
  background-color: var(--pallete-color-black-2);
  border-bottom: 1px solid var(--pallete-color-black-1);
}

.coder__project-status {
  margin: auto 0 auto auto;
  user-select: none;
}

.coder__coding__file-name {
  border: 0;
  padding: var(--ratio-4) var(--ratio-3);
  color: var(--pallete-text-main);
  height: calc(var(--base-height) + var(--file-name-selected-height) - 1px);
  width: 125px;
  background-color: var(--pallete-color-black-3);
  border-bottom: var(--file-name-selected-height) solid var(--pallete-color-main-2);
}

.coder__coding__file-name:hover {
  background-color: var(--pallete-color-black-4);
}

.coder__coding__file-name:focus {
  background-color: var(--pallete-color-black-4);
}

.coder__writer {
  width: 100%;
  height: calc(100vh - calc(2 * var(--bar-height)));
  overflow-y: scroll;
  font-size: var(--text-size-little);
  --writer-font-size: 12px;
}

.coder__writer__lines {
  min-height: 100%;
  height: fit-content;
  min-width: 90px;
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
  --libraries-height: 250px;
}

.coder__explorer__main {
  overflow-y: auto;
  height: calc(100vh - var(--bar-height) - var(--libraries-height));
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

.coder__explorer__item {
  width: 100%;
  height: var(--base-height);
  flex-shrink: 0;
  user-select: none;
  padding-right: var(--ratio-3);
}

.coder__explorer__item__toggle {
  height: 6px;
  margin-right: var(--ratio-3);
  transform: rotate(-90deg);
}

.coder__explorer__item__toggle.open {
  transform: rotate(0deg);
}

.coder__explorer__item:hover {
  background-color: var(--pallete-color-black-3);
}

.coder__explorer__item.first-level {
  padding-left: 10px;
}

.coder__explorer__item.second-level {
  padding-left: 30px;
}

.coder__explorer__item.third-level {
  padding-left: 50px;
}

.coder__action {
  display: grid;
  place-items: center;
  border: 0;
  height: 100%;
}

.coder__action.is-agent-type {
  text-align: left;
  color: var(--pallete-text-main);
  background-color: var(--pallete-color-black-3);
  height: 100%;
  padding: 0 var(--ratio-3);
  text-align: center;
}

.coder__explorer__item__name {
  white-space: nowrap;
  overflow: hidden;
  position: relative;
}

.coder__explorer__item__icon {
  font-weight: 1000;
  font-size: var(--text-size-little);
  color: var(--pallete-text-aside);
  width: 15px;
}

.coder__action.is-remove {
  opacity: 0;
  margin-left: auto;
  background: url("@/assets/media/icon/remove.svg") center transparent no-repeat;
  background-size: 40%;
  aspect-ratio: 1/1;
}

.coder__explorer__item:hover > .is-remove {
  opacity: 1;
}

.coder__action.is-add {
  background: url("@/assets/media/icon/add.svg") center transparent no-repeat;
  background-size: 50%;
  aspect-ratio: 1/1;
}

.coder__action.is-refresh {
  background: url("@/assets/media/icon/refresh.svg") center transparent no-repeat;
  background-size: 50%;
  aspect-ratio: 1/1;
}

.coder__action:hover {
  background-color: var(--pallete-color-black-4);
}

.coder__explorer__libraries {
  border-top: 1px solid var(--pallete-color-black-1);
  height: calc(var(--libraries-height) - calc(2 * var(--bar-height)));
}

/** Placas */

.coder__boards {
  width: 350px;
  background-color: var(--pallete-color-black-2);
  border-left: 1px solid var(--pallete-color-black-1);
  height: calc(100vh - var(--bar-height));
  overflow-y: auto;
}

.coder__board {
  height: 75px;
  padding: var(--ratio-3);
}

.coder__board:hover {
  background-color: var(--pallete-color-black-3);
}

.coder__board__select {
  height: 15px;
  aspect-ratio: 1/1;
  border-radius: var(--border-radius-total);
  border: 2px solid var(--pallete-color-black-4);
}

.coder__board__select.is-selected {
  background-color: var(--pallete-color-main-1);
}

</style>