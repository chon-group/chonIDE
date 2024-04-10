<template>
  <div class="dragger"></div>
</template>

<script>
export default {
  name: "Dragger",
  props: {
    top: Boolean,
    right: Boolean,
    left: Boolean,
    bottom: Boolean,
    maxHeight: {
      default: 99999
    },
    minHeight: {
      default: 0
    },
    minWidth: {
      default: 0
    },
    maxWidth: {
      default: 99999
    }
  },
  data() {
    return {
      isHorizontal: this.right || this.left,
      isVertical: this.top || this.bottom
    }
  },
  computed: {
    height() {
      if (this.isHorizontal) {
        return "100%";
      } else {
        return "6px";
      }
    },
    width() {
      if (this.isHorizontal) {
        return "6px";
      } else {
        return "100%";
      }
    },
    inset() {
      if (this.top) {
        return "0 auto auto auto";
      } else if (this.right) {
        return "auto 0 auto auto";
      } else if (this.left){
        return "auto auto auto 0";
      } else if (this.bottom) {
        return "auto auto auto 0";
      } else {
        return "";
      }
    },
    transform() {
      if (this.isVertical) {
        return "translateY(-50%)";
      } else {
        return "translateX(50%)";
      }
    },
    cursor() {
      if (this.isVertical) {
        return "row-resize";
      } else {
        return "col-resize"
      }
    }
  },
  mounted() {
    let parentElement = this.$el.parentElement;

    parentElement.style.position = "relative";

    let img = new Image();
    this.$el.ondragstart = (event) => {
      event.dataTransfer.setDragImage(img, 0, 0);
    }

    const stopResize = () => {
      window.removeEventListener('mousemove', resize, false);
      window.removeEventListener('mouseup', stopResize, false);
    }

    const resize = (event) => {
      if (this.isVertical) {
        let height = Math.abs(window.innerHeight - event.pageY);
        if (height < this.maxHeight && height > this.minHeight) {
          parentElement.style.height = height + "px";
        }
      } else if (this.isHorizontal) {
        let width = event.pageX;
        if (width < this.maxWidth && width > this.minWidth) {
          parentElement.style.width = width + "px";
        }
      }
    }

    this.$el.onmousedown = () => {
      window.addEventListener('mousemove', resize, false);
      window.addEventListener('mouseup', stopResize, false);
    }
  }
}
</script>

<style scoped>

.dragger {
  height: v-bind(height);
  width: v-bind(width);
  opacity: 0;
  background-color: rgba(255, 255, 255, 0.2);
  position: absolute;
  transition: opacity 0.1s;
  inset: v-bind(inset);
  transform: v-bind(transform);
  @apply z-10;
}

.dragger:hover {
  opacity: 100%;
  cursor: v-bind(cursor);
}

.dragger:active {
  opacity: 100%;
  cursor: v-bind(cursor);
}

</style>