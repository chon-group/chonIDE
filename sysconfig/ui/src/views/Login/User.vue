<script>
export default {
  name: 'User',
  props: {
    name: String,
    host: String
  }
}
</script>

<template>
  <div class="user">
    <div class="user__photo"> {{ this.name.charAt(0).toUpperCase() }}</div>
    <div class="flex flex-col gap-0.5">
      <span class="user__name"> {{ name }}</span>
      <span class="user__host"> {{ host }} </span>
    </div>
  </div>
</template>

<style scoped>

.user {
  background-color: var(--pallete-color-black-2);
  min-width: 400px;
  @apply flex gap-4 rounded-md p-4 cursor-default;
}

.user:hover {
  background-color: var(--pallete-color-black-3);
}

.user__photo {
  background-color: var(--pallete-color-black-4);
  min-height: 100%;
  aspect-ratio: 1/1;
  @apply flex items-center justify-center rounded-full text-xl;
}

.user__name {
  @apply text-xl;
}

.user__host {
  color: var(--pallete-text-aside);
  @apply text-base;
}

</style>