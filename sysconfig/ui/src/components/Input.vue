<template>
  <div :class="[readonly ===
    'true' ?
    'ready-only' : '', type === 'file' ? 'is-file' : '']" class="flex gap-2.5 input">
    <input v-if="type !== 'file'" ref="input" :name="name" :placeholder="placeholder"
           :readonly="readonly"
           :type="type"
           :value="modelValue"
           autocomplete="off" class="w-full" @input="$emit('update:modelValue',
      $event.target.value)">
    <label v-else class="input__file-label flex items-center justify-center">
      <input ref="input" :accept="accept" :name="name" type="file" @change="mountFileName($event)">
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