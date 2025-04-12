<script>
import Input from "@/components/general/Input.vue";
import Button from "@/components/general/Button.vue";
import {AppEvent, MessageType} from "@/utils/enums";
import {Api} from "@/services/chonide/api";
import {Routes} from "@/router/routes";
import projectNamesColor from "@/assets/css/name-colors.json";

export default {
  name: "UserForm",
  components: {Button, Input},
  props: {
    displayName: String,
    name: String,
    host: String,
  },
  computed: {
    photoColor() {
      if (this.displayName == null) {
        return "var(--pallete-color-black-3)";
      }
      return projectNamesColor[this.displayName.charAt(0).toUpperCase()];
    },
  },
  data() {
    return {
      user: {
        name: this.name != null ? this.name : "",
        password: "",
        host: this.host != null ? this.host : "",
      },
      loggingIn: false,
    };
  },
  mounted() {
    this.$el.focus();
  },
  methods: {
    submit() {
      if (this.user.name.length === 0) {
        this.$emit(AppEvent.MESSAGE, {
          content: "User must be filled",
          type: MessageType.ERROR,
        });
        return;
      }

      if (this.user.password.length === 0) {
        this.$emit(AppEvent.MESSAGE, {
          content: "Password must be filled",
          type: MessageType.ERROR,
        });
        return;
      }

      this.loggingIn = true;
      Api.auth(this.user.name, this.user.password, this.user.host)
          .then((response) => {
            if (response.data.status === 200) {
              this.$router.push(Routes.HOME);
            }
          })
          .catch((error) => {
            if (error.response.status === 401) {
              this.$emit(AppEvent.MESSAGE, {
                content: error.response.data.message,
                type: MessageType.ERROR,
              });
            } else {
              this.$emit(AppEvent.MESSAGE, {
                content: "Unable to access the system",
                type: MessageType.ERROR,
              });
            }
            this.loggingIn = false;
          });
    },
    sanitizeHost() {
      this.user.host = this.user.host.replace(/[^a-zA-Z0-9.]/g, "");
    },
    sanitizeUsername() {
      this.user.name = this.user.name.replace(/[^a-zA-Z0-9.-]/g, "");
    },
  },
};
</script>

<template>
  <div class="user-form" tabindex="1" @keyup.enter="submit">
    <div class="flex flex-col items-center gap-4">
      <div class="user-form__photo">{{ displayName.charAt(0).toUpperCase() }}</div>
      <span class="user-form__name">{{ displayName }}</span>
    </div>
    <div class="flex flex-col items-center gap-2 w-full">
      <Input
          v-if="host == null"
          v-model="user.host"
          center
          placeholder="Host"
          @input="sanitizeHost"
      />
      <Input
          v-if="name == null"
          v-model="user.name"
          center
          placeholder="Username"
          @input="sanitizeUsername"
      />
      <Input
          v-model="user.password"
          center
          placeholder="Password"
          type="password"
      />
      <Button
          :is-loading="loggingIn"
          main-color
          text="Login"
          width-full
          @click="submit"
      />
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
