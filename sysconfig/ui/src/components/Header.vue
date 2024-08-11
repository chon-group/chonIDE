<template>
  <div class="header">
    <div class="flex items-center h-full gap-2.5">
      <h2 class="header__logo">chonIDE</h2>
      <slot name="left"></slot>
    </div>
    <div class="header__center">
      <slot name="center"></slot>
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
  background-color: var(--pallete-color-black-2);
  border-bottom: 1px solid var(--pallete-color-black-4);
  @apply shrink-0 flex justify-between items-center w-full h-fit sticky top-0 p-1.5;
}

.header__logo {
  color: var(--pallete-text-main);
  @apply text-sm font-thin ml-1.5 cursor-pointer;
}

.header__center {
  transform: translateX(-50%);
  @apply flex items-center h-full gap-2.5 absolute left-2/4;
}

</style>