<script>

import Input from "@/components/general/Input.vue";
import Button from "@/components/general/Button.vue";
import {AppEvent, MessageType} from "@/utils/enums";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {Routes} from "@/router/routes";

export default {
  name: 'UserForm',
  components: {Button, Input},
  props: {
    displayName: String,
    name: String,
    host: String
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
          Api.get(EndPoints.USERS_FIRST_ACCESS).then((response) => {
            if (response.data.data === true) {
              this.$router.push(Routes.DOMAIN);
            } else {
              Api.get(EndPoints.CONFIGURATION);
              this.$router.push(Routes.HOME);
            }
          }).finally(() => {
            this.loggingIn = false;
          });
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
    </div>
    <div class="flex flex-col w-full gap-2">
      <Button text="Login" main-color width-full @click="submit" :is-loading="loggingIn"/>
      <Button text="Back" color="var(--pallete-color-black-3)" @click="$emit('back')" width-full/>
    </div>
  </div>
</template>

<style scoped>

.user-form {
  border: var(--border-trace);
  background-color: var(--pallete-color-black-2);
  min-width: 300px;
  @apply flex flex-col items-center gap-6 p-6 rounded-md;
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