<script>
import Boards from "@/views/Project/rightbar/boards/Boards.vue";
import {FileType} from "@/domain/Enums";
import Dragger from "@/components/Dragger.vue";
import MindInpector from "@/views/Project/rightbar/mindinspector/MindInspector.vue";

export default {
    name: "RightBar",
    computed: {
        FileType() {
            return FileType
        },
        show() {
            return this.showBoards || this.showMindInspector;
        },
        showBoards() {
            return this.currentFileType === FileType.FIRMWARE.valueOf();
        },
        showMindInspector() {
            return this.smaRunning && this.currentFileType === FileType.AGENT.valueOf();
        }
    },
    components: {MindInpector, Dragger, Boards},
    props: {
        currentFileType: {},
        domain: {},
        smaRunning: Boolean
    }
}
</script>

<template>
    <div class="right-bar" v-if="show">
        <MindInpector v-if="showMindInspector" :domain="domain" @highlightAgentFile="$emit('highlightAgentFile', $event)"/>
        <Boards @selectedBoard="$emit('selectedBoard', $event)" v-else-if="showBoards"/>
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
