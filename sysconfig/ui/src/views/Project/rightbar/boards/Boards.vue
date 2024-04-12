<script>
import Button from "@/components/Button.vue";
import Loading from "@/components/Loading.vue";
import {API, EndPoints} from "@/domain/API";
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
            API.get(EndPoints.BOARDS, refresh).then((response) => {
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
        <div class="flex items-center justify-center h-full w-full" v-if="loadingBoards">
            <Loading border-width="2px" main-color="var(--pallete-text-main)" ratio="25px"/>
        </div>
        <div class="flex items-center justify-center h-full w-full" v-else-if="boards.length === 0 && !loadingBoards">
            <span class="text-aside">No available boards found</span>
        </div>
        <div v-else class="flex flex-col gap-1.5 p-1.5">
            <Board
                    v-for="(board, index) in boards" :key="index"

                    :is-current="currentBoard === board"
                    :board="board"

                    @select="() => {
                        currentBoard = currentBoard === board ? null : board
                    }"
                    @click="$emit('selectedBoard', board)"/>
        </div>
    </div>
</template>

<style scoped>
@import "@/views/Project/style.css";

.boards {
    background-color: var(--pallete-color-black-1);
    border-left: 1px solid var(--pallete-color-black-4);
    @apply flex flex-col overflow-y-auto h-full w-full;
}
</style>