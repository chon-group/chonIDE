<template>
  <div class="coder__explorer__folder flex flex-col">
    <div class="coder__explorer__folder-name coder__explorer__item flex items-center pr-1">
      <div class="h-full w-full flex justify-between items-center" @click="folderOpen = !folderOpen">
        <div class="flex gap-2.5 items-center w-full">
          <img src="../../../../assets/media/icon/toggle.svg" :class="`coder__explorer__item__toggle
             ${folderOpen ? 'open' : ''}`">
          <img class="coder__explorer__folder__icon" v-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)"/>
          <span>{{ name }}</span>
        </div>
      </div>
      <button class="coder__explorer__action-button add" ref="dotsButton" v-if="hasAdd" @click="addFile"></button>
      <button class="coder__explorer__action-button download" v-if="hasDownload" @click="download"></button>
      <button class="coder__explorer__action-button refresh" v-if="hasRefresh" @click="refresh"></button>
      <Toggle click-position type="contextmenu" v-if="hasAdd || hasRefresh">
        <template v-slot:options>
          <button v-if="hasAdd" @click="addFile">{{ addMessage }}</button>
          <button v-if="hasRefresh" @click="refresh">Refresh</button>
        </template>
      </Toggle>
    </div>
    <div class="coder__explorer__folder__content" v-show="folderOpen">
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script>
import Toggle from "@/components/Toggle.vue";
import {removeRipple, useRipple} from "@/assets/js/effects/ripple";

export default {
  name: "ExplorerFolder",
  components: {Toggle},
  props: {
    name: String,
    hasAdd: {
      default: true
    },
    icon: String,
    iconRatio: {
      default: "12px",
      type: String
    },
    hasRefresh: Boolean,
    addMessage: {
      default: "Novo arquivo"
    },
    hasDownload: Boolean
  },
  data() {
    return {
      folderOpen: true,
      level: 1
    }
  },
  mounted() {
    if(this.$refs.dotsButton != null) {
      useRipple(this.$refs.dotsButton);
    }
    let currentParent = this.$parent;
    while (currentParent.$options.name === "ExplorerFolder") {
      currentParent = currentParent.$parent;
      this.level++;
    }
  },
  beforeUnmount() {
    if(this.$refs.dotsButton != null) {
      removeRipple(this.$refs.dotsButton)
    }
  },
  computed: {
    folderNamePaddingLeft() {
      return this.level * 12 + "px";
    },
    filesPaddingLeft() {
      return this.level * 12 + 20 + "px";
    }
  },
  watch: {
    folderOpen(state) {
      this.$emit("opened", state);
    }
  },
  methods: {
    addFile() {
      this.$emit("add");
    },
    refresh() {
      this.$emit("refresh");
    },
      download() {
        this.$emit("download");
      }
  }
}
</script>

<style scoped>
@import "@/views/Project/leftbar/explorer/style.css";

.coder__explorer__folder-name {
  @apply pl-2.5;
}

.coder__explorer__folder__icon {
  width: v-bind(iconRatio);
  aspect-ratio: 1/1;
}

.coder__explorer__action-button.add {
  background-image: url("@/assets/media/icon/add.svg");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 44%;
}

.coder__explorer__action-button.download {
    background-image: url("@/assets/media/icon/download.svg");
    background-repeat: no-repeat;
    background-position: center;
    background-size: 42%;
}

.coder__explorer__action-button.refresh {
  background-image: url("@/assets/media/icon/refresh.svg");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 42%;
}

.coder__explorer__action-button.add:hover, .coder__explorer__action-button.download:hover,
.coder__explorer__action-button.refresh:hover {
  background-color: var(--pallete-color-black-4);
}

:slotted(.coder__explorer__file) {
  padding-left: calc(v-bind(filesPaddingLeft) + 10px);
}

:slotted(.coder__explorer__folder) > .coder__explorer__folder-name {
  padding-left: v-bind(folderNamePaddingLeft);
}

.coder__explorer__item__toggle {
  height: 8px;
  width: 8px;
  transform: rotate(-90deg);
}

.coder__explorer__item__toggle.open {
  transform: rotate(0deg);
}

</style>