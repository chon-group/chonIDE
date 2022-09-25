<template>
  <h1 class="logo u-column u-align-i-center u-gap-2">
    <span class="u-row u-gap-3 u-align-i-center">
      <a href="http://chonos.sf.net/" target="_blank" class="logo__chonos is-bold">chonos</a>
      <span class="is-aside is-light">{{ name }}</span>
    </span>
  </h1>
</template>

<script>
export default {
  name: "Logo",
  props: {
    name: String
  }
}
</script>

<style scoped>

.logo {
  font-size: 36px;
  line-height: 1;
  cursor: default;
}

.logo__chonos {
  color: var(--pallete-text-main);
}

</style>