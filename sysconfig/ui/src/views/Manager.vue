<template>
  <div class="manager u-column u-gap-2">
    <header class="manager__header u-row u-align-i-center u-justify-i-between">
      <div class="u-row u-gap-2 u-align-i-center">
        <Logo name="sysconfig"/>
        <div class="u-row u-gap-3">
          <router-link to="/domain">
            <Button skin="navigation" icon="domain.svg">
              <template v-slot:content>
                Nome do bot
              </template>
            </Button>
          </router-link>
          <router-link to="/connect">
            <Button skin="navigation" icon="wifi-quality-4.svg" icon-ratio="16">
              <template v-slot:content>
                Redes
              </template>
            </Button>
          </router-link>
          <a :href="'http://'+this.currentDomain+':3271'" target="_blank">
            <Button skin="navigation">
              <template v-slot:content>
                Logs do SMA
              </template>
            </Button>
          </a>
        </div>
      </div>
      <div class="u-row u-gap-2">
        <div class="u-row u-gap-3">
          <Button skin="transparent" icon="start.svg" :is-loading="isStartingMas" @click="startMas">
            <template v-slot:content>
              Iniciar SMA
            </template>
          </Button>
          <Button skin="transparent" icon="stop.svg" icon-ratio="14" :is-loading="isStopingMas" @click="stopMas">
            <template v-slot:content>
              Parar SMA
            </template>
          </Button>
          <Button icon="upload.svg" class="import-mas" :is-loading="isImportingMas">
            <template v-slot:content>
              Importar SMA
            </template>
          </Button>
          <Popup title="Importar SMA" for="import-mas" ref="pop-up-import-mas">
            <template v-slot:content>
              <div class="u-row u-gap-2">
                <Input type="file" ref="mas-file-input"/>
              </div>
            </template>
            <template v-slot:action>
              <Button @click="importMas" icon="upload.svg">
                <template v-slot:content>
                  Importar
                </template>
              </Button>
            </template>
          </Popup>
        </div>
        <div class="u-row u-gap-3">
          <Button icon="turn-off.svg" skin="transparent" border="rounded" @click="turnOff" icon-ratio="16"/>
          <Button icon="reset.svg" skin="transparent" border="rounded" @click="reset" icon-ratio="16"/>
          <Button icon="logout.svg" skin="transparent" border="rounded" @click="logout" icon-ratio="16"/>
        </div>
      </div>
    </header>
    <main class="manager__main u-row u-gap-2 u-justify-i-center">
      <div class="manager__boards u-column u-gap-2">
        <div class="u-row u-align-i-center u-justify-i-between">
          <h2 class="is-huge">Placas</h2>
          <Button icon="refresh.svg" skin="transparent" border="rounded" @click="refreshBoards"/>
        </div>
        <Loading v-if="isSearchingBoards" :class="'manager__loading'"/>
        <div v-else class="manager__boards__list">
          <span v-if="boards.length === 0" class="is-big is-aside">Não foram detectadas placas</span>
          <div class="c-base-container manager__board u-column u-gap-2 u-justify-i-between"
               v-for="(board, index) in boards"
               :key="index">
            <div class="u-row u-gap-3 u-align-i-center">
              <span class="is-big is-bold">{{ board.board }}</span>
              <span class="manager__board__indicator is-small">{{ board.fqbn }}</span>
            </div>
            <div class="u-column u-gap-3">
                <span class="u-row u-gap-3 u-align-i-center">
                  <span class="manager__board__indicator is-small">Núcleo</span>
                  <span class="manager__board__value">{{ board.core }}</span>
                </span>
              <span class="u-row u-gap-3 u-align-i-center">
                  <span class="manager__board__indicator is-small">Porta</span>
                  <span class="manager__board__value">{{ board.port }}</span>
                </span>
            </div>
            <div class="u-row u-gap-3 u-align-s-end u-align-i-center">
              <Button color="var(--pallete-color-green-1)" icon="white-circle.svg" icon-ratio="16"
                      @click="deploy(board.port, board.fqbn)" :is-loading="isDeploying">
                <template v-slot:content>
                  Deploy
                </template>
              </Button>
              <Button icon="upload.svg" class="" @click="code(board)">
                <template v-slot:content>
                  Firmware
                </template>
              </Button>
            </div>
          </div>
        </div>
      </div>

      <div class="manager__libraries u-column u-gap-2">
        <div class="u-row u-align-i-center u-justify-i-between">
          <h2 class="is-huge">Bibliotecas</h2>
          <div class="u-row u-gap-3">
            <Button icon="upload.svg" skin="transparent" border="rounded" class="upload-library"/>
            <Button icon="refresh.svg" skin="transparent" border="rounded" @click="refreshLibraries"/>
            <Popup title="Importar biblioteca" for="upload-library" ref="pop-up-import-library">
              <template v-slot:content>
                <div class="u-row u-gap-2">
                  <Input type="file" ref="upload-library-input" accept=".zip"/>
                </div>
              </template>
              <template v-slot:action>
                <Button @click="uploadLibrary" icon="upload.svg">
                  <template v-slot:content>
                    Importar
                  </template>
                </Button>
              </template>
            </Popup>
          </div>
        </div>
        <Loading v-if="isSearchingLibraries" :class="'manager__loading'"/>
        <div v-else class="manager__libraries__list u-column u-gap-3">
          <span v-if="libraries.length === 0" class="is-big is-aside">Não foram detectadas bibliotecas</span>
          <span class="manager__library" v-for="(library, index) in libraries" :key="index">
              {{ library.name }}
            </span>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import Button from "@/components/Button";
