<script>

import Input from "@/components/Input.vue";
import Button from "@/components/Button.vue";

export default {
  name: 'UserForm',
  components: {Button, Input},
  props: {
    user: {},
    loggingIn: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      password: ""
    }
  }
}
</script>

<template>
  <div class="user-form">
    <div class="flex flex-col items-center gap-4">
      <div class="user-form__photo">{{ this.user.name.charAt(0).toUpperCase() }}</div>
      <span class="user-form__name"> {{ user.name }}</span>
    </div>
    <Input type="password" placeholder="Password" center v-model="password"/>
    <div class="flex justify-between w-full gap-4">
      <Button text="Back" color="var(--pallete-color-black-2)" @click="$emit('back')"/>
      <Button text="Login" main-color class="grow" @click="$emit('login', this.password)" :is-loading="loggingIn"/>
    </div>
  </div>
</template>

<style scoped>

.user-form {
  background-color: var(--pallete-color-black-2);
  @apply flex flex-col items-center gap-8 p-6 rounded-md;
}

.user-form__photo {
  background-color: var(--pallete-color-black-4);
  min-height: 100px;
  aspect-ratio: 1/1;
  @apply flex items-center justify-center rounded-full text-2xl;
}

.user-form__name {
  @apply text-2xl;
}

</style>