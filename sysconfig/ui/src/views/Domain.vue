<template>
  <div class="flex flex-col w-screen h-screen">
    <Header>
      <template v-slot:left>
        <router-link to="/home">
          <Button icon="back.svg" icon-ratio="11px"/>
        </router-link>
      </template>
    </Header>
    <div class="flex flex-col gap-10 items-center m-auto">
      <h2 class="text-xl">Enter the name of your bot</h2>
      <div class="flex flex-col items-center gap-5">
        <input v-model="domain" class="domain__name" maxlength="30" placeholder="Bot name"
               type="text">
        <div class="domain__new-url">
          <span class="text-base">chonIDE Access URL</span>
          <span class="text-xl text-aside">{{ domainUrl }}</span>
        </div>
        <div class="flex gap-2.5">
          <Button v-if="!isFirstAccess" @click="backToHome">
            <template v-slot:content>
              Back
            </template>
          </Button>
          <Button :isLoading="loading" main-color @click="submit">
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

import GeneralUtil from "@/utils/generalUtil";
import Button from "@/components/Button";
import {AppEvent, MessageType} from "@/utils/enums"
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import Header from "@/components/layout/Header";
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
      if (this.domain.length > 0) {
        return this.domain + '.bot.chon.group:3270/chonide';
      } else {
        return "<nome>.bot.chon.group:3270/chonide"
      }
    }
  },
  watch: {
    domain(newValue) {
      this.domain = GeneralUtil.mantainJustRegularCharacters(newValue).toLowerCase();
    }
  },
  setup() {
    GeneralUtil.setTitle("Bot name");
    Api.loadToken();
  },
  mounted() {
    Api.get(EndPoints.USERS_FIRST_ACCESS).then((response) => {
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
      Api.post(EndPoints.DOMAINS, {params: {domain: this.domain}}).then(() => {
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