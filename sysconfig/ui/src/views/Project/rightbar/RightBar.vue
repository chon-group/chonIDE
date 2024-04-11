<script>
import Boards from "@/views/Project/rightbar/boards/Boards.vue";
import {FileType} from "@/domain/Enums";
import Dragger from "@/components/Dragger.vue";

export default {
    name: "RightBar",
    computed: {
        FileType() {
            return FileType
        }
    },
    components: {Dragger, Boards},
    props: {
        currentFileType: {}
    }
}
</script>

<template>
    <div class="right-bar" v-if="currentFileType === FileType.FIRMWARE.valueOf()">
        <Boards @selectedBoard="$emit('selectedBoard', $event)"/>
        <Dragger left/>
    </div>
</template>

<style scoped>

.right-bar {
    max-width: 50vw;
    min-width: 300px;
    @apply flex flex-col h-full relative;
}

</style>
