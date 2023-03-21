<template>
  <div class="coder__explorer__folder flex flex-col">
    <div class="coder__explorer__folder-name coder__explorer__item flex items-center">
      <div class="h-full w-full flex items-center" @click="folderOpen = !folderOpen">
        <img src="@/assets/media/icon/toggle.svg" :class="`coder__explorer__item__toggle
             ${folderOpen ? 'open' : ''}`">
        <span>{{ name }}</span>
      </div>
      <Toggle click-position type="contextmenu" v-if="hasAdd || hasRefresh">
        <template v-slot:options>
          <button v-if="hasAdd" @click="addFile">{{ addMessage }}</button>
          <button v-if="hasRefresh" @click="refresh">Atualizar</button>
        </template>
      </Toggle>
    </div>
    <div class="coder__explorer__folder__content" v-show="folderOpen">
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script>
import Toggle from "@/components/Toggle";
export default {
  name: "ExplorerFolder",
  components: {Toggle},
  props: {
    name: String,
    hasAdd: {
      default: true
    },
    hasRefresh: Boolean,
    addMessage: {
      default: "Novo arquivo"
    }
  },
  data() {
    return {
      folderOpen: true,
      level: 1
    }
  },
  mounted() {
    let currentParent = this.$parent;
    while (currentParent.$options.name == "ExplorerFolder") {
      currentParent = currentParent.$parent;
      this.level++;
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
    }
  }
}
</script>

<style scoped>
@import "@/views/Project/style.css";

.coder__explorer__folder-name {
  @apply pl-2.5;
}

:slotted(.coder__explorer__file) {
  padding-left: v-bind(filesPaddingLeft);
}

:slotted(.coder__explorer__folder) > .coder__explorer__folder-name{
  padding-left: v-bind(folderNamePaddingLeft);
}

.coder__explorer__item__toggle {
  height: 8px;
  width: 8px;
  transform: rotate(-90deg);
  @apply mr-2.5;
}

.coder__explorer__item__toggle.open {
  transform: rotate(0deg);
}

</style>