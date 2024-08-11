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
    forgetNeighbors(){
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
      <span class="text-base">Your neighbors</span>
      <div class="flex gap-2 items-center">
        <Button text="Forget all" @click="forgetNeighbors" color="var(--pallete-color-black-2)"/>
        <Button text="Search" @click="loadNeighbors" color="var(--pallete-color-black-2)"/>
      </div>
    </div>

    <User
        v-for="(neighbor, index) in neighbors"
        :host="neighbor.ip" :name="neighbor.name"
        :key="index"
        @click="$emit('select', {displayName: neighbor.name, host: neighbor.ip})"/>

    <Loading v-if="loadingNeighbors"/>
    <span v-if="!loadingNeighbors && neighbors.length === 0" class="text-base text-aside">No neighbors around...</span>
  </div>
</template>

<style scoped>

.neighbors {
  height: 50vh;
  @apply flex flex-col gap-3 items-center w-full
}

.neighbors__header {
  @apply flex justify-between items-center w-full
}

</style>