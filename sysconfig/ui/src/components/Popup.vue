<template>
  <div style='display: none' class="pop-up-background flex items-center justify-center">
    <div class="c-base-container pop-up flex flex-col gap-5">
      <div class="flex items-center justify-between">
        <span class="pop-up__title">{{ title }}</span>
        <div class="pop-up__close-action" @click="close" v-if="canClose"></div>
      </div>
      <slot name="content"></slot>
      <div class="flex gap-2.5 justify-end">
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
    isChildren: Boolean
  },
  data() {
    return {
      triggerElement: null
    }
  },
  methods: {
    close() {
      this.showing(false);
      if(this.triggerElement != null && this.for != null) {
        this.triggerElement.appendChild(this.$el);
      }
    },
    showing(isShowing) {
      if (isShowing) {
        this.$el.removeAttribute("style");
      } else {
        this.$el.style.display = "none";
      }
    }
  },
  mounted() {
    if(this.isChildren) {
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
  position: absolute;
  z-index: 5;
  overflow-y: hidden;
}

.pop-up {
  width: var(--container-width-2);
  z-index: 10;
  font-size: var(--text-size-normal);
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
  @apply cursor-pointer rounded-full;
}

.pop-up__close-action:hover {
  background-color: var(--pallete-color-black-3);
}

</style>