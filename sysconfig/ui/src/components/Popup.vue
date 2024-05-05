<template>
  <div class="pop-up-background" style='display: none'>
    <div ref="popup" class="pop-up flex flex-col gap-5">
      <div class="flex items-center justify-between">
        <span class="pop-up__title">{{ title }}</span>
        <div v-if="canClose" class="pop-up__close-action" @click="close"></div>
      </div>
      <slot name="content"></slot>
      <div v-if="$slots.action != null" class="flex gap-1.5 justify-end">
        <slot name="action"></slot>
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
    canClose: Boolean,
    isChildren: Boolean,
    width: {
      type: String,
      default: "var(--container-width-2)"
    }
  },
  data() {
    return {
      triggerElement: null
    }
  },
  methods: {
    close() {
      this.showing(false);
      if (this.triggerElement != null && this.for != null) {
        this.triggerElement.appendChild(this.$el);
      }
    },
    showing(isShowing) {
      if (isShowing) {
        this.$el.style.removeProperty("display");
        document.body.style.overflow = "hidden";
      } else {
        this.$el.style.display = "none";
        document.body.style.removeProperty("overflow");
      }
    }
  },
  mounted() {
    if (this.isChildren) {
      this.triggerElement = this.$el.parentElement;
    } else {
      this.triggerElement = document.querySelector(`.${this.for}`);
    }
    if (this.triggerElement == null) {
      return;
    }
    this.triggerElement.appendChild(this.$el);
    this.triggerElement.onclick = () => {
      this.showing(true);
      document.body.appendChild(this.$el);
    }
    this.$el.parentElement.querySelectorAll("[role='pop-up-closer']").forEach((closer) => {
      closer.addEventListener("click", () => {
        this.close();
      });
    });
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
  position: fixed;
  z-index: 5;
  overflow-y: hidden;
  @apply flex items-center justify-center;
}

.pop-up {
  width: v-bind(width);
  z-index: 10;
  background-color: var(--pallete-color-black-2);
  @apply m-auto p-5 rounded-md;
}

.pop-up__title {
  font-size: var(--text-size-headline);
  color: var(--pallete-text-main);
}

.pop-up__close-action {
  width: var(--action-height);
  height: var(--action-height);
  background: url("@/assets/media/icon/close-action.svg") center no-repeat;
  background-size: 40%;
  @apply cursor-pointer rounded-lg;
}

.pop-up__close-action:hover {
  background-color: var(--pallete-color-black-3);
}

</style>