import Loading from "@/components/Loading";
import Popup from "@/components/Popup";
import Input from "@/components/Input";
import PageUtils from "@/assets/js/util/PageUtils";
import axios from "axios";
import router from "@/router";
import {MessageType} from "@/assets/js/model/Enums"
import Logo from "@/components/Logo";


export default {
  name: "Manager",
  components: {Logo, Input, Popup, Loading, Button},
  data() {
    return {
      boards: [{}],
      libraries: [],
      boardPopUpIsOpen: false,
      isSearchingBoards: true,
      isSearchingLibraries: true,
      isCompiling: false,
      isDeploying: false,
      isImportingMas: false,
      isStartingMas: false,
      isStopingMas: false,
      currentDomain: ''
    }
  },
  setup() {
    PageUtils.isLogged().then((response) => {
      if (response.data == false) {
        router.push("/");
      }
    });
    PageUtils.setTitle("Gerenciador");
  },
  mounted() {
    PageUtils.isFirstAccess().then((response) => {
      if (response.data == true) {
        router.push("/");
      } else {
        axios.get("/sysconfig/domains").then((response) => {
          this.currentDomain = response.data.domain;
        });
      }
    });
    this.getBoards();
    this.getLibraries();
  },
  methods: {
    turnOff() {
      this.$root.message({content: "Desligando sistema", type: MessageType.WARNING});
      axios.post("/sysconfig/system/poweroff");
      setTimeout(() => {
        router.push("/");
      }, 2000);
    },
    reset() {
      this.$root.message({content: "Reiniciando sistema", type: MessageType.WARNING});
      axios.post("/sysconfig/system/reboot");
      setTimeout(() => {
        router.push("/");
      }, 2000);
    },
    logout() {
      axios.delete("/sysconfig/users");
      router.push("/");
    },
    uploadLibrary() {
      let uploadLibraryInput = this.$refs['upload-library-input'].$refs.input;

      if (uploadLibraryInput.value.length === 0 || uploadLibraryInput.files.length === 0) {
        this.$root.message({content: "Você precisa selecionar um arquivo para importar", type: MessageType.ERROR});
        return;
      }

      let formData = new FormData();
      formData.append("file", uploadLibraryInput.files[0]);

      this.$refs['pop-up-import-library'].close();
      this.isSearchingLibraries = true;
      axios.post("/sysconfig/libraries/import", formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }).then((response) => {
        if (response.data === false) {
          this.$root.message({
            content: "Não foi possível importar essa biblioteca. Confira seu formato e validade",
            type: MessageType.ERROR
          });
          return;
        }
        this.getLibraries();
      });
    },
    startMas() {
      this.isStartingMas = true;
      axios.put("/sysconfig/mas/start").then((response) => {
        if (response.data != null || response.data.length != 0) {
          this.$root.message({
            content: response.data.message,
            type: MessageType.SUCCESS
          });
        } else {
          this.$root.message({
            content: "SMA já foi iniciado",
            type: MessageType.WARNING
          });
        }
        this.isStartingMas = false;
      }).catch(() => {
        this.isStartingMas = false;
      })
    },
    stopMas() {
      this.isStopingMas = true;
      axios.put("/sysconfig/mas/stop").then((response) => {
        if (response.data != null || response.data.length != 0) {
          this.$root.message({content: response.data, type: MessageType.SUCCESS});
        } else {
          this.$root.message({content: "SMA já está parado", type: MessageType.WARNING});
        }
        this.isStopingMas = false;
      }).catch(() => {
        this.isStopingMas = false;
      });
    },
    importMas() {
      let masFileInput = this.$refs['mas-file-input'].$refs.input;

      if (masFileInput.value.length === 0 || masFileInput.files.length === 0) {
        this.$root.message({content: "Você precisa selecionar um arquivo para importar", type: MessageType.ERROR});
        return;
      }

      let formData = new FormData();
      formData.append("file", masFileInput.files[0]);

      this.$refs['pop-up-import-mas'].close();
      this.isImportingMas = true;
      axios.post("/sysconfig/mas/import", formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }).then((response) => {
        this.boardResponse = response.data;
        this.isImportingMas = false;
      });
    },
    code(board) {
      axios.post("/sysconfig/sketch/board", {}, {params: {boardName: board.fqbn}}).then(() => {
        router.push("/coder");
      });
    },
    refreshBoards() {
      this.isSearchingBoards = true;
      this.getBoards();
    },
    getBoards() {
      axios.get("/sysconfig/boards").then((response) => {
        this.boards = response.data;
        this.isSearchingBoards = false;
      }).catch(() => {
        this.isSearchingBoards = false;
      });
    },
    refreshLibraries() {
      this.isSearchingLibraries = true;
      this.getLibraries();
    },
    getLibraries() {
      axios.get("/sysconfig/libraries").then((response) => {
        this.libraries = response.data;
        this.isSearchingLibraries = false;
      }).catch((error) => {
        this.isSearchingLibraries = false;
      });
    }
  }
}
</script>

