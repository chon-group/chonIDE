<template>
  <div class="u-column u-align-i-center u-gap-2">
    <h2 class="connect__message">Conecte à uma rede wifi para acesso à internet</h2>

    <div class="u-row u-gap-3 u-justify-i-center">
      <Popup title="Levantar uma rede própria AP" for="network-customized" ref="customized-network-pop-up">
        <template v-slot:content>
          <div class="u-column u-gap-3">
            <Input placeholder="Nome da rede (SSID)" type="text" name="essid" ref="customized-network-essid-input"/>
            <Input placeholder="Senha da rede (Mínimo 8 caracteres)" type="password" name="password"
                   ref="customized-network-password-input"/>
          </div>
        </template>
        <template v-slot:action>
          <div class="u-row u-gap-2 u-align-i-center">
            <Button @click="createNetwork" :is-loading="isConnecting">
              <template v-slot:content>
                Criar rede
              </template>
            </Button>
          </div>
        </template>
      </Popup>
      <Popup title="Conectar manualmente" for="network-manual" ref="manual-network-pop-up">
        <template v-slot:content>
          <div class="u-column u-gap-3">
            <Input placeholder="Nome da rede (SSID)" type="text" name="essid" ref="manual-network-essid-input"/>
            <Input placeholder="Senha da rede" type="password" name="password"
                   ref="manual-network-password-input"/>
          </div>
        </template>
        <template v-slot:action>
          <div class="u-row u-gap-2 u-align-i-center">
            <Button @click="connectManualNetwork" :is-loading="isConnecting">
              <template v-slot:content>
                Conectar à rede
              </template>
            </Button>
          </div>
        </template>
      </Popup>
      <Button class="network-manual" skin="transparent">
        <template v-slot:content>
          Conectar manualmente
        </template>
      </Button>
      <Button class="network-customized" skin="transparent">
        <template v-slot:content>
          Criar rede própria (AP)
        </template>
      </Button>
      <Button @click="getNetworks" icon="refresh.svg" icon-ratio="15.5">
        <template v-slot:content>
          Atualizar
        </template>
      </Button>
    </div>

    <Loading v-if="isSearching"/>

    <span v-if="networks == null" class="is-aside is-big">
      Procurando redes disponíveis...
    </span>

    <div v-else class="networks u-column u-align-i-center u-gap-3">
      <span v-if="networks.length === 0" class="is-aside is-big">
        Não foram encontradas redes disponíveis
      </span>
      <div class="c-base-container network u-row u-justify-i-between u-align-i-center"
           v-for="(network, index) in networks" :key="index" :class="'network-'+index" @click="networkPopUpIsOpen =
           true">
        <div class="u-row u-gap-2 u-align-i-center">
          <div style="position: relative">
            <img v-if="network.quality < 15" class="network__quality-indicator"
                 src="@/assets/media/icon/wifi-quality-1.svg">
            <img v-else-if="network.quality < 30" class="network__quality-indicator"
                 src="@/assets/media/icon/wifi-quality-2.svg">
            <img v-else-if="network.quality < 65" class="network__quality-indicator"
                 src="@/assets/media/icon/wifi-quality-3.svg">
            <img v-else class="network__quality-indicator" src="@/assets/media/icon/wifi-quality-4.svg">
            <img v-if="network.encryption === true && network.password === null" class="network__lock"
                 src="@/assets/media/icon/lock.svg">
          </div>
          <span class="network__essid">{{ network.essid }}</span>
        </div>
        <span class="network__connected is-bold"
              v-if="connectedNetwork.essid == network.essid && connectedNetwork.frequency == network.frequency">
          Conectado
        </span>
        <Popup title="Conectar-se à rede" :for="'network-'+index" ref="network">
          <template v-slot:content>
            <div class="u-column u-gap-3">
              <Input type="text" readonly="true"
                     :model-value="network.essid" name="essid"/>
              <Input v-show="network.encryption === true" :model-value="network.password" placeholder="Senha da rede"
                     type="password"
                     name="password"/>
            </div>
          </template>
          <template v-slot:action>
            <div class="u-row u-gap-2 u-align-i-center">
              <Button @click="connectNetwork(index)" :is-loading="isConnecting">
                <template v-slot:content>
                  Conectar à rede
                </template>
              </Button>
            </div>
          </template>
        </Popup>
      </div>
    </div>
    <router-link to="/manager" v-if="!isFirstAccess">
      <Button skin="navigation">
        <template v-slot:content>
          Voltar para gerenciador
        </template>
      </Button>
    </router-link>
  </div>
