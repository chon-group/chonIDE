<template>
  <div class="coder u-column">
    <Popup title="Resultado" v-if="boardResponse != null" type="now">
      <template v-slot:content>
        <div class="manager__compiled-response is-small">
          {{ boardResponse }}
        </div>
      </template>
    </Popup>

    <div class="coder__header u-row u-justify-i-between u-align-i-center">
      <h2 class="coder__header__logo u-row">
        chonIDE
      </h2>
      <div class="u-row u-height-cover">
        <Button icon="start.svg" transparent no-border adjust side-padding="10" icon-ratio="12">
          <template v-slot:content>
            Iniciar SMA
          </template>
        </Button>
        <Button icon="stop.svg" transparent no-border adjust side-padding="10" icon-ratio="12">
          <template v-slot:content>
            Parar SMA
          </template>
        </Button>
      </div>
    </div>

    <div class="u-row">
      <div class="coder__explorer u-column">
        <input type="text" class="coder__explorer__project-name" v-model="masName"
               placeholder="Nome do projeto" ref="masName">
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
              <button class="coder__explorer__action coder__explorer__item__add" @click="addAgentFile"></button>
            </div>
            <div v-for="(agent,index) in agentFiles" :key="index"
                 class="coder__explorer__item third-level u-row u-align-i-center u-gap-3" v-show="agentsOpen">
              <div class="u-height-cover u-width-cover u-row u-align-i-center u-gap-3" @click="showAgentFile(index)">
                <span class="coder__explorer__item__icon">Ag</span>
                <select class="coder__explorer__item__agent-type coder__explorer__action">
                  <option v-for="(type, index) in agentTypes" :key="index" value="type"
                          :selected="agent.type === type ? true : false">
                    {{ type }}
                  </option>
                </select>
                <span class="coder__explorer__item__name">{{ agent.name }}</span>
              </div>
              <button class="coder__explorer__action coder__explorer__item__remove"
                      @click="removeAgentFile(index)" v-if="index !== 0">
              </button>
            </div>
            <div class="coder__explorer__item second-level u-row u-align-i-center">
              <div class="u-height-cover u-width-cover u-row u-align-i-center" @click="firmwaresOpen = !firmwaresOpen">
                <img src="@/assets/media/icon/toggle.svg" class="coder__explorer__item__toggle" :class="firmwaresOpen ?
          'open' : ''">
                <span>Firmwares</span>
              </div>
              <button class="coder__explorer__action coder__explorer__item__add" @click="addFirmwareFile"></button>
            </div>
            <div v-for="(firmware,index) in firmwareFiles" :key="index"
                 class="coder__explorer__item third-level u-row u-align-i-center u-gap-3" v-show="firmwaresOpen">
              <div class="u-height-cover u-width-cover u-row u-align-i-center u-gap-3" @click="showFirmwareFile(index)">
                <span class="coder__explorer__item__icon">C++</span>
                <span class="coder__explorer__item__name">{{ firmware.name }}</span>
              </div>
              <button class="coder__explorer__action coder__explorer__item__remove"
                      @click="removeFirmwareFile(index)" v-if="index !== 0"></button>
            </div>
          </div>
        </div>
        <div class="coder__explorer__libraries">
          <div class="coder__explorer__item first-level u-row u-align-i-center">
            <span class="u-height-cover u-width-cover u-row u-align-i-center" @click="librariesOpen =
              !librariesOpen">
              <img src="@/assets/media/icon/toggle.svg" class="coder__explorer__item__toggle"
                   :class="librariesOpen ? 'open' : ''">
              <span>Bibliotecas</span>
            </span>
            <div class="u-row u-width-fit">
              <input type="file" style="display: none" id="add-library" @change="addLibrary($event)">
              <label type="file" class="coder__explorer__action coder__explorer__item__add"
                     for="add-library"></label>
              <span class="coder__explorer__action coder__explorer__item__refresh"></span>
            </div>
          </div>
        </div>
      </div>
      <div class="coder__coding u-column">
        <div class="coder__coding__controller u-row u-justify-i-between">
          <input type="text" class="coder__coding__file-name" ref="fileName"
                 placeholder="Nome do arquivo"
                 v-model="currentFile.name"/>
          <div class="u-row">
            <Button v-if="currentFile.fileType === 2" icon="upload.svg" transparent adjust icon-ratio="12"
                    no-border side-padding="10" no-pointer>
              <template v-slot:content>
                Compilar
              </template>
            </Button>
            <Button v-if="currentFile.fileType === 2" icon="white-circle.svg" transparent adjust icon-ratio="12"
                    no-border side-padding="10" no-pointer>
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
          <textarea class="coder__writer__text" ref="coder" v-model="currentFile.code" spellcheck="false"></textarea>
        </div>
      </div>
    </div>

  </div>

