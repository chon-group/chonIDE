<template>
  <div class="board" @click="select">
    <div class="flex justify-between">
      <span class="flex flex-col">
        <span>{{ board.board }}</span>
        <span class="text-aside">{{ board.fqbn }}</span>
      </span>
      <div class="coder__board__select" :class="isCurrent ? 'is-selected' : ''"></div>
    </div>
    <span class="text-aside">{{ board.port }}</span>
  </div>
</template>

<script>
import {removeRipple, useRipple} from "@/composable/Ripple";

export default {
  name: "Board",
  props: {
    board: {},
    isCurrent: Boolean
  },
  mounted() {
    useRipple(this.$el);
  },
  beforeUnmount() {
    removeRipple(this.$el);
  },
  methods: {
    select() {
      this.$emit("select");
    }
  }
}
</script>

<style scoped>
@import "@/views/Project/style.css";

.board {
  height: 80px;
  @apply flex flex-col justify-between p-1.5 rounded-lg cursor-pointer;
}

.board:hover {
  background-color: var(--pallete-color-black-3);
}

.coder__board__select {
  height: 15px;
  aspect-ratio: 1/1;
  border: 2px solid var(--pallete-color-black-4);
  @apply rounded-full;
}

.coder__board__select.is-selected {
  background-color: var(--pallete-color-main-1);
}

</style>