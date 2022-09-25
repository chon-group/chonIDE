<template>
  <div class="rounded-button-container" :class="direction === 'left' ? 'is-left' : 'is-right'">
    <button class="rounded-button u-total-center" :class="isLoading ? 'is-loading' : ''">
      <Loading v-if="isLoading" :class="'rounded-button__loading'"/>
    </button>
  </div>
</template>

<script>

import Loading from "@/components/Loading";

export default {
  name: "RoundedButton",
  components: {Loading},
  props: {
    direction: String,
    isLoading: Boolean
  },
  mounted() {
    this.$el.parentElement.style.position = "relative";
  }
}
</script>

<style scoped>

.rounded-button-container {
  position: absolute;
  top: 50%;
  filter: drop-shadow(var(--shadow-item-1));
}

.rounded-button {
  border-radius: var(--border-radius-total);
  height: 50px;
  width: 50px;
  border: var(--border-trace);
  background: url('@/assets/media/icon/back-action.svg') no-repeat var(--pallete-color-black-2);
  background-repeat: no-repeat;
  background-position: 45% 49%;
  background-size: 25%;
  cursor: pointer;
}

.rounded-button-container.is-left {
  left: -70px;
  transform: translateY(-50%);
}

.rounded-button-container.is-right {
  right: -70px;
  transform: translateY(-50%);
}

.rounded-button-container.is-right > .rounded-button {
  transform: rotate(180deg)
}

.rounded-button.is-loading {
  background: var(--pallete-color-black-2);
}

.rounded-button:hover {
  background-color: var(--pallete-color-black-3);
}
</style>