</template>

<script>

import Button from "@/components/Button";
import Popup from "@/components/Popup";
import Input from "@/components/Input";
import PageUtils from "@/assets/js/util/PageUtils";
import axios from "axios";
import Loading from "@/components/Loading";
import router from "@/router";
import {MessageType} from "@/assets/js/model/Enums"

export default {
  name: "Connect",
  components: {Loading, Input, Popup, Button},
  data() {
    return {
      networks: null,
      connectResponse: null,
      isSearching: true,
      networkPopUpIsOpen: false,
      connectedNetwork: null,
      isConnecting: false,
      isFirstAccess: false
    }
  },
  mounted() {
    PageUtils.isFirstAccess().then((response) => {
      this.isFirstAccess = response.data;
    });
    axios.get("/sysconfig/networks/status").then((response) => {
      this.connectedNetwork = response.data;
    });
    this.getNetworks();
  },
  setup() {
    PageUtils.isLogged().then((response) => {
      if (response === false) {
        router.push("/");
      }
    });
    PageUtils.setTitle("Conectar");
  },
  methods: {
    getNetworks() {
      this.isSearching = true;
      axios.get("/sysconfig/networks").then((response) => {
        this.networks = response.data;
        this.isSearching = false;
      });
    },
    createNetwork() {
      let essid = this.$refs['customized-network-essid-input'].$refs.input.value;
      let password = this.$refs['customized-network-password-input'].$refs.input.value;

      if (password.length < 8) {
        this.$root.message({
          content: "A senha da rede precisa ser igual ou mais que 8 caracteres", type:
          MessageType.ERROR
        });
        return;
      }

      this.isConnecting = true;
      axios.post("/sysconfig/networks/ap", {
        essid: essid,
        password: password
      }).then(() => {
        this.isConnecting = false;
        this.$refs['customized-network-pop-up'].close();
        localStorage.setItem("connecting", "true");
        router.push("/");
      });
    },
    connectNetwork(networkIndex) {
      let essid = this.$refs['network'][networkIndex].$el.querySelector("input[name='essid']").value;
      let password = this.$refs['network'][networkIndex].$el.querySelector("input[name='password']").value;

      this.isConnecting = true;
      axios.post("/sysconfig/networks/client", {
        essid: essid,
        password: password
      }).then(() => {
        this.isConnecting = false;
        this.$refs['network'][networkIndex].close();
        localStorage.setItem("connecting", "true");
        router.push("/");
      });
    },
    connectManualNetwork() {
      let essid = this.$refs['manual-network-essid-input'].$refs.input.value;
      let password = this.$refs['manual-network-password-input'].$refs.input.value;

      this.isConnecting = true;
      axios.post("/sysconfig/networks/client", {
        essid: essid,
        password: password
      }).then(() => {
        this.isConnecting = false;
        this.$refs['manual-network-pop-up'].close();
        localStorage.setItem("connecting", "true");
        router.push("/");
      });
    }
  }
}
</script>

<style scoped>

.connect__message {
  font-size: var(--text-size-title);
}

.networks {
  overflow: auto;
  max-height: calc(100vh / 1.5);
  min-height: 100px;
  padding: var(--ratio-4);
  min-width: var(--container-width-2);
}

.network {
  width: var(--container-width-2);
  cursor: pointer;
  transition: var(--background-hover-transition), transform 0.2s;
}

.network:hover {
  background: var(--pallete-color-black-3);
  transform: scale(1.01);
}

.network__essid {
  font-size: var(--text-size-headline);
  line-height: 1;
}

.network__quality-indicator {
  width: 30px;
}

.network__lock {
  right: -2px;
  bottom: -3px;
  position: absolute;
  height: 16px;
}

.network__connected {
  color: var(--pallete-color-green-2);
}

</style>