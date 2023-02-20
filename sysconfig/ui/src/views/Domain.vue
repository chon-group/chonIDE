<template>
  <div class="u-column u-gap-1 u-align-i-center">
    <div class="u-column u-gap-3 u-align-i-center">
      <h2 class="is-huge">Informe o nome do seu bot</h2>
      <span class="is-aside">
        Através desse nome, você será capaz de acessar a chonide via:
        <span v-if="domain !== ''" class="is-bold">
          {{ domainUrl }}
        </span>
      </span>
    </div>
    <div class="u-column u-align-i-center u-gap-2">
      <input type="text" class="c-base-container domain-input is-huge" v-model="domain" placeholder="Nome do bot"
             maxlength="30">
      <Button @click="submit" :isLoading="loading">
        <template v-slot:content>
          Salvar nome
        </template>
      </Button>
    </div>
    <router-link to="/coder" v-if="!isFirstAccess">
      <Button transparent>
        <template v-slot:content>
          Voltar para codador
        </template>
      </Button>
    </router-link>
  </div>
</template>

<script>

import Util from "@/main/Util";
import Button from "@/components/Button";
import router, {Routes} from "@/router";
import {MessageType} from "@/main/Enums"
import {API, EndPoints} from "@/main/API";


export default {
  name: "Domain",
  components: {Button},
  data() {
    return {
      domain: '',
      loading: false,
      isFirstAccess: false
    }
  },
  computed: {
    domainUrl() {
      return this.domain + '.bot.chon.group:3270/chonide';
    }
  },
  watch: {
    domain(newValue) {
      this.domain = newValue.replace(" ", "").toLowerCase();
    }
  },
  setup() {
    Util.setTitle("Nome do bot");
    API.loadToken();
  },
  mounted() {
    API.get(EndPoints.USERS_FIRST_ACCESS).then((response) => {
      this.isFirstAccess = Boolean(response.data.data);
    });
  },
  methods: {
    submit() {
      if (this.domain === '' || this.domain.length === 0) {
        this.$emit("message", {content: "O nome não pode ser vazio", type: MessageType.ERROR});
        return;
      }
      this.loading = true;
      API.post(EndPoints.DOMAINS, {params: {domain: this.domain}}).then(() => {
        this.$emit("message", {content: "O nome do seu bot foi salvo com sucesso", type: MessageType.SUCCESS});
        router.push(Routes.CONNECT);
      });
    }
  }
}
</script>

<style scoped>

.domain-input {
  width: 600px;
  color: var(--pallete-text-main);
  text-align: center;
}

</style>