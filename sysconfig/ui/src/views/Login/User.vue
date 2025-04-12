<script>
import projectNamesColor from "@/assets/css/name-colors.json";


export default {
  name: 'User',
  props: {
    name: String,
    host: String
  },
  computed: {
    photoColor() {
      return projectNamesColor[this.name.charAt(0).toUpperCase()]
    }
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
    <div class="user__background"></div>
  </div>
</template>

<style scoped>

.user {
  transition: transform 0.1s;
  @apply flex gap-4 rounded-md py-4 px-6 cursor-pointer w-full relative items-center;
}

.user__background {
  transition: background-color 0.1s;
  @apply block absolute w-full h-full left-0 top-0 z-10
}

.user:hover > .user__background {
  background-color: rgba(255, 255, 255, 0.08);
}

.user__photo {
  background-color: v-bind(photoColor);
  min-height: 60px;
  aspect-ratio: 1/1;
  @apply flex items-center justify-center rounded-full text-xl;
}

.user__name {
  @apply text-lg;
}

.user__host {
  color: var(--pallete-text-aside);
  @apply text-base;
}

</style>