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
      } else if (this.left) {
        return "auto auto auto 0";
      } else if (this.bottom) {
        return "auto auto auto 0";
      } else {
        return "";
      }
    },
    transform() {
      if (this.top) {
        return "translateY(-50%)";
      } else if (this.bottom) {
        return "translateY(50%)";
      } else if (this.right) {
        return "translateX(50%)";
      } else if (this.left) {
        return "translateX(-50%)";
      } else {
        return "";
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
        let height;
        if (this.top) {
          height = Math.abs(window.innerHeight - event.pageY);
        } else {
          height = 0
        }
        parentElement.style.height = height + "px";
      } else if (this.isHorizontal) {
        let width;
        if (this.right) {
          width = event.pageX
        } else {
          width = Math.abs(window.innerWidth - event.pageX);
        }
        parentElement.style.width = width + "px";
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
  position: absolute;
  inset: v-bind(inset);
  transform: v-bind(transform);
  cursor: v-bind(cursor);
  @apply z-10;
}

</style>