<style scoped>

.manager {
  width: 100vw;
  height: 100vh;
  padding: var(--ratio-2);
}

.manager__header {
  width: 100%;
}

.manager__main {
  height: 100%;
}

.manager__boards, .manager__libraries {
  position: relative;
  padding: var(--ratio-2);
  border-radius: var(--border-radius-container);
  border: var(--border-trace);
  height: 100%;
}

.manager__boards {
  width: 100%;
}

.manager__boards__list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: auto;
  grid-gap: var(--ratio-3);
}

.manager__board {
  min-width: 400px;
}

.manager__board:after {
  content: "";
  display: block;
  position: absolute;
  left: 0;
  top: var(--ratio-2);
  width: 2px;
  height: 25px;
  background-color: var(--pallete-text-main);
  border-radius: 0 2px 2px 0;
}

.manager__board__indicator {
  padding: var(--ratio-4) var(--ratio-3);
  border-radius: var(--border-radius-item);
  background-color: var(--pallete-color-black-3);
}

.manager__libraries {
  min-width: 400px;
}

.manager__library {
  padding: var(--ratio-3) var(--ratio-2);
  border-radius: var(--border-radius-item);
  background-color: var(--pallete-color-black-2);
  border: var(--border-trace);
  filter: drop-shadow(var(--shadow-item-1));
}

.manager__libraries__list {
  overflow: auto;
}

.manager__compiled-response {
  padding: var(--ratio-3);
  background-color: var(--pallete-color-black-3);
  border-radius: var(--border-radius-item);
}

.manager__loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

</style>