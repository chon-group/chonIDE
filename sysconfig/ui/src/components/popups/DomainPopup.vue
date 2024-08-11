<script>
import GeneralUtil from "@/utils/generalUtil";
import {AppEvent, MessageType} from "@/utils/enums";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import Popup from "@/components/general/Popup.vue";
import Loading from "@/components/general/Loading.vue";
import Button from "@/components/general/Button.vue";
import {Routes} from "@/router/routes";
import RebootPopup from "@/components/popups/RebootPopup.vue";

export default {
  name: "DomainPopup",
  components: {RebootPopup, Button, Loading, Popup},
  data() {
    return {
      domainName: '',
      domainStatus: null,
      loading: false
    }
  },
  emits: ["message"],
  mounted() {
    Api.get(EndPoints.DOMAINS).then((response) => {
      this.domainStatus = response.data.data;
      this.domainName = this.domainStatus.domain.substring(0, this.domainStatus.domain.indexOf("."));
    });
  },
  watch: {
    domainName(newValue) {
      this.domainName = GeneralUtil.mantainJustRegularCharacters(newValue).toLowerCase();
    }
  },
  methods: {
    renameBot(reboot) {
      this.$refs.rebootPopup.close();
      this.loading = true;
      Api.post(EndPoints.DOMAINS, {params: {domain: this.domainName}}).then(() => {
        this.$emit(AppEvent.MESSAGE, {content: "Your bot name was successfully saved", type: MessageType.SUCCESS});
      }).then(() => {
        if (reboot) {
          Api.put(EndPoints.SYSTEM_REBOOT).then(() => {
            this.loading = false;
            this.$refs.popup.close();
            this.$router.push(Routes.LOGIN);
          });
        } else {

          this.loading = false;
        }
      });
    },
    openRebootPopup() {
      if (this.domainName === '' || this.domainName.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "Name cannot be empty", type: MessageType.ERROR});
        return;
      }

      this.$refs.rebootPopup.show();
    }
  }
}
</script>

<template>
  <Popup can-close is-children title="Domain status" ref="popup">
    <template v-slot:content>
      <div class="flex items-center justify-center" v-if="domainStatus === null">
        <Loading/>
      </div>
      <div v-else class="flex flex-col gap-2.5 items-center w-full">
        <div class="domain" @click="$refs.input.focus()">
          <span class="text-base">Bot name is</span>
          <input v-model="domainName" maxlength="30" type="text" ref="input">
        </div>
        <div class="addresses">
          <div class="flex items-center gap-2.5">
            <strong class="text-aside">Local Address</strong>
            <strong class="text-aside">{{ domainStatus.localAddress }}</strong>
          </div>
          <div class="flex items-center gap-2.5">
            <strong class="text-aside">Registered Address</strong>
            <strong class="text-aside">{{ domainStatus.registredAddress }}</strong>
          </div>
        </div>
        <Button main-color height="55px" width icon="check.svg" width-full @click="openRebootPopup" text="Save bot name" :is-loading="loading"/>
        <RebootPopup ref="rebootPopup" @act="(event) => renameBot(event.reboot)"/>
      </div>
    </template>
  </Popup>
</template>

<style scoped>

.addresses {
  border: var(--border-trace);
  @apply flex flex-col gap-2.5 p-2.5 items-center rounded-md w-full;
}

.domain {
  border: var(--border-trace);
  cursor: pointer;
  @apply flex flex-col items-center gap-1 p-2.5 w-full rounded-md;
}

.domain:hover {
 background-color: var(--pallete-color-black-3);
}

.domain > input {
  color: var(--pallete-text-main);
  border: none;
  cursor: pointer;
  @apply bg-transparent text-center w-full text-4xl;
}

</style>