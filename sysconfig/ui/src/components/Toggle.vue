<template>
  <div class="toggle">
    <div class="toggle__options u-column">
      <slot name="options"></slot>
    </div>
  </div>
</template>

<script>
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
    }
  },
  data() {
    return {
      isOpen: false,
      triggerElement: null
    }
  },
  mounted() {
    this.triggerElement = this.$el.parentElement;
    document.body.append(this.$el);
    document.body.addEventListener(this.type, (event) => {
      if (this.type == "contextmenu") {
        event.preventDefault();
      }
      if (this.triggerElement.contains(event.target) || event.target == this.triggerElement) {
        if (this.isOpen) {
          this.close();
        } else {
          this.open();
        }
      } else {
        if (this.isOpen) {
          this.close();
        }
      }
    });
    if (this.type == "contextmenu") {
      document.body.addEventListener("click", (event) => {
        if (!this.triggerElement.contains(event.target) && event.target != this.triggerElement) {
          if (this.isOpen) {
            this.close();
          }
        }
      });
    }
  },
  methods: {
    close() {
      this.isOpen = false;
      this.$el.classList.remove("is-open");
    },
    open() {
      this.isOpen = true;
      this.$el.classList.add("is-open");
      this.adjustToRelative();
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
        this.$el.style.top = relativeElementClientRect.top - elementClientRect.height + "px";
      } else {
        this.$el.style.top = relativeElementClientRect.top + relativeElementClientRect.height + "px";
      }
    }
  }
}
</script>

<style scoped>

.toggle {
  display: none;
  position: absolute;
  border-radius: var(--border-radius-container);
  padding: var(--ratio-4) 0;
  min-width: 125px;
  background-color: var(--pallete-color-black-2);
  font-size: var(--font-size);
  border: 1px solid var(--pallete-color-black-3);
}

.toggle.is-open {
  display: flex;
}

.toggle__options {
  width: 100%;
}

:slotted(span) {
  color: var(--pallete-text-aside);
  padding: var(--ratio-4) var(--ratio-3);
}

:slotted(button) {
  border: none;
  background-color: transparent;
  color: var(--pallete-text-main);
  padding: var(--ratio-4) var(--ratio-3);
  text-align: left;
}

:slotted(button.severe) {
  color: var(--pallete-color-red-1);
}

:slotted(hr) {
  width: 100%;
  height: 1px;
  border: 0;
  background-color: var(--pallete-color-black-4);
  margin: var(--ratio-4) 0;
}

:slotted(button):hover {
  background-color: var(--pallete-color-black-3);
  width: 100%;
}

</style>