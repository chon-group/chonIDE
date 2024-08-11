<template>
  <div class="board" @click="$emit('select')">
    <div class="flex justify-between">
      <span class="flex flex-col">
        <span>{{ board.board }}</span>
        <span class="text-aside">{{ board.fqbn }}</span>
      </span>
      <div :class="isCurrent ? 'is-selected' : ''" class="coder__board__select"></div>
    </div>
    <span class="text-aside">{{ board.port }}</span>
  </div>
</template>

<script>
import {removeRipple, useRipple} from "@/assets/js/effects/ripple";

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
  }
}
</script>

<style scoped>
@import "@/views/Project/style.css";

.board {
  @apply flex flex-col justify-between p-3 cursor-pointer;
}

.board:hover {
  background-color: var(--pallete-color-black-3);
}

.coder__board__select {
  height: 14px;
  aspect-ratio: 1/1;
  background-color: var(--pallete-color-black-3);
  @apply rounded-full;
}

.coder__board__select.is-selected {
  background-color: var(--pallete-color-main-1);
}

</style>