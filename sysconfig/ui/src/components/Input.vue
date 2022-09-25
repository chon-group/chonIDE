<template>
  <div class="u-row u-gap-3 input" :class="[readonly ===
    'true' ?
    'ready-only' : '', type === 'file' ? 'is-file' : '']">
    <input v-if="type !== 'file'" :readonly="readonly" :value="modelValue" @input="$emit('update:modelValue',
      $event.target.value)"
           :type="type"
           :name="name"
           :placeholder="placeholder"
           class="u-cover" ref="input" autocomplete="off">
    <label v-else class="input__file-label u-total-center">
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
    type: String,
    placeholder: String,
    readonly: String,
    modelValue: String,
    accept: String
  },
  data() {
    return {
      fileName: 'Importar arquivo...'
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
  width: 100%;
  padding: var(--action-padding-1);
  height: var(--action-ratio-1);
  border: 1px solid var(--pallete-color-black-3);
  background-color: var(--pallete-color-black-1);
  border-radius: var(--border-radius-item);
  position: relative;
}

.input > input {
  border: none;
  background: transparent;
  color: var(--pallete-text-main);
}

.input.ready-only > input {
  cursor: default;
}

.input.ready-only {
  background-color: var(--pallete-color-black-2);
}

.input__show {
  position: absolute;
  right: 0;
  background-color: transparent;
  padding: var(--ratio-4);
  color: var(--pallete-text-main);
  border: none;
  border-radius: var(--border-radius-item);
  cursor: pointer;
  top: 50%;
  transform: translateY(-50%);
  margin-right: var(--ratio-4);
}

.input__show:hover {
  background-color: var(--pallete-color-black-3);
}

.input__file-label > input {
  display: none;
}

.input__file-label {
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.input__file-label__fileName {
  color: var(--pallete-text-aside);
}
</style>