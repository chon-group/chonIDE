<script>
import Button from "@/components/Button.vue";
import Dragger from "@/components/Dragger.vue";

const DEFAULT_LINKS_PROTOCOL = "http://";
const SMA_PORT = ":3271";

export default {
    name: "Console",
    components: {Dragger, Button},
    props: {
        domain: {}
    },
    data() {
        return {
            showing: true
        }
    },
    computed: {
        logsUrl() {
            if (this.domain == null) {
                return "";
            }
            return DEFAULT_LINKS_PROTOCOL + this.domain.domain + SMA_PORT;
        },
    },
    methods: {
        show() {
            if (this.showing) {
                this.$el.style.height = "fit-content";
                this.showing = false;
            } else {
                this.$el.removeAttribute("style");
                this.showing = true;
            }
        },
        reload() {
            const iframeSrc = this.$refs.iframe.src;
            this.$refs.iframe.src = iframeSrc;
        }
    }
}
</script>

<template>
    <div class="console flex flex-col">
        <div class="project__header-bar">
            <span class="project__header-bar__title">Agent Tracer</span>
            <div class="flex gap-2.6" v-if="domain != null">
                <Button icon="refresh.svg" icon-ratio="13px" side-padding="12px" @click="reload"/>
                <Button
                        icon="terminal.svg"
                        icon-ratio="16px"

                        @click="show"

                        class="console__show-button"
                />
                <Button :link="logsUrl" text="Tab"/>
            </div>
        </div>
        <iframe v-show="domain != null && showing" :src="logsUrl" frameborder="0" ref="iframe"></iframe>
        <Dragger top :min-height="42"/>
    </div>
</template>

<style>

.console {
    border: 1px solid var(--pallete-color-black-1);
    border-right: 0;
    height: 350px;
}

.console > iframe {
    height: 100%;
}

</style>