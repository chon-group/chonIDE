<script>

import Input from "@/components/general/Input.vue";
import Button from "@/components/general/Button.vue";
import {AppEvent, MessageType} from "@/utils/enums";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {Routes} from "@/router/routes";
import projectNamesColor from "@/assets/css/name-colors.json";

export default {
  name: 'UserForm',
  components: {Button, Input},
  props: {
    displayName: String,
    name: String,
    host: String
  },
  computed: {
    photoColor() {
      return projectNamesColor[this.name.charAt(0).toUpperCase()]
    }
  },
  data() {
    return {
      user: {
        name: "",
        password: "",
        host: this.host
      },
      loggingIn: false
    }
  },
  mounted() {
    this.$el.focus();
  },
  methods: {
    submit() {
      if (this.name != null) {
        this.user.name = this.name;
      }

      if (this.user.name.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "User must be filled", type: MessageType.ERROR});
        return;
      }

      if (this.user.password.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "Password must be filled", type: MessageType.ERROR});
        return;
      }

      this.loggingIn = true;
      Api.auth(this.user.name, this.user.password, this.user.host).then((response) => {
        if (response.data.status === 200) {
          this.$router.push(Routes.HOME);
        }
      }).catch((error) => {
        if (error.response.status === 401) {
          this.$emit(AppEvent.MESSAGE, {content: error.response.data.message, type: MessageType.ERROR});
        } else {
          this.$emit(AppEvent.MESSAGE, {content: "Unable to access the system", type: MessageType.ERROR});
        }
        this.loggingIn = false;
      });

    }
  }
}
</script>

<template>
  <div class="user-form" @keyup.enter="submit" tabindex="1">
    <div class="flex flex-col items-center gap-4">
      <div class="user-form__photo">{{ displayName.charAt(0).toUpperCase() }}</div>
      <span class="user-form__name"> {{ displayName }}</span>
    </div>
    <div class="flex flex-col items-center gap-2 w-full">
      <Input v-if="name == null" placeholder="Username" center v-model="user.name"/>
      <Input type="password" placeholder="Password" center v-model="user.password"/>
      <Button text="Login" main-color width-full @click="submit" :is-loading="loggingIn"/>
    </div>
  </div>
</template>

<style scoped>

.user-form {
  background-color: var(--pallete-color-black-2);
  @apply flex flex-col items-center gap-6 rounded-md w-full;
}

.user-form__photo {
  background-color: v-bind(photoColor);
  min-height: 100px;
  aspect-ratio: 1/1;
  @apply flex items-center justify-center rounded-full text-2xl;
}

.user-form__name {
  @apply text-2xl;
}

</style>