<template>
  <div :style="type !== 'now' ? {display: 'none'}: ''">
    <div class="pop-up-background u-total-center">
      <div class="c-base-container pop-up u-column u-gap-2">
        <div class="u-row u-align-i-center u-justify-i-between">
          <span class="pop-up__title">{{ title }}</span>
          <div class="pop-up__close-action" @click="close" v-if="canClose"></div>
        </div>
        <slot name="content"></slot>
        <div class="u-row u-gap-3 u-justify-i-end">
          <slot name="action"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "Popup",
  props: {
    title: String,
    type: String,
    for: String,
    canClose: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      triggerElement: null
    }
  },
  methods: {
    close() {
      this.$el.style.display = "none";
      try {
        this.triggerElement.appendChild(this.$el);
      } catch (e) {
        this.$el.remove();
      }
    }
  },
  mounted() {
    this.triggerElement = document.querySelector(`.${this.for}`);
    if (this.triggerElement == null) {
      return;
    }
    this.triggerElement.appendChild(this.$el);
    this.triggerElement.onclick = () => {
      this.$el.style.display = "block";
      document.body.appendChild(this.$el);
    }
  }
}

</script>

<style scoped>

.pop-up-background {
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(3px);
  position: absolute;
  z-index: 5;
  overflow-y: hidden;
}

.pop-up {
  width: var(--container-width-2);
  z-index: 10;
}

.pop-up__title {
  font-size: var(--text-size-title);
  color: var(--pallete-text-main);
}

.pop-up__close-action {
  width: var(--action-ratio-1);
  height: var(--action-ratio-1);
  background: url("@/assets/media/icon/close-action.svg") center no-repeat;
  background-size: 40%;
  cursor: pointer;
  border-radius: var(--border-radius-item);
}

.pop-up__close-action:hover {
  background-color: var(--pallete-color-black-3);
}

</style>