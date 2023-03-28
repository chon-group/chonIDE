<template>
  <div class="flex gap-2.5 input" :class="[readonly ===
    'true' ?
    'ready-only' : '', type === 'file' ? 'is-file' : '']">
    <input v-if="type !== 'file'" :readonly="readonly" :value="modelValue" @input="$emit('update:modelValue',
      $event.target.value)"
           :type="type"
           :name="name"
           :placeholder="placeholder"
           class="w-full" ref="input" autocomplete="off">
    <label v-else class="input__file-label flex items-center justify-center">
      <input type="file" :name="name" @change="mountFileName($event)" ref="input" :accept="accept">
      <span class="input__file-label__fileName">{{ fileName }}</span>
    </label>
  </div>
</template>

<script>

export default {
  name: "Input",
  props: {
    name: String,
    type: {
      default: "text"
    },
    placeholder: String,
    readonly: {
      default: false
    },
    modelValue: String,
    accept: String,
    center: Boolean
  },
  data() {
    return {
      fileName: 'Importar arquivo...'
    }
  },
  computed: {
    align() {
      return this.center ? "center" : "inherit";
    }
  },
  methods: {
    mountFileName(event) {
      let value = event.target.value;
      if (value.length === 0) {
        this.fileName = "Importar arquivo...";
      } else {
        this.fileName = value.replace("C:\\fakepath\\", "");
      }
    }
  }
}
</script>

<style scoped>
.input {
  height: var(--action-height);
  border: 1px solid var(--pallete-color-black-3);
  background-color: var(--pallete-color-black-1);
  @apply w-full relative rounded-lg text-base;
}

.input > input {
  color: var(--pallete-text-main);
  text-align: v-bind(align);
  @apply border-none bg-transparent px-3;
}

.input.ready-only > input {
  @apply cursor-default;
}

.input.ready-only {
  background-color: var(--pallete-color-black-2);
}

.input__file-label {
  @apply cursor-pointer h-full w-full;
}

.input__file-label > input {
  display: none;
}

.input__file-label__fileName {
  color: var(--pallete-text-aside);
}
</style>