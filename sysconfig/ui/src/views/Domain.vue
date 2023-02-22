<template>
  <div class="domain u-column u-gap-1 u-align-i-center">
    <h2 class="is-huge">Informe o nome do seu bot</h2>
    <div class="u-column u-align-i-center u-gap-2">
      <input type="text" class="domain__name is-huge" v-model="domain" placeholder="Nome do bot"
             maxlength="30">
      <div class="domain__new-url u-column u-gap-3 u-align-i-center">
        <span class="is-headline">URL de acesso a chonIDE</span>
        <span class="is-huge is-aside">{{domainUrl}}</span>
      </div>
      <div class="u-row u-gap-3">
        <Button @click="backToCoder" v-if="!isFirstAccess">
          <template v-slot:content>
            Voltar para codador
          </template>
        </Button>
        <Button @click="submit" :isLoading="loading" main-color>
          <template v-slot:content>
            Salvar nome
          </template>
        </Button>
      </div>
    </div>
  </div>
</template>

<script>

import Util from "@/domain/Util";
import Button from "@/components/Button";
import router, {Routes} from "@/router";
import {AppEvent, MessageType} from "@/domain/Enums"
import {API, EndPoints} from "@/domain/API";


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
    Util.setTitle("Nome do bot");
    API.loadToken();
  },
  mounted() {
    API.get(EndPoints.USERS_FIRST_ACCESS).then((response) => {
      this.isFirstAccess = Boolean(response.data.data);
    });
  },
  methods: {
    backToCoder() {
      router.push(Routes.CODER);
    },
    submit() {
      if (this.domain === '' || this.domain.length === 0) {
        this.$emit(AppEvent.MESSAGE, {content: "O nome não pode ser vazio", type: MessageType.ERROR});
        return;
      }
      this.loading = true;
      API.post(EndPoints.DOMAINS, {params: {domain: this.domain}}).then(() => {
        this.$emit(AppEvent.MESSAGE, {content: "O nome do seu bot foi salvo com sucesso", type: MessageType.SUCCESS});
        router.push(Routes.CONNECT);
      });
    }
  }
}
</script>

<style scoped>

.domain__name {
  min-width: 500px;
  color: var(--pallete-text-main);
  text-align: center;
  padding-bottom: var(--ratio-2);
  background-color: transparent;
  border: none;
  border-bottom: var(--border-trace);
}

.domain__new-url {
  border: var(--border-trace);
  border-radius: var(--border-radius-container);
  padding: var(--ratio-2);
  background-color: var(--pallete-color-black-2);
}

</style>