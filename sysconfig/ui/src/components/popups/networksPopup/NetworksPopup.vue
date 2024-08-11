<template>
  <Popup can-close show width="fit-content" title="Establish connection" ref="networksPopup">
    <template v-slot:content>
      <NetworkForm
          v-if="currentNetwork != null"
          :network="currentNetwork"
          :loading="acting"

          @close="currentNetwork = null"
          @act="act($event)"
          @message="$emit(AppEvent.MESSAGE, $event)"
      />

      <div v-if="searchingNetworks" class="flex items-center justify-center">
        <Loading/>
      </div>
      <div v-else class="networks">
        <span v-if="networks.length === 0" class="text-aside">
          No available networks found
        </span>
        <Network
            v-for="(network, index) in networks"
            :key="index" :network="network"
            @click="selectNetwork(network)"
        />
      </div>
    </template>
    <template v-slot:action>
      <Button
          color="var(--pallete-color-black-3)"
          text="Connect manually"

          @click="openManualNetwork"
      />
      <Button
          color="var(--pallete-color-black-3)"
          text="Create own network (AP)"

          @click="openCreateNetwork"
      />
      <Button
          color="var(--pallete-color-black-3)"
          text="Forget all"

          @click="forgetNetworks"
      />
      <Button
          icon="refresh.svg"
          icon-ratio="12px"
          color="var(--pallete-color-black-3)"
          text="Reload"
          main-color

          @click="loadNetworks"
      />
    </template>
  </Popup>
</template>

<script>

import Button from "@/components/general/Button.vue";
import GeneralUtil from "@/utils/generalUtil";
import Loading from "@/components/general/Loading.vue";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {Routes} from "@/router/routes";
import Network from "@/components/popups/networksPopup/Network.vue";
import NetworkForm from "@/components/popups/networksPopup/NetworkForm.vue";
import {NetworkOperation} from "@/components/popups/networksPopup/operation";
import Popup from "@/components/general/Popup.vue";
import {AppEvent, MessageType} from "@/utils/enums";

export default {
  name: "NetworksPopup",
  computed: {
    AppEvent() {
      return AppEvent
    }
  },
  emits: ["message"],
  components: {Popup, NetworkForm, Network, Loading, Button},
  data() {
    return {
      networks: null,
      currentNetwork: null,
      searchingNetworks: true,
      networkPopUpIsOpen: false,
      isConnecting: false,
      acting: false
    }
  },
  mounted() {
    this.loadNetworks();
  },
  setup() {
    GeneralUtil.setTitle("Connect");
    Api.loadToken();
  },
  methods: {
    act(event) {
      this.acting = true;

      Api.post(event.network.operation === NetworkOperation.CONNECT ? EndPoints.NETWORKS_CLIENT : EndPoints.NETWORKS_AP, {
        params: {
          essid: event.network.essid,
          password: event.network.password,
          restart: event.restart
        }
      }, {}).then(() => {
        if (event.restart) {
          setTimeout(() => {
            this.currentNetwork = null;
            this.acting = false;
            this.$refs.networksPopup.close();
            this.$router.push(Routes.LOGIN);
          }, 2000);
        } else {
          this.currentNetwork = null;
          this.acting = false;
        }
      });
    },
    selectNetwork(network) {
      this.currentNetwork = network
    },
    openCreateNetwork() {
      this.currentNetwork = {
        essid: null,
        password: null,
        operation: NetworkOperation.AP,
        encryption: true
      }
    },
    openManualNetwork() {
      this.currentNetwork = {
        essid: null,
        password: null,
        operation: NetworkOperation.CONNECT,
        encryption: true
      }
    },
    loadNetworks() {
      this.searchingNetworks = true;
      Api.get(EndPoints.NETWORKS_STATUS).then((response) => {
        let connectedNetwork = response.data.data;
        Api.get(EndPoints.NETWORKS).then((response) => {
          let allNetworks = response.data.data;

          for (let i = 0; i < allNetworks.length; i++) {
            let network = allNetworks[i];
            if (network.essid === connectedNetwork.essid) {
              network.connected = true;
            }
            network.operation = NetworkOperation.CONNECT;
          }

          allNetworks.sort((n1, n2) => {
            if (n1.connected) {
              return -1;
            }

            if (n1.quality === n2.quality) {
              return 0;
            } else if (n1.quality > n2.quality) {
              return -1;
            } else {
              return 1;
            }
          });

          this.networks = allNetworks;
        }).then(() => {
          this.searchingNetworks = false;
        });
      });
    },
    forgetNetworks() {
      Api.delete(EndPoints.NETWORKS).then(() => {
        this.loadNetworks();
      });
    }
  }
}
</script>

<style scoped>

.networks {
  overflow-y: auto;
  @apply flex flex-col items-start gap-2.5 w-full h-full rounded-md;
}

</style>