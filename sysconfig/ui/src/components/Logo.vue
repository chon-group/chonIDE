<template>
  <h1 class="login__logo flex flex-col items-center gap-5">
    <span class="flex gap-2.5 items-center">
      <a class="logo__chonos font-black" href="http://chonos.sf.net/" target="_blank">chonos</a>
      <span class="text-aside font-thin">{{ name }}</span>
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

.login__logo {
  font-size: 36px;
  line-height: 1;
  cursor: default;
}

.logo__chonos {
  color: var(--pallete-text-main);
}

</style>