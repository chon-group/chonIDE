<script>
import axios from "axios";
import Agent from "@/views/Project/rightbar/mindinspector/Agent.vue";
import {EndPoints} from "@/services/chonide/endPoints";
import {Api} from "@/services/chonide/api";

export default {
  name: "MindInspector",
  components: {Agent},
  props: {
    domain: {}
  },
  data() {
    return {
      interval: 0,
      agents: null,
      isUnavailable: false
    }
  },
  beforeUnmount() {
    clearInterval(this.interval);
  },
  mounted() {
    this.interval = setInterval(() => {
      Api.get(EndPoints.MINDINSPECTOR).then((response) => {
        if (response.status === 200) {
          this.agents = response.data.data;
        }
      }).catch((error) => {
        this.isUnavailable = error.response.status === 503;
        this.agents = null;
      });
    }, 1000);
  }
}
</script>

<template>
  <div class="mindinspector">
    <div class="project__header-bar">
      <span class="project__header-bar__title">Mind Inspector</span>
    </div>
    <div v-if="isUnavailable" class="flex justify-center items-center h-full w-full">
      <span class="text-aside">MindInspector is unavailable</span>
    </div>
    <div v-else-if="agents != null" class="mindinspector__agents">
      <Agent
          v-for="(agent, index) in agents"

          :key="index"
          :agentData="agent"

          @highlightAgentFile="$emit('highlightAgentFile', $event)"
      />
    </div>
    <div v-else class="flex justify-center items-center h-full w-full">
      <span class="text-aside">Collecting data...</span>
    </div>
  </div>
</template>

<style scoped>

.mindinspector {
  @apply flex flex-col h-full w-full;
}

.mindinspector__agents {
  flex-basis: 0;
  @apply flex flex-col flex-grow overflow-y-auto select-none;
}

.mindinspector__agents:last-child {
  @apply mb-2
}


</style>