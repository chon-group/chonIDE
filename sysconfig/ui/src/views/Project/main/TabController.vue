<script>
import Button from "@/components/Button.vue";
import Toggle from "@/components/Toggle.vue";
import {AppEvent, FileType, MessageType} from "@/utils/enums";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import Popup from "@/components/Popup.vue";

const UNKNOW_BOARD_MODEL_STRING = 'unknown:unknown:unknown';

export default {
  name: "TabController",
  computed: {
    FileType() {
      return FileType
    },
    isCurrentBoardUnknown() {
      if (this.board == null || this.board.fqbn == null) {
        return false;
      }
      return this.board.fqbn.toLowerCase().includes(UNKNOW_BOARD_MODEL_STRING);
    }
  },
  components: {Popup, Toggle, Button},
  props: {
    currentFile: {},
    currentFileType: {},
    currentBoard: {},
    configuration: {}
  },
  data() {
    return {
      compilingSketch: false,
      deployingSketch: false,
      boardResponse: null,
      board: null
    }
  },
  watch: {
    currentBoard(newValue) {
      this.board = newValue;
    }
  },
  methods: {
    compileSketch() {
      if (this.board == null || this.isCurrentBoardUnknown) {
        this.$refs.compileBoardUnknownPopup.showing(true);
        return;
      }

      this.compilingSketch = true;
      Api.post(EndPoints.SKETCH_COMPILE, {}, {
        boardName: this.board.fqbn,
        code: this.currentFile.sourceCode
      }).then((response) => {
        this.boardResponse = response.data.data;
        this.$refs.boardResponse.showing(true);
        this.$emit(AppEvent.MESSAGE, {content: 'Sketch compiled', type: MessageType.SUCCESS});
      }).finally(() => {
        this.compilingSketch = false;
      });
    },
    deploySketch() {
      if (this.board == null) {
        this.$emit(AppEvent.MESSAGE, {content: "There is no board selected", type: MessageType.WARNING});
        return;
      }

      if (this.isCurrentBoardUnknown) {
        this.$refs.deployBoardUnknownPopup.showing(true);
        return;
      }

      this.deployingSketch = true;
      Api.post(EndPoints.SKETCH_DEPLOY, {
        params: {
          serialPort: this.board.port,
          boardName: this.board.fqbn
        }
      }).then(() => {
        this.$emit(AppEvent.MESSAGE, {content: 'Sketch deployed', type: MessageType.SUCCESS});
      }).catch(() => {
        this.$emit(AppEvent.MESSAGE, {content: 'ERROR: Sketch was not deployed', type: MessageType.ERROR});
      }).finally(() => {
        this.deployingSketch = false;
      });
    },
    performBoardOperationWithDifferentFqbn(fqbn, operation, popUpToClose) {
      popUpToClose.showing(false);
      if (this.board == null) {
        this.board = {
          fqbn: fqbn
        }
      } else {
        this.board.fqbn = fqbn;
      }

      this.$refs.deployBoardUnknownPopup.showing(false);
      operation();

      if (this.board.name == null) {
        this.board = null;
      } else {
        this.board.fqbn = UNKNOW_BOARD_MODEL_STRING;
      }
    }
  }
}
</script>

