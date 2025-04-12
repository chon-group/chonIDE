<template>
  <div class="login">
    <div class="login__logo">
      <h1 class="text-3xl">chonIDE</h1>
    </div>

    <div v-if="this.attributes != null" class="login__access">
      <div class="login__access__local-user">
        <div class="p-6">
          <span class="text-lg block" style="height: var(--action-height)">Enter on local user</span>
        </div>
        <div class="flex flex-col gap-2 overflow-y-auto h-full">
          <User
              v-for="(user, index) in attributes.users"
              :key="index"
              :name="user"
              host="localhost"
              @click="this.user = {displayName: user, name: user, host: 'localhost'}"/>
        </div>
      </div>

      <Neighbors @connectManually="user =
      {displayName: 'New Bot', name: null, password: '', host: null}" @select="this.user = $event"/>
    </div>

    <Loading v-else-if="this.attributes == null"/>

    <Popup v-if="user != null" :can-close="true" :show="true" width="350px" @closed="user = null">
      <template v-slot:content>
        <UserForm
            :display-name="this.user.displayName"
            :host="this.user.host"
            :name="this.user.name"

            @back="() => this.user = null"
            @message="$emit('message', $event)"
        />
      </template>
    </Popup>
  </div>
</template>

<script>
import GeneralUtil from "@/utils/generalUtil";
import Loading from "@/components/general/Loading.vue";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import User from "@/views/Login/User.vue";
import UserForm from "@/views/Login/UserForm.vue";
import Neighbors from "@/views/Login/Neighbors.vue";
import Popup from "@/components/general/Popup.vue";


export default {
  name: "Login",
  components: {Popup, Neighbors, UserForm, User, Loading},
  data() {
    return {
      attributes: null,
      configuration: null,
      loggingIn: false,
      user: null
    }
  },
  setup() {
    GeneralUtil.setTitle("Enter");
    Api.loadToken();
  },
  mounted() {
    Api.get(EndPoints.ATTRIBUTES).then((response) => {
      this.attributes = response.data.data;
    });
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

.login__access {
  background-color: var(--pallete-color-black-2);
  @apply flex h-0 grow rounded-md overflow-hidden shadow-2xl
}

.login__access__local-user {
  min-width: 500px;
  background-color: var(--pallete-color-black-3);
  @apply flex flex-col pb-0 rounded-md shadow-2xl
}
</style>