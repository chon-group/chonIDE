<template>
  <div class="flex flex-col w-screen h-screen">
    <Header>
      <template v-slot:left>
        <router-link to="/home">
          <Button icon-ratio="11px" icon="back.svg"/>
        </router-link>
      </template>
    </Header>
    <div class="flex flex-col gap-10 items-center m-auto">
      <h2 class="text-xl">Enter the name of your bot</h2>
      <div class="flex flex-col items-center gap-5">
        <input type="text" class="domain__name" v-model="domain" placeholder="Bot name"
               maxlength="30">
        <div class="domain__new-url">
          <span class="text-base">chonIDE Access URL</span>
          <span class="text-xl text-aside">{{domainUrl}}</span>
        </div>
        <div class="flex gap-2.5">
          <Button @click="backToHome" v-if="!isFirstAccess">
            <template v-slot:content>
              Back
            </template>
          </Button>
          <Button @click="submit" :isLoading="loading" main-color>
            <template v-slot:content>
              Save name
            </template>
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import Util from "@/domain/Util";
import Button from "@/components/Button";
import {AppEvent, MessageType} from "@/domain/Enums"
import {API, EndPoints} from "@/domain/API";
import Header from "@/layout/Header";
import {Routes} from "@/router/routes";


export default {
  name: "Domain",
  components: {Header, Button},
  data() {
    return {
      domain: '',
      loading: false,
      isFirstAccess: false
    }
  },
  computed: {
    domainUrl() {
      if(this.domain.length > 0) {
        return this.domain + '.bot.chon.group:3270/chonide';
      } else {
        return "<nome>.bot.chon.group:3270/chonide"
      }
    }
  },
  watch: {
    domain(newValue) {
      this.domain = Util.mantainJustRegularCharacters(newValue).toLowerCase();
    }
  },
  setup() {
    Util.setTitle("Bot name");
    API.loadToken();
  },
  mounted() {
    API.get(EndPoints.USERS_FIRST_ACCESS).then((response) => {
      this.isFirstAccess = Boolean(response.data.data);
    });
  },
  methods: {
    backToHome() {
      this.$router.push(Routes.HOME);
    },
    submit() {
      if (this.domain === '' || this.domain.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "Name cannot be empty", type: MessageType.ERROR});
        return;
      }
      this.loading = true;
      API.post(EndPoints.DOMAINS, {params: {domain: this.domain}}).then(() => {
        this.$emit(AppEvent.MESSAGE, {content: "Your bot name was successfully saved", type: MessageType.SUCCESS});
        this.$router.push(Routes.CONNECT);
      });
    }
  }
}
</script>

<style scoped>

.domain__name {
  min-width: 500px;
  color: var(--pallete-text-main);
  border: none;
  border-bottom: var(--border-trace);
  @apply bg-transparent text-center text-xl pb-3;
}

.domain__new-url {
  border: var(--border-trace);
  background-color: var(--pallete-color-black-2);
  @apply rounded-md flex flex-col gap-2.5 items-center p-10;
}

</style>