</template>

<script>
import PageUtils from "@/assets/js/util/PageUtils";
import router from "@/router";
import Button from "@/components/Button";

const LINE_BREAK_CHAR = "\n", TAB_CHAR = "\t", POS_CHAR = "$";
const CODER_DIFF_HEIGHT = 18;
const AGENT_TYPE_ARGO = "Argo", AGENT_TYPE_JASON = "Jason", AGENT_TYPE_COMMUNICATOR = "Comunicador";
const AGENT_DEFAULT_FILE_NAME = "Agente", FIRMWARE_DEFAULT_FILE_NAME = "sketch";
const FILE_TYPE_AGENT = 1, FILE_TYPE_FIRMWARE = 2;

export default {
  name: "Coder",
  components: {Button},
  data() {
    return {
      masName: "Projeto SMA",
      currentFile: {name: "", code: "", fileType: FILE_TYPE_AGENT},
      agentFiles: [],
      firmwareFiles: [],
      masOpen: true,
      agentsOpen: true,
      firmwaresOpen: true,
      librariesOpen: true,
      agentTypes: [AGENT_TYPE_ARGO, AGENT_TYPE_JASON, AGENT_TYPE_COMMUNICATOR],
      boardResponse: null
    }
  },
  computed: {
    lineQuantity() {
      if (this.currentFile.code === '') {
        return 1;
      }
      return this.currentFile.code.split(LINE_BREAK_CHAR).length;
    }
  },
  setup() {
    PageUtils.isLogged().then((response) => {
      if (response.data == false) {
        router.push("/");
      }
    });
    PageUtils.setTitle("Criando SMA");
  },
  mounted() {
    this.agentFiles = [
      {
        name: "Agente 1",
        type: AGENT_TYPE_JASON,
        code: "",
        fileType: FILE_TYPE_AGENT
      }
    ];
    this.firmwareFiles = [
      {
        name: "sketch 1",
        code: "",
        fileType: FILE_TYPE_FIRMWARE
      }
    ];

    // Carregando conteúdos dinâmicos.
    this.currentFile = this.agentFiles[0];

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
    write(event, text, setPositionInner) {
      event.preventDefault();
      setTimeout(() => {
        let selectionStart = this.$refs.coder.selectionStart;
        let beforeText = this.currentFile.code.substring(0, selectionStart);
        let afterText = this.currentFile.code.substring(selectionStart);
        this.currentFile.code = beforeText + text + afterText;
        let selectionPosition;

        if (setPositionInner) {
          selectionPosition = this.currentFile.code.indexOf(POS_CHAR);
          this.currentFile.code = this.currentFile.code.replace(POS_CHAR, "");
        } else {
          selectionPosition = selectionStart + text.length;
        }
        setTimeout(() => {
          this.$refs.coder.setSelectionRange(selectionPosition, selectionPosition);
        }, 0)
      }, 0);
    },
    removeAgentFile(index) {
      if (this.currentFile === this.agentFiles[index]) {
        this.currentFile = this.agentFiles[index - 1];
      }
      this.agentFiles.splice(index, 1);
    },
    removeFirmwareFile(index) {
      if (this.currentFile === this.firmwareFiles[index]) {
        this.currentFile = this.firmwareFiles[index - 1];
      }
      this.firmwareFiles.splice(index, 1);
    },
    showAgentFile(index) {
      this.currentFile = this.agentFiles[index];
    },
    showFirmwareFile(index) {
      this.currentFile = this.firmwareFiles[index];
    },
    addFirmwareFile() {
      this.firmwareFiles.push({
        name: FIRMWARE_DEFAULT_FILE_NAME + " " + (this.firmwareFiles.length + 1),
        code: "",
        fileType: FILE_TYPE_FIRMWARE
      });
    },
    addAgentFile() {
      this.agentFiles.push({
        name: AGENT_DEFAULT_FILE_NAME + " " + (this.agentFiles.length + 1),
        type: AGENT_TYPE_JASON,
        code: "",
        fileType: FILE_TYPE_AGENT
      });
    },
    addLibrary(input) {
      console.log(input);
    }
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
  width: 100vw;
  height: 100vh;
}

.coder *:not(input, textarea) {
  cursor: default;
}

.coder__header {
  padding: 0 var(--ratio-3);
  background-color: var(--pallete-color-black-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
  height: var(--bar-height);
  font-size: var(--base-font-size);
}

.coder__header__logo {
  font-size: 14px;
}

.coder__coding {
  width: calc(100vw - var(--explorer-width));
  border-left: 1px solid var(--pallete-color-black-1);
}

.coder__coding__controller {
  width: 100%;
  height: calc(var(--base-height) + var(--file-name-selected-height));
  background-color: var(--pallete-color-black-2);
  font-size: var(--base-font-size);
  padding-right: var(--ratio-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
}

.coder__coding__file-name {
  border: 0;
  padding: var(--ratio-4) var(--ratio-3);
  color: var(--pallete-text-main);
  height: var(--base-height);
  width: 125px;
  background-color: var(--pallete-color-black-3);
  box-shadow: 0 calc(var(--file-name-selected-height) - 1px) var(--pallete-color-main-2);
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
}

.coder__writer__lines {
  min-height: 100%;
  height: fit-content;
  width: 100px;
  background-color: var(--pallete-color-black-2);
  padding: var(--ratio-2) 0;
  color: var(--pallete-text-aside);
  border-right: var(--ratio-4) solid var(--pallete-color-black-3);
}

.coder__writer__line {
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
}

/* Lateral */

.coder__explorer {
  width: var(--explorer-width);
  background-color: var(--pallete-color-black-2);
  font-size: var(--base-font-size);
  --libraries-height: 250px;
}

.coder__explorer__main {
  overflow-y: auto;
  height: calc(100vh - var(--bar-height) - var(--libraries-height));
}

.coder__explorer__project-name {
  background-color: var(--pallete-color-black-3);
  border: 0;
  border-bottom: 1px solid var(--pallete-color-black-1);
  height: var(--bar-height);
  padding: 0 var(--ratio-3);
  color: var(--pallete-text-main);
}

.coder__explorer__project-name:hover {
  background-color: var(--pallete-color-black-4);
}

.coder__explorer__project-name:focus {
  background-color: var(--pallete-color-black-4);
}

.coder__explorer__item {
  width: 100%;
  height: var(--base-height);
  flex-shrink: 0;
  user-select: none;
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
  padding: 0 var(--ratio-3);
}

.coder__explorer__item.second-level {
  padding: 0 var(--ratio-2);
}

.coder__explorer__item.third-level {
  padding: 0 var(--ratio-1);
}

.coder__explorer__action {
  display: grid;
  place-items: center;
  border: 0;
  height: var(--base-height);
}

.coder__explorer__item__agent-type {
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
}

.coder__explorer__item__remove {
  opacity: 0;
  margin-left: auto;
  background: url("@/assets/media/icon/remove.svg") center transparent no-repeat;
  background-size: 40%;
  aspect-ratio: 1/1;
}

.coder__explorer__item:hover > .coder__explorer__item__remove {
  opacity: 1;
}

.coder__explorer__action:hover {
  background-color: var(--pallete-color-black-4);
}

.coder__explorer__item__add {
  background: url("@/assets/media/icon/add.svg") center transparent no-repeat;
  background-size: 50%;
  aspect-ratio: 1/1;
}

.coder__explorer__item__refresh {
  background: url("@/assets/media/icon/refresh.svg") center transparent no-repeat;
  background-size: 50%;
  aspect-ratio: 1/1;
}

.coder__explorer__libraries {
  border-top: 1px solid var(--pallete-color-black-1);
  height: calc(var(--libraries-height) - calc(2 * var(--bar-height)));
}

</style>