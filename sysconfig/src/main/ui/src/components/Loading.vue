<template>
  <div class="loading"></div>
</template>

<script>
export default {
  name: "Loading",
  props: {
    mainColor: String,
    asideColor: String,
    ratio: String,
    borderWidth: String
  },
  data() {
    return {
      asideColorStyle: this.asideColor == null ? "var(--pallete-color-black-3)" : this.asideColor,
      mainColorStyle: this.mainColor == null ? "var(--pallete-color-main-2)" : this.mainColor,
      ratioStyle: this.ratio == null ? "30px" : this.ratio + "px",
      borderWidthStyle: this.borderWidth == null ? "4px" : this.borderWidth + "px"
    }
  }
}
</script>

<style scoped>

.loading {
  border-width: v-bind(borderWidthStyle);
  border-style: solid;
  border-color: v-bind(asideColorStyle);
  border-top-color: v-bind(mainColorStyle);
  border-radius: 50%;
  width: v-bind(ratioStyle);
  height: v-bind(ratioStyle);
  animation: loading 0.9s cubic-bezier(.17, .67, .83, .67) infinite;
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