<template>
  <div class="tab-controller">

    <Popup ref="deployBoardUnknownPopup" can-close title="Choose board model">
      <template v-slot:content>
        <p v-if="this.isCurrentBoardUnknown">The Arduino board model cannot be identified.</p>
        <p> Perform deploy as...</p>
      </template>
      <template v-slot:action>
        <div class="flex gap-2 flex-col w-full">
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:uno', this.deploySketch,
                            $refs.deployBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Uno
            </template>
          </Button>
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:mega', this.deploySketch,
                            $refs.deployBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Mega
            </template>
          </Button>
        </div>
      </template>
    </Popup>

    <Popup ref="compileBoardUnknownPopup" can-close title="Choose board model">
      <template v-slot:content>
        <p v-if="this.isCurrentBoardUnknown">The Arduino board model cannot be identified.</p>
        <p> Perform compile as...</p>
      </template>
      <template v-slot:action>
        <div class="flex gap-2 flex-col w-full">
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:uno', this.compileSketch,
                            $refs.compileBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Uno
            </template>
          </Button>
          <Button width-full
                  @click="this.performBoardOperationWithDifferentFqbn('arduino:avr:mega', this.compileSketch,
                            $refs.compileBoardUnknownPopup)">
            <template v-slot:content>
              Arduino Mega
            </template>
          </Button>
        </div>
      </template>
    </Popup>

    <Popup ref="boardResponse" can-close title="Board response" width="var(--container-width-1)">
      <template v-slot:content>
        <div class="project__compiled-response">
          {{ boardResponse }}
        </div>
      </template>
    </Popup>

    <div class="flex w-full h-full">
      <div class="tab-controller__file" type="text">
        <div class="tab-controller__selected-file"></div>
        <span class="tab-controller__file__name">
                  {{ currentFile.name }}
                </span>
        <div v-if="currentFile.name !== 'No file'" class="project-action-separator"></div>

        <Button v-if="currentFileType === FileType.AGENT" icon="toggle.svg" icon-ratio="8px" icon-sense="right">
          <template v-slot:content>
            {{ this.currentFile.archClass }}
            <Toggle :selected="this.currentFile.archClass" parent-position
                    select
                    @select="(selected) => $emit('changeArchClass', selected)">
              <template v-slot:options>
                <button v-for="(agentType, index) in this.configuration.agentTypes"
                        :key="index">
                  {{ agentType }}
                </button>
              </template>
            </Toggle>
          </template>
        </Button>
        <div v-if="currentFileType === FileType.FIRMWARE" class="flex">
          <Button :is-loading="compilingSketch" icon="check.svg" icon-ratio="11px" text="Compile"
                  @click="compileSketch"/>
          <Button v-if="currentBoard != null" :is-loading="deployingSketch" icon="upload.svg" icon-ratio="11px"
                  text="Deploy" @click="deploySketch"/>
        </div>
      </div>
      <div class="tab-controller-background">

      </div>
      <div class="tab-controller__right">

      </div>
    </div>
  </div>
</template>

<style scoped>
@import "@/views/Project/style.css";

.tab-controller {
  height: var(--bar-height);
  @apply flex items-center justify-between relative w-full;
}

.tab-controller-background {
  @apply absolute h-full w-full;
}

.tab-controller-background::before {
  content: "";
  display: block;
  background-color: var(--pallete-color-black-1);
  height: 50%;
  bottom: 0;
  width: 100%;
}

.tab-controller-background::after {
  content: "";
  display: block;
  background-color: var(--pallete-color-black-2);
  top: 0;
  height: 50%;
  width: 100%;
}

.tab-controller__right {
  background-color: var(--pallete-color-black-1);
  border-bottom-left-radius: 10px;
  z-index: 2;
  @apply w-full h-full;
}

.tab-controller__file {
  background-color: var(--pallete-color-black-2);
  border-top-right-radius: 10px;
  z-index: 2;
  @apply flex h-full items-center p-1.5 pl-2.5 gap-1.5;
}

.tab-controller__selected-file {
  width: 8px;
  height: 8px;
  background-color: var(--pallete-color-main-1);
  @apply rounded-full mr-1.5;
}

.tab-controller__file__name {
  max-width: 125px;
  color: var(--pallete-text-main);
  text-overflow: ellipsis;
  @apply overflow-hidden whitespace-nowrap shrink-0 mr-2.5 select-none;
}

.project__compiled-response {
  background-color: var(--pallete-color-black-3);
  word-break: break-word;
  @apply p-2.5 rounded-sm text-xl;
}

</style>