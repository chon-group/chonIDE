<template>
  <div class="login">
    <div class="login__logo" v-if="user == null">
      <h1 class="text-3xl">chonIDE</h1>
      <p class="text-base text-aside text-center">
        IDE to development of<br>Embedded Multi-Agent Systems
      </p>
    </div>

    <div class="flex flex-col gap-6 items-center flex-grow" v-if="user == null && this.attributes != null">
      <span class="text-base">Enter on</span>

      <User host="localhost" :name="this.attributes.username"
            @click="this.user = {name: this.attributes.username, host: 'localhost'}"/>

      <div class="flex justify-between items-center w-full" v-if="this.configuration.neighbors">
          <span class="text-base">
            Your neighbors
          </span>
        <Button icon="refresh.svg" @click="loadNeighbors" :is-loading="loadingNeighbors"/>
      </div>

      <div class="neighbors">
        <User
            v-for="(neighbor, index) in neighbors"
            :host="neighbor.ip" :name="neighbor.name"
            :key="index"
            @click="this.user = {name: neighbor.name, host: neighbor.ip}"/>
      </div>

      <span class="text-base text-aside" v-if="neighbors.length === 0">No neighbors around...</span>
    </div>

    <div v-else-if="this.attributes == null">
      <Loading/>
    </div>

    <div class="flex flex-col items-center justify-center h-full" v-else-if="user != null">
      <UserForm :user="user" @login="submit($event)" @back="() => {
        this.user = null;
        this.loadingNeighbors();
      }" :logging-in="loggingIn"/>
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
import Popup from "@/components/Popup.vue";
import Loading from "@/components/Loading.vue";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {Routes} from "@/router/routes";
import User from "@/views/Login/User.vue";
import UserForm from "@/views/Login/UserForm.vue";
import Button from "@/components/Button.vue";


export default {
  name: "Login",
  components: {Button, UserForm, User, Loading, Popup},
  data() {
    return {
      attributes: null,
      configuration: null,
      loggingIn: false,
      loadingNeighbors: false,
      neighbors: [],
      user: null,
      systemAttributes: null,
      awaitConnection: 'false',
      awaitConnectionCounter: 5,
      currentDomain: null
    }
  },
  setup() {
    GeneralUtil.setTitle("Entrar");
    Api.loadToken();
  },
  mounted() {

    Api.get(EndPoints.ATTRIBUTES).then((response) => {
      this.attributes = response.data.data;
    });

    Api.get(EndPoints.CONFIGURATION).then((response) => {
      this.configuration = response.data.data;
    }).then(() => {
      if (this.configuration.neighbors) {
        this.loadNeighbors();
      }
    });

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
  },
  methods: {
    loadNeighbors() {
      this.loadingNeighbors = true;
      Api.get(EndPoints.NEIGHBORS).then((response) => {
        this.neighbors = response.data.data;
      }).finally(() => {
        this.loadingNeighbors = false;
      });
    },
    submit(password) {
      this.loggingIn = true;
      Api.auth(this.user.name, password, this.user.host).then((response) => {
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

<style scoped>

.login {
  @apply h-screen flex flex-col items-center p-10 gap-10;
}

.login__logo {
  line-height: 1;
  @apply flex flex-col items-center gap-2 select-none whitespace-nowrap;
}

.login__new-link {
  color: var(--pallete-text-main);
  border: var(--border-trace);
  @apply text-center rounded-sm w-full py-2.5;
}

.login__new-link:hover {
  background-color: var(--pallete-color-black-3);
}

.neighbors {
  max-height: 50vh;
  @apply flex flex-col gap-6 items-center rounded-md overflow-y-auto;
}
</style>