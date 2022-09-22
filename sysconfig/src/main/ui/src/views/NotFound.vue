<template>
  <div class="u-column u-gap-1 u-align-i-center">
    <span class="not-found__404">404</span>
    <h1 class="is-huge">Opa! Parece que temos alguém perdido.</h1>
    <RoundedButton direction="left"/>
  </div>
</template>

<script>
import RoundedButton from "@/components/RoundedButton";

export default {
  name: "NotFound",
  components: {RoundedButton}
}
</script>

<style scoped>

.not-found__404 {
  font-size: 12em;
  font-weight: 800;
  background: -webkit-linear-gradient(var(--pallete-color-black-2), transparent);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  line-height: 1;
  margin-bottom: -110px;
}

</style>