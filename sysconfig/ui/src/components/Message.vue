<template>
  <div class="message u-total-center" :class="mountResponseClasses()">
    {{ message.content }}
  </div>
</template>

<script>
import {MessageType} from "@/assets/js/model/Enums";

export default {
  name: "Message",
  props: {
    message: Object
  },
  methods: {
    mountResponseClasses() {
      let classes;
      console.log(MessageType.ERROR);
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
  border-radius: var(--border-radius-item);
  filter: drop-shadow(var(--shadow-item-1));
  font-weight: 600;
  white-space: nowrap;
  padding: var(--ratio-3) var(--ratio-3);
  position: relative;
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