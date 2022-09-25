<template>
  <div class="coder u-column">
    <Popup title="Resultado" v-if="boardResponse != null" type="now">
      <template v-slot:content>
        <div class="manager__compiled-response is-small">
          {{ boardResponse }}
        </div>
      </template>
    </Popup>

    <div class="coder__controller u-row u-gap-3 u-justify-i-end u-align-i-center">
      <div class="u-row u-gap-2" style="margin-right: auto">
        <router-link to="/manager">
          <div class="coder__controller__back">
            Voltar para gerenciador
          </div>
        </router-link>
      </div>
      <h2 class="coder__title is-normal is-bold">{{ board.fqbn }}</h2>
      <Button color="var(--pallete-color-green-1)" icon="white-circle.svg" icon-ratio="16"
              @click="deploy()">
        <template v-slot:content>
          Deploy
        </template>
      </Button>
      <Button icon="upload.svg" @click="compile">
        <template v-slot:content>
          Compilar
        </template>
      </Button>
    </div>

    <div class="coder__area u-row">
      <div class="coder__area__lines u-column" ref="coderLines">
        <div v-for="index in lineQuantity" :key="index" class="coder__area__line is-small">{{ index }}</div>
      </div>
      <textarea class="coder__area__text" ref="coder" v-model="code" spellcheck="false"></textarea>
      <div class="u-row u-gap-4 coder__area__status">
          <span class="coder__area__stats is-small">
            {{ lineQuantity }}
            linha<span v-if="lineQuantity !== 1">s</span>
          </span>
        <span class="coder__area__stats is-small">
            {{ letterQuantity }}
            caractere<span v-if="letterQuantity !== 1">s</span>
          </span>
      </div>
    </div>
  </div>

</template>

<script>
import PageUtils from "@/assets/js/util/PageUtils";
import router from "@/router";
import axios from "axios";
import {MessageType} from "@/assets/js/model/Enums";
import Button from "@/components/Button";

export default {
  name: "Coder",
  components: {Button},
  data() {
    return {
      code: '',
      board: {fqbn: "CoreArduino"},
      boardResponse: null
    }
  },
  computed: {
    lineQuantity() {
      if (this.code === '') {
        return 1;
      }
      return this.code.split("\n").length;
    },
    letterQuantity() {
      return this.code.length;
    }
  },
  setup() {
    PageUtils.isLogged().then((response) => {
      if (response.data == false) {
        router.push("/");
      }
    });
    axios.get("/sysconfig/sketch/board").then((response) => {
      if (response.data == null) {
        router.push("/");
      } else {
        this.board = response.data;
      }
    })
    PageUtils.setTitle("Codador");
  },
  mounted() {
    this.$refs.coder.addEventListener("keydown", (event) => {
      this.$refs.coder.style.height = this.$refs.coderLines.scrollHeight + 'px';
      if (event.keyCode === 221) {
        if (event.shiftKey) {
          this.write(event, '{\n\t%\n}', true);
        } else {
          this.write(event, '[%]', true);
        }
      } else if (event.keyCode === 57 && event.shiftKey) {
        this.write(event, '(%)', true);
      } else if (event.keyCode === 9) {
        event.preventDefault();
        this.write(event, '\t', false);
      } else if (event.keyCode === 192) {
        this.write(event, '"%"', true);
      }
    });
  },
  methods: {
    write(event, text, setPositionInner) {
      event.preventDefault();
      setTimeout(() => {
        let selectionStart = this.$refs.coder.selectionStart;
        let beforeText = this.code.substring(0, selectionStart);
        let afterText = this.code.substring(selectionStart);
        this.code = beforeText + text + afterText;
        let selectionPosition;

        if (setPositionInner) {
          selectionPosition = this.code.indexOf("%");
          this.code = this.code.replace("%", "");
        } else {
          selectionPosition = selectionStart + text.length;
        }
        setTimeout(() => {
          this.$refs.coder.setSelectionRange(selectionPosition, selectionPosition);
        }, 0)
      }, 0);
    },
    compile() {
      let formData = new FormData();
      formData.append("code", this.code);
      formData.append("boardName", this.board.fqbn);

      this.isCompiling = true;
      axios.post("/sysconfig/sketchs/compile/code", formData).then((response) => {
        if (response === null) {
          this.$root.message({content: "Não foi possível compilar o arquivo", type: MessageType.ERROR});
          return;
        }
        this.isCompiling = false;
        this.boardResponse = response.data;
        this.$root.message({content: "Arquivo compilado com sucesso", type: MessageType.SUCCESS});
      });
    },
    deploy() {
      this.isDeploying = true;
      axios.post("/sysconfig/sketchs/deploy", {}, {
        params: {boardName: this.board.fqbn, serialPort: this.board.port}
      }).then((response) => {
        this.isDeploying = false;
        this.boardResponse = response.data;
        this.$root.message({content: "Arquivo carregado com sucesso!", type: MessageType.SUCCESS});
      });
    },
  }
}
</script>

<style scoped>

.coder {
  width: 100vw;
  height: 100vh;
}

.coder__controller {
  padding: var(--ratio-3) var(--ratio-2);
  padding-left: 0;
  width: 100%;
  background-color: var(--pallete-color-black-2);
}

.coder__controller__back {
  padding: var(--ratio-3) var(--ratio-2);
  border-radius: 0 var(--border-radius-total) var(--border-radius-total) 0;
  color: var(--pallete-text-main);
}

.coder__controller__back:hover {
  background-color: var(--pallete-color-black-3);
}

.coder__title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.coder__explorer{
  width: 300px;
  height: 100%;
}

.coder__area {
  width: 100%;
  height: 100%;
  font-size: 14px;
  line-height: 130%;
  overflow-y: auto;
  background-color: var(--pallete-color-black-3);
}

.coder__area__status {
  position: fixed;
  right: var(--ratio-2);
  bottom: var(--ratio-2);
}

.coder__area__stats {
  white-space: nowrap;
  border-radius: var(--border-radius-item);
  padding: 2px var(--ratio-4);
  background-color: var(--pallete-color-black-3);
  height: fit-content;
}

.coder__area__lines {
  min-height: 100%;
  height: fit-content;
  width: 80px;
  background-color: var(--pallete-color-black-2);
  padding: var(--ratio-2) 0;
  color: var(--pallete-text-aside);
  border-right: var(--ratio-4) solid var(--pallete-color-black-3);
}

.coder__area__line {
  text-align: left;
  padding-left: var(--ratio-2);
}

.coder__area__text {
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

</style>