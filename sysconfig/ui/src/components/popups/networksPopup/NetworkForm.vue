<script>
import Popup from "@/components/general/Popup.vue";
import Input from "@/components/general/Input.vue";
import Button from "@/components/general/Button.vue";
import {NetworkOperation} from "@/components/popups/networksPopup/operation";
import {AppEvent, MessageType} from "@/utils/enums";
import RebootPopup from "@/components/popups/RebootPopup.vue";

export default {
  name: "NetworkForm",
  computed: {
    NetworkOperation() {
      return NetworkOperation
    },
    getQualityTextual() {
      if (this.network.quality < 0.15) {
        return "Terrible";
      } else if (this.network.quality < 0.30) {
        return "Bad"
      } else if (this.network.quality < 0.65) {
        return "Normal";
      } else {
        return "Great";
      }
    }
  },
  emits: ["close", "message", "act"],
  components: {RebootPopup, Button, Input, Popup},
  props: {
    network: {},
    loading: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    actWithReboot() {
      this.$emit('act', {network: this.filledNetwork, restart: true});
      this.$refs.rebootPopup.close();
    },
    actWithoutReboot() {
      this.$emit('act', {network: this.filledNetwork, restart: false});
      this.$refs.rebootPopup.close();
    },
    openPopup() {
      if (this.filledNetwork.essid == null || this.filledNetwork.essid.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "Essid must be filled", type: MessageType.ERROR})
        return;
      }

      if (this.filledNetwork.password == null || (this.filledNetwork.password.length < 8 && this.network.encryption)) {
        this.$emit(AppEvent.MESSAGE, {content: "Password must have at least 8 characters", type: MessageType.ERROR})
        return;
      }

      this.$refs.rebootPopup.show();
    }
  },
  data() {
    return {
      filledNetwork: {
        essid: this.network.essid,
        password: this.network.password,
        operation: this.network.operation
      }
    }
  }
}
</script>

<template>
  <Popup :title="network.operation === NetworkOperation.AP ? 'Create network' : 'Connect network'" show>
    <template v-slot:content>
      <div class="flex flex-col" :class="network.essid === null ? 'gap-2.5' : 'gap-5'">
        <Input
            v-if="network.essid === null"
            v-model="filledNetwork.essid"
            placeholder="Essid"
        />
        <div v-else class="flex flex-col gap-1">
          <strong class="text-lg font-thin">{{ network.essid }}</strong>
          <div class="flex flex-col gap-1">
            <div class="flex gap-2.5">
              <span class="text-aside">Quality</span>
              <span>{{ getQualityTextual }}</span>
            </div>
            <div class="flex gap-2.5">
              <span class="text-aside">Frequency</span>
              <span>{{ network.frequency }} MHz</span>
            </div>
          </div>
        </div>
        <Input v-model="filledNetwork.password" placeholder="Password" type="password"
               v-if="network.encryption"/>
      </div>
    </template>
    <template v-slot:action>
      <Button text="Cancel" color="var(--pallete-color-black-3)"
              @click="$emit('close')"/>
      <Button
          main-color
          :is-loading="loading"
          :text="network.operation === NetworkOperation.AP ? 'Create network' : 'Connect network'"

          @click="openPopup"
      />
      <RebootPopup @act="(event) => event.reboot ? actWithReboot() : actWithoutReboot()" ref="rebootPopup"/>
    </template>
  </Popup>
</template>
