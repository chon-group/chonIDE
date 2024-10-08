<template>
  <div ref="item"
       :class="['coder__explorer__file coder__explorer__item', this.selected ? 'selected' : '']"
       @click="showFile($event)">
    <div class="h-full w-full flex gap-2.5 items-center justify-between">
      <div class="flex items-center grow gap-2.5">
        <img v-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)" class="coder__explorer__item__icon"/>
        <input ref="filename" :value="file.name" class="coder__explorer__item__name w-full" readonly
               spellcheck="false" type="text" @change="editFile" @dblclick="editNameAction"/>
      </div>
      <button v-if="canRename || canDelete" ref="dotsButton" class="coder__explorer__action-button dots" tabindex="0">
        <Toggle parent-position>
          <template v-slot:options>
            <button v-if="canRename" @click="editNameAction">
              Rename
            </button>
            <button v-if="canDelete" class="severe">
              Delete
              <Popup ref="dots-delete-file" :title="`Delete ${file.name}`" is-children>
                <template v-slot:content>
                  Are you sure you want to delete {{ file.name }}?
                </template>
                <template v-slot:action>
                  <Button role="pop-up-closer">
                    <template v-slot:content>
                      Cancel
                    </template>
                  </Button>
                  <Button color="var(--pallete-color-red-1)" @click="deleteFile">
                    <template v-slot:content>
                      Yes, delete.
                    </template>
                  </Button>
                </template>
              </Popup>
            </button>
          </template>
        </Toggle>
      </button>
    </div>
    <Toggle v-if="canRename || canDelete" click-position type="contextmenu">
      <template v-slot:options>
        <button v-if="canRename" @click="editNameAction">
          Rename
        </button>
        <button v-if="canDelete" class="severe">
          Delete
          <Popup ref="delete-file" :title="`Delete ${file.name}`" is-children>
            <template v-slot:content>
              Are you sure you want to delete {{ file.name }}?
            </template>
            <template v-slot:action>
              <Button role="pop-up-closer">
                <template v-slot:content>
                  Cancel
                </template>
              </Button>
              <Button color="var(--pallete-color-red-1)" @click="deleteFile">
                <template v-slot:content>
                  Yes, delete.
                </template>
              </Button>
            </template>
          </Popup>
        </button>
      </template>
    </Toggle>
  </div>
</template>

<script>
import {Key} from "@/utils/enums";
import Popup from "@/components/general/Popup.vue";
import Button from "@/components/general/Button.vue";
import GeneralUtil from "@/utils/generalUtil";
import Toggle from "@/components/general/Toggle.vue";
import {removeRipple, useRipple} from "@/assets/js/effects/ripple";

export default {
  name: "ExplorerFile",
  components: {Toggle, Popup, Button},
  props: {
    file: {},
    icon: String,
    iconRatio: {
      default: "12px",
      type: String
    },
    level: String,
    canRename: {
      type: Boolean,
      default: true
    },
    canDelete: {
      type: Boolean,
      default: true
    },
    selected: Boolean
  },
  mounted() {
    this.$refs.filename.onkeyup = (event) => {
      if (event.key == Key.ENTER) {
        this.finishEditNameAction();
        return;
      }
      let formattedName = GeneralUtil.mantainJustRegularCharacters(this.$refs.filename.value);
      this.$refs.filename.value = formattedName.substring(0, 1).toLowerCase() + formattedName.substring(1);
    }
    this.$refs.filename.onblur = () => {
      this.finishEditNameAction();
    }

    if (this.$refs.dotsButton != null) {
      useRipple(this.$refs.dotsButton);
    }
  },
  beforeUnmount() {
    if (this.$refs.dotsButton != null) {
      removeRipple(this.$refs.dotsButton);
    }
  },
  methods: {
    deleteFile() {
      this.$emit("delete");
      this.$refs["delete-file"].close();
      this.$refs["dots-delete-file"].close();
    },
    showFile(event) {
      if (event.target !== this.$refs.dotsButton) {
        this.$emit("show");
      }
    },
    editFile() {
      let editedFile = this.file;
      editedFile.name = this.$refs.filename.value;
      this.emit("edit", editedFile);
    },
    editNameAction() {
      this.$refs.filename.focus();
      this.$refs.filename.readOnly = false;
      this.$refs.filename.setSelectionRange(
          this.$refs.filename.value.length,
          this.$refs.filename.value.length);
      this.$refs.item.classList.add("editing");
    },
    finishEditNameAction() {
      this.$refs.filename.readOnly = true;
      this.$refs.item.classList.remove("editing");
      this.$refs.filename.blur();
    }
  }
}
</script>

<style scoped>
@import "@/views/Project/leftbar/explorer/style.css";

.coder__explorer__item.selected {
  background-color: var(--pallete-color-black-3);
}

.coder__explorer__item__icon {
  width: v-bind(iconRatio);
  aspect-ratio: 1/1;
}

.coder__explorer__item__name {
  color: var(--pallete-text-main);
  @apply border-none rounded-sm px-1 py-0.5 cursor-default bg-transparent relative overflow-hidden whitespace-nowrap;
}

.coder__explorer__item.editing .coder__explorer__item__name {
  box-shadow: 0 0 0 1px var(--pallete-color-black-4);
}

.coder__explorer__action-button.dots {
  opacity: 0;
  background-image: url("@/assets/media/icon/vertical-dots.svg");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 5%;
  @apply mr-1;
}

.coder__explorer__file.coder__explorer__item:hover .coder__explorer__action-button.dots {
  opacity: 100%;
}

</style>