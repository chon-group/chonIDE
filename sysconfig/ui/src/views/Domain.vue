<template>
  <div class="u-column u-gap-1 u-align-i-center">
    <Popup title="URL de acesso seu sistema" type="now" v-if="showUrl">
      <template v-slot:content>
        <a :href="'https://' + domain + '.bot.chon.group:3270/sysconfig'" target="_blank" class="domain__url is-huge">{{
            domain
          }}.bot.chon.group:3270/sysconfig</a>
      </template>
    </Popup>
    <div class="u-column u-gap-3 u-align-i-center">
      <h2 class="is-huge">Informe o nome do seu bot</h2>
      <span class="is-aside">
        Através desse nome, você será capaz de acessar o sysconfig via:
        <span v-if="domain !== ''" class="is-bold">
          {{ domain }}.bot.chon.group:3270/sysconfig
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
    <router-link to="/manager" v-if="!isFirstAccess">
      <Button skin="navigation">
        <template v-slot:content>
          Voltar para gerenciador
        </template>
      </Button>
    </router-link>
  </div>
</template>

<script>

import axios from "axios";
import PageUtils from "@/assets/js/util/PageUtils";
import Button from "@/components/Button";
import Popup from "@/components/Popup";
import router from "@/router";
import {MessageType} from "@/assets/js/model/Enums"


export default {
  name: "Domain",
  components: {Popup, Button},
  data() {
    return {
      domain: '',
      showUrl: false,
      loading: false,
      isFirstAccess: false
    }
  },
  watch: {
    domain(newValue) {
      this.domain = newValue.replace(" ", "").toLowerCase();
    }
  },
  setup() {
    PageUtils.isLogged().then((response) => {
      if (response.data == false) {
        router.push("/");
      }
    });
    PageUtils.setTitle("Nome do bot");
  },
  mounted() {
    PageUtils.isFirstAccess().then((response) => {
      this.isFirstAccess = Boolean(response.data);
    });
  },
  methods: {
    submit() {
      if (this.domain === '' || this.domain.length === 0) {
        this.$root.message({content: "O nome não pode ser vazio", type: MessageType.ERROR});
        return;
      }
      this.loading = true;
      axios.post("/sysconfig/domains", {}, {params: {domain: this.domain}}).then(() => {
        this.$root.message({content: "O nome do seu bot foi salvo com sucesso", type: MessageType.SUCCESS});
        router.push("/connect");
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

.domain__url {
  text-align: center;
  color: var(--pallete-text-main);
  padding: var(--ratio-3) 0;
  border-radius: var(--border-radius-item);
  background-color: var(--pallete-color-black-2);
  border: var(--border-trace);
}

.domain__url:hover {
  background-color: var(--pallete-color-black-3);
}

</style>