<template>
  <div class="coder__board flex flex-col justify-between" @click="select">
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
export default {
  name: "Board",
  props: {
    board: {},
    isCurrent: Boolean
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

.coder__board {
  height: 75px;
  @apply p-2.5;
}

.coder__board:hover {
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