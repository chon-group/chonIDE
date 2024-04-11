<script>
import Dragger from "@/components/Dragger.vue";
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
    <div class="project__left-bar">
        <div class="project__header-bar">
            <span class="project__header-bar__title">Explorer</span>
        </div>
        <Explorer
                :configuration="configuration"
                :project="project"
                :current-file="currentFile"

                @addFirmware="$emit('addFirmware', $event)"
                @removeFirmware="$emit('removeFirmware', $event)"
                @addAgent="$emit('addAgent', $event)"
                @removeAgent="$emit('removeAgent', $event)"
                @setCurrentFile="$emit('setCurrentFile', $event)"
                @setFileType="$emit('setFileType', $event)"
        />
        <Dragger right/>
    </div>
</template>

<style scoped>

.project__left-bar {
    min-width: 300px;
    max-width: 50vw;
    @apply flex flex-col h-full relative;
}

</style>