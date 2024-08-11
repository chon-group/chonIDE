<template>
  <div :class="[readonly ?
    'ready-only' : '', type === 'file' ? 'is-file' : '']" class="flex items-center gap-2.5 input">
    <input v-if="type !== 'file'"
           ref="input"
           :name="name"
           :placeholder="placeholder"
           :readonly="readonly"
           :type="type"
           :value="modelValue"
           autocomplete="off" class="w-full" @input="updateValue">
    <div v-if="type === 'password' && hasValue" class="absolute right-1">
      <button v-if="!showingPassword" @click="showPassword" class="input__show-password">Show</button>
      <button v-else @click="hidePassword" class="input__show-password">Hide</button>
    </div>
    <label v-if="type === 'file'" class="input__file-label flex items-center justify-center">
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
      fileName: 'Importar arquivo...',
      showingPassword: false,
      hasValue: false
    }
  },
  mounted() {
    if (this.$refs.input.value.length > 0) {
      this.hasValue = true;
    }
  },
  computed: {
    align() {
      return this.center ? "center" : "inherit";
    }
  },
  methods: {
    updateValue(event) {
      let value = event.target.value;
      this.hasValue = value.length > 0;
      this.$emit('update:modelValue', value);
    },
    showPassword() {
      this.$refs.input.type = 'text'
      this.showingPassword = true;
    },
    hidePassword() {
      this.$refs.input.type = 'password'
      this.showingPassword = false;
    },
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
  background-color: var(--pallete-color-black-3);
  @apply w-full relative rounded-md;
}

.input > input {
  color: var(--pallete-text-main);
  text-align: v-bind(align);
  font-size: var(--text-size-normal);
  @apply border-none bg-transparent px-3;
}

.input.ready-only > input {
  @apply cursor-default;
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

.input__show-password {
  cursor: pointer;
  background: none;
  border: none;
  color: var(--pallete-text-aside);
  @apply py-0.5 px-1 rounded-md;
}

.input__show-password:hover {
  color: var(--pallete-text-main);
  background-color: var(--pallete-color-black-4);
}
</style>