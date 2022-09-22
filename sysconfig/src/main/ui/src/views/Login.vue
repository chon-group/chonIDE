<template>
  <div class="login u-column u-justify-i-center u-gap-1">
    <Response ref="response"/>
    <h1 class="logo u-column u-align-i-center u-gap-2">
      <span class="logo__systemname u-row u-gap-3 u-align-i-center">
        <span class="is-bold">chonos</span>
        <span class="is-aside is-light">sysconfig</span>
      </span>
    </h1>
    <div>
      <div class="c-base-container login__form u-column u-gap-3">
        <input type="text" name="user" placeholder="Usuário" class="login__input" v-model="username"
               autocomplete="off"/>
        <Trace/>
        <input type="password" name="password" placeholder="Senha" class="login__input" v-model="password"
               autocomplete="off"/>
      </div>
      <RoundedButton @click="submit" :is-loading="loading"/>
    </div>

    <Popup :title="'Conectando à rede'" type="now" v-if="this.awaitConnection == 'true'" :can-close="false">
      <template v-slot:content>
        <div class="u-column u-gap-2 u-align-i-center" v-if="awaitConnectionCounter != 0">
          <span>Por favor, aguarde... {{ awaitConnectionCounter }}</span>
          <Loading/>
        </div>
        <div v-else class="u-column u-gap-3 u-align-i-center is-big">
          Seu novo link de acesso ao sysconfig
          <a :href="'https://' + currentDomain + ':3270/sysconfig'" target="_blank"
             class="login__new-link">{{
              'https://' + currentDomain + ':3270/sysconfig'
            }}</a>
        </div>
      </template>
    </Popup>

  </div>
</template>

<script>
import RoundedButton from "@/components/RoundedButton";
import PageUtils from "@/assets/js/util/PageUtils";
import Trace from "@/components/Trace";
import axios from "axios";
import router from "@/router";
import {MessageType} from "@/assets/js/model/Enums"
import Popup from "@/components/Popup";
import Loading from "@/components/Loading";


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
      currentDomain: ''
    }
  },
  setup() {
    // PageUtils.isLogged().then((response) => {
    //   if (response.data == true) {
    //     router.push("/manager");
    //   }
    // });
    PageUtils.setTitle("Entrar");
  },
  beforeUnmount() {
    document.removeEventListener("keypress", this.keyPressListener);
  },
  mounted() {
    this.awaitConnection = localStorage.getItem("connecting");
    if (this.awaitConnection === 'true') {
      axios.get("/sysconfig/network/domain/status").then((response) => {
        this.currentDomain = response.data.domain;
        axios.post("/sysconfig/user/disconnect");
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
        this.$root.message({content: "O usuário e/ou a senha não podem ser vazios", type: MessageType.ERROR});
        return;
      }

      this.loading = true;
      axios.post("/sysconfig/user/connect",
          {
            username: this.username,
            password: this.password
          }
      ).then((response) => {
        if (response.data === true) {
          PageUtils.isFirstAccess().then((response) => {
            if (Boolean(response.data) === true) {
              router.push("/domain");
            } else {
              router.push("/manager");
            }
          });
        } else {
          this.loading = false;
          this.$root.message({content: "Não foi possível conectar ao sistema", type: MessageType.ERROR});
        }
      }).catch(() => {
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
  font-weight: 700;
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