<template>
  <div class="header">
    <div class="flex items-center h-full gap-2.5">
      <h2 class="header__logo">chonIDE</h2>
      <slot name="left"></slot>
    </div>
    <slot name="right"></slot>
  </div>
</template>

<script>
export default {
  name: "Header"
}
</script>

<style scoped>

.header {
  background-color: var(--pallete-color-black-3);
  border-bottom: 1px solid var(--pallete-color-black-1);
  height: 30px;
  @apply shrink-0 flex justify-between items-center w-full sticky top-0;
}


.header__logo {
  @apply text-sm font-thin ml-2.5 cursor-default;
}

</style>