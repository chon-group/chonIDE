<template>
  <div class="toggle">
    <div class="w-full flex flex-col">
      <slot name="options"></slot>
    </div>
  </div>
</template>

<script>

const DISTANCE_TO_PARENT_ELEMENT = 5;

import checkImage from '@/assets/media/icon/check.svg';

export default {
  name: "Toggle",
  props: {
    type: {
      type: String,
      default: "click",
      required: false
    },
    direction: {
      type: String,
      default: null,
      required: false
    },
    select: Boolean,
    selected: String,
    clickPosition: Boolean,
    parentPosition: Boolean
  },
  data() {
    return {
      isOpen: false,
      triggerElement: null,
      buttons: [],
      check: null
    }
  },
  watch: {
    selected(selected) {
      this.buttons.forEach((button) => {
        if (selected == button.textContent) {
          button.append(this.check);
        }
      });
    }
  },
  mounted() {
    this.applyClickEvent();

    if (this.select) {
      setTimeout(() => {
        this.buttons = this.$el.querySelectorAll("button");
        this.check = this.createCheckImage();
        this.buttons[0].append(this.check);
        this.buttons.forEach((button) => {
          if (this.selected == button.textContent) {
            button.append(this.check);
          }
          button.addEventListener("click", () => {
            this.$emit("select", button.textContent);
            button.append(this.check);
          });
        });
      }, 50);
    }
  },
  methods: {
    createCheckImage() {
      let check = document.createElement("img");
      check.src = checkImage;
      check.style.height = "12px"
      check.style.width = "12px";
      return check;
    },
    applyClickEvent() {
      this.triggerElement = this.$el.parentElement;
      document.body.addEventListener(this.type, (event) => {
        if (this.type == "contextmenu") {
          event.preventDefault();
        }
        if (this.triggerElement.contains(event.target) || event.target == this.triggerElement) {
          if (this.isOpen) {
            this.close();
          } else {
            this.open(event);
          }
        } else {
          if (this.isOpen) {
            this.close();
          }
        }
      });
      if (this.type == "contextmenu") {
        document.body.addEventListener("click", () => {
          if (this.isOpen) {
            this.close();
          }
        });
      }
    },
    close() {
      this.triggerElement.append(this.$el);
      this.isOpen = false;
      this.$el.classList.remove("is-open");
    },
    open(event) {
      document.body.append(this.$el);
      this.isOpen = true;
      this.$el.classList.add("is-open");
      if (this.parentPosition) {
        this.adjustToRelative();
      } else if (this.clickPosition) {
        this.adjustToEvent(event);
      }
      this.adjustToWindow();
    },
    adjustToWindow() {
      let elementClientRect = this.$el.getBoundingClientRect();
      if (elementClientRect.right == window.innerWidth) {
        this.$el.style.left = elementClientRect.left - DISTANCE_TO_PARENT_ELEMENT + "px";
      }
    },
    adjustToEvent(event) {
      this.$el.style.left = event.pageX + "px";
      this.$el.style.top = event.pageY + "px";
    },
    adjustToRelative() {
      let relativeElementClientRect = this.triggerElement.getBoundingClientRect();
      let elementClientRect = this.$el.getBoundingClientRect();

      if (this.direction == null) {
        // Ajustando a direção horizontal do menu.
        let midWidthWindow = (window.innerWidth / 2);
        if (midWidthWindow > relativeElementClientRect.left) {
          this.$el.style.left = relativeElementClientRect.left + "px";
        } else {
          let rightDifferenceBetweenRelativeAndElement = Math.abs(elementClientRect.width - relativeElementClientRect.width);
          this.$el.style.left = relativeElementClientRect.left - rightDifferenceBetweenRelativeAndElement + "px";
        }
      } else if (this.direction == "right") {
        this.$el.style.left = relativeElementClientRect.right - elementClientRect.width + "px";
      }

      // Ajustando a direção vertical do menu.
      if (relativeElementClientRect.top + relativeElementClientRect.height + elementClientRect.height >=
          window.innerHeight) {
        this.$el.style.top = relativeElementClientRect.top - DISTANCE_TO_PARENT_ELEMENT
            - elementClientRect.height + "px";
      } else {
        this.$el.style.top = relativeElementClientRect.top + relativeElementClientRect.height + DISTANCE_TO_PARENT_ELEMENT + "px";
      }
    }
  }
}
</script>

<style scoped>

.toggle {
  display: none;
  min-width: 125px;
  background-color: var(--pallete-color-black-2);
  font-size: var(--text-size-normal);
  border: 1px solid var(--pallete-color-black-3);
  @apply absolute rounded-md py-1.5 z-10;
}

.toggle.is-open {
  @apply flex;
}

:slotted(span) {
  color: var(--pallete-text-aside);
  @apply px-2.5 py-1.5 cursor-default select-none;
}

:slotted(button) {
  color: var(--pallete-text-main);
  @apply flex justify-between items-center gap-2.5 bg-transparent px-2.5 py-1.5 border-none;
}

:slotted(button.severe) {
  color: var(--pallete-color-red-1);
}

:slotted(hr) {
  background-color: var(--pallete-color-black-4);
  @apply w-full border-none mx-1.5;
}

:slotted(button):hover {
  background-color: var(--pallete-color-black-3);
  @apply w-full;
}

</style>