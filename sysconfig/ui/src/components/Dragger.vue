<template>
  <div class="dragger" draggable="true"></div>
</template>

<script>
export default {
  name: "Dragger",
  props: {
    horizontal: Boolean,
    vertical: Boolean
  },
  data() {
    return {
      lastValue: 0
    }
  },
  computed: {
    height() {
      if (this.horizontal) {
        return "100%";
      } else {
        return "4px";
      }
    },
    width() {
      if (this.horizontal) {
        return "4px";
      } else {
        return "100%";
      }
    },
    transform() {
      if (this.vertical) {
        return "translateY(-100%)";
      } else {
        return "translateX(-100%)";
      }
    },
    cursor() {
      if (this.vertical) {
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
    this.$el.ondrag = (event) => {
      let height = Math.abs(event.clientY - window.innerHeight);
      this.lastValue = height;
      parentElement.style.height = height + "px";
    }
    this.$el.ondragend = () => {
      if(this.vertical) {
        parentElement.style.height = this.lastValue + "px";
      }
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
  transform: v-bind(transform);
  transition: opacity 0.1s;
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