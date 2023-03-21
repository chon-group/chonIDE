<template>
  <div class="coder__explorer__file coder__explorer__item" @click="showFile" ref="item">
    <div class="h-full w-full flex items-center gap-2.5">
      <span class="coder__explorer__item__icon">{{ icon }}</span>
      <input type="text" class="coder__explorer__item__name w-full" :value="file.name" @change="editFile"
             ref="filename"
             readonly/>
    </div>
    <Toggle type="contextmenu" click-position>
      <template v-slot:options>
        <button v-if="canRename" @click="editNameAction">
          Renomear
        </button>
        <button class="severe">
          Excluir
          <Popup :title="`Excluir ${file.name}`" is-children ref="delete-file">
            <template v-slot:content>
              Você tem certeza que deseja excluir {{ file.name }}?
            </template>
            <template v-slot:action>
              <Button role="pop-up-closer">
                <template v-slot:content>
                  Cancelar
                </template>
              </Button>
              <Button color="var(--pallete-color-red-1)" @click="deleteFile">
                <template v-slot:content>
                  Sim, excluir
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
import {Key} from "@/domain/Enums";
import Popup from "@/components/Popup";
import Button from "@/components/Button";
import Util from "@/domain/Util";
import Toggle from "@/components/Toggle";

export default {
  name: "ExplorerFile",
  components: {Toggle, Popup, Button},
  props: {
    file: {},
    icon: String,
    level: String,
    canRename: {
      type: Boolean,
      default: true
    }
  },
  mounted() {
    this.$refs.filename.onkeyup = (event) => {
      if (event.key == Key.ENTER) {
        this.finishEditNameAction();
        return;
      }
      let formattedName = Util.mantainJustRegularCharacters(this.$refs.filename.value);
      this.$refs.filename.value = formattedName.substring(0, 1).toLowerCase() + formattedName.substring(1);
    }
    this.$refs.filename.onblur = () => {
      this.finishEditNameAction();
    }
  },
  methods: {
    deleteFile() {
      this.$emit("delete");
      this.$refs["delete-file"].close();
    },
    showFile() {
      this.$emit("show");
    },
    editFile() {
      let editedFile = this.file;
      editedFile.name = this.$refs.filename.value;
      this.emit("edit", editedFile);
    },
    editNameAction() {
      this.$refs.filename.focus();
      this.$refs.filename.readOnly = false;
      this.$refs.filename.select();
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
@import "@/views/Project/style.css";

.coder__explorer__item__icon {
  font-weight: 1000;
  font-size: var(--text-size-little);
  color: var(--pallete-text-aside);
  width: 15px;
}

.coder__explorer__item__name {
  color: var(--pallete-text-main);
  @apply border-none cursor-default bg-transparent relative overflow-hidden whitespace-nowrap;
}

.coder__explorer__item.editing {
  background-color: var(--pallete-color-black-3);
}

</style>