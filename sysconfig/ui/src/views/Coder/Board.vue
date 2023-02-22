<template>
  <div class="coder__board u-column u-justify-i-between" @click="select">
    <div class="u-row u-justify-i-between">
            <span class="u-column">
              <span>{{ board.board }}</span>
              <span class="is-aside">{{ board.fqbn }}</span>
            </span>
      <div class="coder__board__select" :class="isCurrent ? 'is-selected' : ''"></div>
    </div>
    <span class="is-aside">{{ board.port }}</span>
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
@import "@/views/Coder/style.css";

.coder__board {
  height: 75px;
  padding: var(--ratio-3);
}

.coder__board:hover {
  background-color: var(--pallete-color-black-3);
}

.coder__board__select {
  height: 15px;
  aspect-ratio: 1/1;
  border-radius: var(--border-radius-total);
  border: 2px solid var(--pallete-color-black-4);
}

.coder__board__select.is-selected {
  background-color: var(--pallete-color-main-1);
}

</style>