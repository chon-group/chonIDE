<template>
  <div class="h-full flex flex-col items-center justify-center gap-10">
    <h1 class="logo flex flex-col items-center gap-5">
      <span class="logo__systemname">
        <span class="font-black">chonIDE</span>
      </span>
    </h1>
    <div class="flex flex-col gap-5 items-center">
      <div class="login__form">
        <Input v-model="username" center placeholder="User"/>
        <Input v-model="password" center placeholder="Password" type="password"/>
        <Input v-if="!useLocalHost" v-model="currentHost" center placeholder="Hostname"/>
      </div>
      <div class="flex items-center gap-5">
        <div class="flex gap-2.5 items-center justify-center">
        <span class="text-aside">
          Localhost
        </span>
          <input v-model="useLocalHost" class="login__check-box" type="checkbox">
        </div>
        <Button :is-loading="loading" text="Enter" @click="submit"/>
      </div>
    </div>

    <Popup ref="conecting" :can-close="false" :title="'Connecting to the network'">
      <template v-slot:content>
        <div v-if="awaitConnectionCounter != 0" class="flex flex-col gap-5 items-center">
          <span>
            Please wait...
            <span v-if="currentDomain != null">{{ awaitConnectionCounter }}</span>
          </span>
          <Loading/>
        </div>
        <div v-else class="flex flex-col gap-2.5 items-center text-base">
          Your new access link to chonide
          <a :href="'https://' + currentDomain + ':3270/chonide/login'" class="login__new-link"
             target="_blank">{{
              'https://' + currentDomain + ':3270/chonide/login'
            }}</a>
        </div>
      </template>
    </Popup>

  </div>
</template>

<script>
import GeneralUtil from "@/utils/generalUtil";
import {AppEvent, MessageType} from "@/utils/enums"
import Popup from "@/components/Popup";
import Loading from "@/components/Loading";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import Button from "@/components/Button";
import Input from "@/components/Input";
import {Routes} from "@/router/routes";


export default {
  name: "Login",
  components: {Input, Button, Loading, Popup},
  data() {
    return {
      username: '',
      password: '',
      loading: false,
      keyPressListener: null,
      awaitConnection: 'false',
      awaitConnectionCounter: 5,
      currentDomain: null,
      useLocalHost: true,
      currentHost: ''
    }
  },
  setup() {
    GeneralUtil.setTitle("Entrar");
    Api.loadToken();
  },
  beforeUnmount() {
    document.removeEventListener("keypress", this.keyPressListener);
  },
  mounted() {
    this.awaitConnection = localStorage.getItem("connecting");
    if (this.awaitConnection === 'true') {
      Api.get(EndPoints.DOMAINS).then((response) => {
        this.$refs.conecting.showing(true);
        this.currentDomain = response.data.data.domain;
        Api.delete(EndPoints.USERS);
        let interval = setInterval(() => {
          this.awaitConnectionCounter--;
          if (this.awaitConnectionCounter === 0) {
            localStorage.setItem("connecting", "false");
            clearInterval(interval);
          }
        }, 1000);
      }).catch(() => {
        this.$emit(AppEvent.MESSAGE, {
          content: "It was not possible generate new access link to chonide", type:
          MessageType.ERROR
        });
        localStorage.setItem("connecting", "false");
      });
    }

    this.keyPressListener = (event) => {
      if (event.which === 13) {
        this.submit();
      }
    }
    document.addEventListener("keypress", this.keyPressListener);
  },
  methods: {
    submit() {
      if (this.username.length === 0 || this.password.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "User and/or password cannot be empty", type: MessageType.ERROR});
        return;
      }

      this.loading = true;
      Api.auth(this.username, this.password, this.currentHost).then((response) => {
        if (response.data.status == 200) {
          Api.get(EndPoints.USERS_FIRST_ACCESS).then((response) => {
            if (response.data.data == true) {
              this.$router.push(Routes.DOMAIN);
            } else {
              Api.get(EndPoints.CONFIGURATION);
              this.$router.push(Routes.HOME);
            }
          });
        }
      }).catch((error) => {
        if (error.response.status == 401) {
          this.$emit(AppEvent.MESSAGE, {content: error.response.data.message, type: MessageType.ERROR});
        } else {
          this.$emit(AppEvent.MESSAGE, {content: "Unable to access the system", type: MessageType.ERROR});
        }
      }).finally(() => {
        this.loading = false;
      });
    }
  }
}
</script>

<style scoped>
.logo {
  line-height: 1;
  @apply select-none whitespace-nowrap;
}

.logo__systemname {
  color: var(--pallete-text-main);
  font-size: var(--text-size-title);
}

.login__form {
  width: 400px;
  @apply flex flex-col gap-2.5;
}

.login__check-box {
  accent-color: var(--pallete-text-aside);
  @apply cursor-pointer;
}

.login__new-link {
  color: var(--pallete-text-main);
  border: var(--border-trace);
  @apply text-center rounded-sm w-full py-2.5;
}

.login__new-link:hover {
  background-color: var(--pallete-color-black-3);
}
</style>