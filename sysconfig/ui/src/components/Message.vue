<template>
  <div class="message flex items-center justify-center drop-shadow-sm p-2.5" :class="mountResponseClasses()">
    {{ message.content }}
  </div>
</template>

<script>
import {MessageType} from "@/domain/Enums";

export default {
  name: "Message",
  props: {
    message: Object
  },
  methods: {
    mountResponseClasses() {
      let classes;
      if (this.message.type === MessageType.ERROR) {
        classes = "is-error";
      } else if (this.message.type === MessageType.SUCCESS) {
        classes = "is-success";
      } else if (this.message.type === MessageType.WARNING) {
        classes = "is-warning";
      } else {
        classes = "is-normal";
      }
      return classes;
    }
  },
  mounted() {
    setTimeout(() => {
      this.$el.remove();
    }, this.message.timeout);
  }
}
</script>

<style scoped>

.message {
  font-weight: 600;
  @apply relative whitespace-nowrap rounded-sm;
}

.message.is-error {
  background-color: var(--pallete-color-red-1);
  border: 1px solid var(--pallete-color-red-2)
}

.message.is-success {
  background-color: var(--pallete-color-green-1);
  border: 1px solid var(--pallete-color-green-2)
}

.message.is-warning {
  background-color: var(--pallete-color-yellow-1);
  border: 1px solid var(--pallete-color-yellow-2);
}

.message.is-normal {
  background-color: var(--pallete-color-black-2);
  border: var(--border-trace);
}

</style>