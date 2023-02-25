<template>
  <div class="login u-column u-align-i-center u-justify-i-center u-gap-1">
    <h1 class="logo u-column u-align-i-center u-gap-2">
      <span class="logo__systemname">
        <span class="is-bold">chonIDE</span>
      </span>
    </h1>
    <div class="u-column u-gap-2 u-align-i-center">
      <div class="c-base-container login__form u-column u-gap-3">
        <input type="text" name="user" placeholder="Usuário" class="login__input" v-model="username"
               autocomplete="off"/>
        <Trace/>
        <input type="password" name="password" placeholder="Senha" class="login__input" v-model="password"
               autocomplete="off"/>
        <Trace v-if="!useLocalHost"/>
        <input type="text" v-model="currentHost" v-if="!useLocalHost" placeholder="Hostname" class="login__input"/>
      </div>
      <RoundedButton @click="submit" :is-loading="loading"/>
    </div>
    <div class="u-row u-gap-3 u-align-i-center u-justify-i-center">
      <span class="is-aside">
        Usar host local
      </span>
      <input type="checkbox" class="login__check-box" v-model="useLocalHost">
    </div>

    <Popup :title="'Conectando à rede'" :can-close="false" ref="conecting">
      <template v-slot:content>
        <div class="u-column u-gap-2 u-align-i-center" v-if="awaitConnectionCounter != 0">
          <span>
            Por favor, aguarde...
            <span v-if="currentDomain != null">{{ awaitConnectionCounter }}</span>
          </span>
          <Loading/>
        </div>
        <div v-else class="u-column u-gap-3 u-align-i-center is-headline">
          Seu novo link de acesso à chonide
          <a :href="'https://' + currentDomain + ':3270/chonide/login'" target="_blank"
             class="login__new-link">{{
              'https://' + currentDomain + ':3270/chonide/login'
            }}</a>
        </div>
      </template>
    </Popup>

  </div>
</template>

<script>
import RoundedButton from "@/components/RoundedButton";
import Util from "@/domain/Util";
import Trace from "@/components/Trace";
import router, {Routes} from "@/router";
import {AppEvent, MessageType} from "@/domain/Enums"
import Popup from "@/components/Popup";
import Loading from "@/components/Loading";
import {API, EndPoints} from "@/domain/API";


export default {
  name: "Login",
  components: {Loading, Popup, Trace, RoundedButton},
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
    Util.setTitle("Entrar");
    API.loadToken();
  },
  beforeUnmount() {
    document.removeEventListener("keypress", this.keyPressListener);
  },
  mounted() {
    this.awaitConnection = localStorage.getItem("connecting");
    if (this.awaitConnection === 'true') {
      this.$refs.conecting.showing(true);
      API.get(EndPoints.DOMAINS).then((response) => {
        this.currentDomain = response.data.data.domain;
        API.delete(EndPoints.USERS);
        let interval = setInterval(() => {
          this.awaitConnectionCounter--;
          if (this.awaitConnectionCounter == 0) {
            localStorage.setItem("connecting", "false");
            clearInterval(interval);
          }
        }, 1000);
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
        this.$emit(AppEvent.MESSAGE, {content: "O usuário e/ou a senha não podem ser vazios", type: MessageType.ERROR});
        return;
      }

      this.loading = true;
      API.auth(this.username, this.password, this.currentHost).then((response) => {
        if (response.status == 200) {
          API.get(EndPoints.USERS_FIRST_ACCESS).then((response) => {
            if (response.data.data == true) {
              router.push(Routes.DOMAIN);
            } else {
              router.push(Routes.CODER);
            }
          });
        }
      }).catch((error) => {
        if (error.response.status == 401) {
          this.$emit(AppEvent.MESSAGE, {content: error.response.data.message, type: MessageType.ERROR});
        } else {
          this.$emit(AppEvent.MESSAGE, {content: "Não foi possível acessar o sistema", type: MessageType.ERROR});
        }
      }).finally(() => {
        this.loading = false;
      });
    }
  }
}
</script>

<style scoped>
.login {
  height: 100%;
}

.logo {
  user-select: none;
  line-height: 1;
  white-space: nowrap;
}

.logo__systemname {
  color: var(--pallete-text-main);
  font-size: var(--text-size-title);
}

.login__form {
  width: var(--container-width-2);
  font-size: var(--text-size-title);
}

.login__input {
  padding: var(--ratio-3);
  border: none;
  background-color: transparent;
  cursor: default;
  border-radius: var(--border-radius-item);
  cursor: default;
  text-align: center;
  color: var(--pallete-text-main);
}

.login__input:hover {
  background-color: var(--pallete-color-black-3);
}

.login__check-box {
  cursor: pointer;
  accent-color: var(--pallete-text-aside);
}

.login__new-link {
  color: var(--pallete-text-main);
  padding: var(--ratio-3) 0;
  width: 100%;
  border: var(--border-trace);
  border-radius: var(--border-radius-item);
  text-align: center;
}

.login__new-link:hover {
  background-color: var(--pallete-color-black-3);
}
</style>