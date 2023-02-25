<template>
  <div class="loading"></div>
</template>

<script>
export default {
  name: "Loading",
  props: {
    mainColor: {
      default: "var(--pallete-color-main-1)"
    },
    asideColor: {
      default: "var(--pallete-color-black-3)"
    },
    ratio: {
      default: "30px"
    },
    borderWidth: {
      default: "4px"
    }
  }
}
</script>

<style scoped>

.loading {
  border-width: v-bind(borderWidth);
  border-style: solid;
  border-color: v-bind(asideColor);
  border-top-color: v-bind(mainColor);
  border-radius: 50%;
  width: v-bind(ratio);
  height: v-bind(ratio);
  animation: loading 0.9s cubic-bezier(.17, .67, .83, .67) infinite;
  flex-shrink: 0;
}

@keyframes loading {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

</style>