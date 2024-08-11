<script>

import {removeRipple, useRipple} from "@/assets/js/effects/ripple";

export default {
  name: "Network",
  props: {
    network: {}
  },
  data() {
    return {
      password: this.network.password
    }
  },
  mounted() {
    useRipple(this.$el);
  },
  beforeUnmount() {
    removeRipple(this.$el);
  },
  computed: {
    getNetworkQualityIcon() {
      if (this.network.quality < 0.15) {
        return require("@/assets/media/icon/wifi-quality-1.svg");
      } else if (this.network.quality < 0.30) {
        return require("@/assets/media/icon/wifi-quality-2.svg");
      } else if (this.network.quality < 0.65) {
        return require("@/assets/media/icon/wifi-quality-3.svg");
      } else {
        return require("@/assets/media/icon/wifi-quality-4.svg");
      }
    }
  }
}
</script>

<template>
  <div class="network">
    <div class="flex gap-4 items-center">
      <div class="relative">
        <img
            class="network__quality-indicator"
            :src="getNetworkQualityIcon"
            alt="wifi-quality-icon"
        />

        <img
            v-if="network.encryption"
            class="network__lock"
            src="../../../assets/media/icon/lock.svg"
            alt="lock-icon"
        />
      </div>
      <span>{{ network.essid }}</span>
    </div>

    <span v-if="network.connected" class="network__connected">Connected</span>

    <span class="text-aside" v-else-if="network.password != null">Saved</span>
  </div>
</template>

<style scoped>

.network {
  background-color: var(--pallete-color-black-3);
  @apply flex justify-between items-center cursor-pointer p-4 rounded-md w-full shrink-0 select-none;
}

.network:hover {
  background: var(--pallete-color-black-4);
}

.network__quality-indicator {
  width: 33px;
  aspect-ratio: 1/1;
  transform: scaleY(1.04);
}

.network__lock {
  right: -1px;
  bottom: 4px;
  position: absolute;
  height: 15px;
  aspect-ratio: 1/1;
}

.network__connected {
  color: var(--pallete-color-green-2);
}

</style>