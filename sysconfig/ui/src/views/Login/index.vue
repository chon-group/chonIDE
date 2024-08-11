<template>
  <div class="login">
    <div class="login__logo" v-if="user == null">
      <h1 class="text-3xl">chonIDE</h1>
      <p class="text-base text-aside text-center">
        IDE to development of<br>Embedded Multi-Agent Systems
      </p>
    </div>

    <div class="flex flex-col gap-6 items-center" v-if="user == null && this.attributes != null">
      <span class="text-base">Enter on user</span>

      <div class="flex flex-col gap-2">
        <User
            v-for="(user, index) in attributes.users"
            host="localhost"
            :name="user"
            :key="index"
            @click="this.user = {displayName: user, name: user, host: 'localhost'}"/>
      </div>

      <Neighbors @select="this.user = $event"/>
    </div>

    <Loading v-else-if="this.attributes == null"/>

    <div v-else-if="user != null" class="flex items-center justify-center h-full w-full">
      <UserForm
          :display-name="this.user.displayName"
          :host="this.user.host"
          :name="this.user.name"

          @message="$emit('message', $event)"
          @back="() => this.user = null"
      />
    </div>
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


export default {
  name: "Login",
  components: {Neighbors, UserForm, User, Loading},
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

.login__new-link {
  color: var(--pallete-text-main);
  border: var(--border-trace);
  @apply text-center rounded-sm w-full py-2.5;
}

.login__new-link:hover {
  background-color: var(--pallete-color-black-3);
}
</style>