<template>
  <div class="u-total-center page">
    <div class="response u-column u-gap-3" ref="response">
      <Message v-for="(message, index) in messages" :key="index" :message="message"/>
    </div>
    <router-view/>
  </div>
</template>

<script>
import Message from "@/components/Message";

export default {
  name: "App",
  components: {Message},
  data() {
    return {
      messages: [],
    }
  },
  methods: {
    message(message) {
      if(this.messages.length >= 6){
        this.messages.splice(5, 1);
        return;
      }
      if (message.timeout == null) {
        message.timeout = 2000;
      }
      let hasSameMessageInMemory = false;
      let messageInMemoryIndex = 0;
      this.messages.forEach((messageInMemory, index) => {
        if (messageInMemory.content === message.content) {
          messageInMemoryIndex = index;
          hasSameMessageInMemory = true;
          return;
        }
      });
      if (hasSameMessageInMemory) {
        this.messages.splice(messageInMemoryIndex, 1);
        setTimeout(()=>{
          this.messages.push(message);
        }, 1);
      } else {
        this.messages.push(message);
      }
    }
  }
}
</script>

<style>
@import "@/assets/css/import.css";

@font-face {
  src: url("@/assets/fonts/nunito-sans/NunitoSans-Regular.ttf");
  font-family: 'Nunito Sans';
}

.response {
  z-index: 999;
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  top: var(--ratio-2);
}

.page {
  background-color: var(--pallete-color-black-1);
}
</style>
