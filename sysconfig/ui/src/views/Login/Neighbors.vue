<script>
import Button from "@/components/general/Button.vue";
import User from "@/views/Login/User.vue";
import Loading from "@/components/general/Loading.vue";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";

export default {
  name: "Neighbors",
  components: {Loading, User, Button},
  data() {
    return {
      loadingNeighbors: false,
      neighbors: []
    }
  },
  mounted() {
    this.loadNeighbors();
  },
  methods: {
    connectManually() {
      this.$emit("connectManually")
    },
    forgetNeighbors() {
      this.loadingNeighbors = true;
      Api.delete(EndPoints.NEIGHBORS).finally(() => {
        this.loadingNeighbors = false;
        this.neighbors = [];
      });
    },
    loadNeighbors() {
      this.loadingNeighbors = true;
      Api.get(EndPoints.NEIGHBORS).then((response) => {
        this.neighbors = response.data.data;
      }).finally(() => {
        this.loadingNeighbors = false;
      });
    }
  }
}
</script>

<template>
  <div class="neighbors">
    <div class="neighbors__header">
      <span class="text-lg">Enter on neighbor</span>
      <div class="flex gap-2 items-center">
        <Button text="Manually" @click="connectManually" color="var(--pallete-color-black-3)"/>
        <Button text="Forget all" @click="forgetNeighbors" color="var(--pallete-color-black-3)"/>
        <Button text="Search" @click="loadNeighbors" color="var(--pallete-color-black-3)"/>
      </div>
    </div>

    <div class="neighbors__list" v-if="neighbors.length > 0">
      <User
          v-for="(neighbor, index) in neighbors"
          :host="neighbor.ip" :name="neighbor.name"
          :key="index"
          @click="$emit('select', {displayName: neighbor.name, host: neighbor.ip})"/>
    </div>

    <Loading v-if="loadingNeighbors" class="my-auto"/>

    <span v-if="!loadingNeighbors && neighbors.length === 0" class="text-base text-aside my-auto">
      No neighbors around...
    </span>
  </div>
</template>

<style scoped>

.neighbors {
  min-width: 500px;
  @apply flex flex-col items-center w-full h-full
}

.neighbors__list {
  @apply flex flex-col gap-2 overflow-y-auto h-full w-full
}

.neighbors__header {
  @apply flex justify-between items-center p-6 w-full
}

</style>