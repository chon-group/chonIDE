<script>
import Button from "@/components/Button.vue";
import Loading from "@/components/Loading.vue";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import Board from "@/views/Project/rightbar/boards/Board.vue";

export default {
  name: "Boards",
  components: {Board, Loading, Button},
  data() {
    return {
      loadingBoards: false,
      boards: [],
      currentBoard: {}
    }
  },
  mounted() {
    this.loadBoards(true);
  },
  methods: {
    loadBoards(refresh = false) {
      this.currentBoard = null;
      this.loadingBoards = true;
      Api.get(EndPoints.BOARDS, refresh).then((response) => {
        this.boards = response.data.data;
        if (this.boards.length !== 0) {
          this.currentBoard = this.boards[0];
        }
      }).finally(() => {
        this.loadingBoards = false;
      });
    },
  }
}
</script>

<template>
  <div class="boards">
    <div class="project__header-bar">
      <span class="project__header-bar__title">Boards available</span>
      <Button icon="refresh.svg" icon-ratio="13px" side-padding="12px" @click="loadBoards(true)"/>
    </div>
    <div v-if="loadingBoards" class="flex items-center justify-center h-full w-full">
      <Loading  ratio="25px"/>
    </div>
    <div v-else-if="boards.length === 0 && !loadingBoards" class="flex items-center justify-center h-full w-full">
      <span class="text-aside">No available boards found</span>
    </div>
    <div v-else class="flex flex-col gap-1.5 p-1.5">
      <Board
          v-for="(board, index) in boards" :key="index"

          :board="board"
          :is-current="currentBoard === board"

          @click="$emit('selectedBoard', board)"
          @select="() => {
                        currentBoard = currentBoard === board ? null : board
                    }"/>
    </div>
  </div>
</template>

<style scoped>
@import "@/views/Project/style.css";

.boards {
  background-color: var(--pallete-color-black-1);
  @apply flex flex-col overflow-y-auto h-full w-full;
}
</style>