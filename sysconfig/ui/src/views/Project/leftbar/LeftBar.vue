<script>
import Dragger from "@/components/general/Dragger.vue";
import Explorer from "@/views/Project/leftbar/explorer/Explorer.vue";

export default {
  name: "LeftBar",
  components: {Explorer, Dragger},
  props: {
    configuration: {},
    project: {},
    currentFile: {}
  }
}
</script>

<template>
  <div class="left-bar">
    <div class="project__header-bar">
      <span class="project__header-bar__title">Explorer</span>
    </div>
    <Explorer
        :configuration="configuration"
        :current-file="currentFile"
        :project="project"

        @addAgent="$emit('addAgent', $event)"
        @addFirmware="$emit('addFirmware', $event)"
        @removeAgent="$emit('removeAgent', $event)"
        @removeFirmware="$emit('removeFirmware', $event)"
        @setCurrentFile="$emit('setCurrentFile', $event)"
        @setFileType="$emit('setFileType', $event)"
        @message="$emit('message', $event)"
    />
    <Dragger right/>
  </div>
</template>

<style scoped>

.left-bar {
  background-color: var(--pallete-color-black-1);
  border-right: 1px solid var(--pallete-color-black-4);
  width: 300px;
  @apply flex flex-col h-full relative;
}

</style>