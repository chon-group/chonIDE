<script>
import Button from "@/components/Button.vue";
import axios from "axios";
import Agent from "@/views/Project/rightbar/mindinspector/Agent.vue";

const DEFAULT_LINKS_PROTOCOL = "http://";
const PORT = ":3273";

export default {
    name: "MindInpector",
    components: {Agent, Button},
    props: {
        domain: {}
    },
    data() {
        return {
            interval: 0,
            agents: null
        }
    },
    computed: {
        url() {
            if (this.domain == null) {
                return null;
            }
            return DEFAULT_LINKS_PROTOCOL + this.domain.domain + PORT + "/api/agents";
        }
    },
    beforeUnmount() {
        clearInterval(this.interval);
    },
    mounted() {
        this.interval = setInterval(() => {
            if (this.url != null) {
                axios.get(this.url).then((response) => {
                   if (response.status === 200) {
                       this.agents = response.data;
                   }
                }).catch((error) => {
                    this.agents = null;
                });
            }
        }, 1000);
    }
}
</script>

<template>
    <div class="flex flex-col h-full w-full">
        <div class="project__header-bar">
            <span class="project__header-bar__title">Mind Inspector</span>
            <Button icon="refresh.svg" icon-ratio="13px" side-padding="12px"/>
        </div>
        <div v-if="agents != null" class="mindinspector__agents">
            <Agent v-for="(agent, index) in agents" :agent="agent" :key="index"/>
        </div>
        <div v-else class="flex justify-center items-center h-full w-full">
            <span class="text-aside">Collecting data...</span>
        </div>
    </div>
</template>

<style scoped>

.mindinspector__agents {
    flex-basis: 0;
    @apply flex flex-col flex-grow overflow-y-auto;
}

.mindinspector__agents:last-child {
    @apply mb-2
}


</style>