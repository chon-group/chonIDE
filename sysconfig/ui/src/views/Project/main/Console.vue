<script>
import Button from "@/components/general/Button.vue";
import Dragger from "@/components/general/Dragger.vue";

const DEFAULT_LINKS_PROTOCOL = "https://";
const SMA_PORT = ":3371";

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
    closedHeight() {
      return "41px";
    },
    openHeight() {
      return "350px";
    }
  },
  methods: {
    show() {
      if (!this.showing) {
        this.$refs.showButton.$el.style.transform = "rotate(0deg)";
        this.$el.style.height = this.openHeight;
        this.showing = true;
      } else {
        this.$refs.showButton.$el.style.transform = "rotate(180deg)";
        this.$el.style.height = this.closedHeight;
        this.showing = false;
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
  <div v-show="domain != null" class="console flex flex-col">
    <div class="project__header-bar">
      <span class="project__header-bar__title">Agent Tracer</span>
      <div v-if="domain != null" class="flex gap-2.6">
        <Button icon="refresh.svg" icon-ratio="13px" @click="reload"/>
        <Button
            ref="showButton"
            class="console__show-button"

            icon="arrow.svg"

            icon-ratio="12px" @click="show"
        />
        <Button :link="logsUrl" icon="tab.svg" icon-ratio="11px"/>
      </div>
    </div>
    <iframe ref="iframe" :src="logsUrl" frameborder="0"></iframe>
    <Dragger top/>
  </div>
</template>

<style>

.console {
  border-top: 1px solid var(--pallete-color-black-4);
  min-height: v-bind(closedHeight);
  height: v-bind(openHeight);
  @apply relative select-none;
}

.console > iframe {
  height: 100%;
  @apply ml-3;
}

</style>