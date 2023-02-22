<template>
  <div class="coder__explorer__folder u-column">
    <div class="coder__explorer__folder-name coder__explorer__item u-row u-align-i-center">
      <div class="u-height-cover u-width-cover u-row u-align-i-center" @click="folderOpen = !folderOpen">
        <img src="@/assets/media/icon/toggle.svg" :class="`coder__explorer__item__toggle
             ${folderOpen ? 'open' : ''}`">
        <span>{{ name }}</span>
      </div>
      <Toggle click-position type="contextmenu">
        <template v-slot:options>
          <button v-if="hasAdd != null" @click="addFile">Criar arquivo</button>
          <button v-if="hasRefresh != null" @click="refresh">Atualizar</button>
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
    hasRefresh: String
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
@import "@/views/Coder/style.css";

.coder__explorer__folder-name {
  padding-left: var(--ratio-3);
}

:slotted(.coder__explorer__file) {
  padding-left: v-bind(filesPaddingLeft);
}

:slotted(.coder__explorer__folder) > .coder__explorer__folder-name{
  padding-left: v-bind(folderNamePaddingLeft);
}

.coder__explorer__item__toggle {
  height: 5px;
  margin-right: var(--ratio-3);
  transform: rotate(-90deg);
}

.coder__explorer__item__toggle.open {
  transform: rotate(0deg);
}


</style>