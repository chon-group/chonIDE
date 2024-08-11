<script>
import Boards from "@/views/Project/rightbar/boards/Boards.vue";
import {FileType} from "@/utils/enums";
import Dragger from "@/components/general/Dragger.vue";
import MindInspector from "@/views/Project/rightbar/mindinspector/MindInspector.vue";

export default {
  name: "RightBar",
  computed: {
    show() {
      return this.showBoards || this.showMindInspector;
    },
    showBoards() {
      return this.currentFileType === FileType.FIRMWARE.valueOf();
    },
    showMindInspector() {
      return this.smaRunning && this.configuration.mindInspector === 2 && this.currentFileType === FileType.AGENT.valueOf();
    }
  },
  components: {MindInspector: MindInspector, Dragger, Boards},
  props: {
    currentFileType: {},
    domain: {},
    currentBoard: {},
    configuration: {},
    smaRunning: Boolean
  }
}
</script>

<template>
  <div v-show="show" class="right-bar">
    <MindInspector v-if="showMindInspector" :domain="domain" @highlightAgentFile="$emit('highlightAgentFile', $event)"/>
    <Boards v-show="showBoards" @selectBoard="$emit('selectBoard', $event)" :current-board="currentBoard"/>
    <Dragger left/>
  </div>
</template>

<style scoped>

.right-bar {
  min-width: 300px;
  border-left: 1px solid var(--pallete-color-black-4);
  z-index: 5;
  @apply flex flex-col h-full relative;
}

</style>
