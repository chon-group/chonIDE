<script>
import Button from "@/components/general/Button.vue";
import Popup from "@/components/general/Popup.vue";

export default {
  name: "RebootPopup",
  components: {Popup, Button},
  props: {
    now: {
      default: false,
      type: Boolean
    }
  },
  emits: ['act'],
  methods: {
    close() {
      this.$refs.popup.close();
    },
    show() {
      this.$refs.popup.showing(true);
    }
  }
}
</script>

<template>
  <Popup title="Reboot system" can-close ref="popup">
    <template v-slot:content>
      <p>It is an operation that <span v-if="!now">may</span> requires a bot restart, do you want to <span v-if="!now">restart now or later?</span> <span v-else>continue?</span></p>
    </template>
    <template v-slot:action>
      <Button
          text="Restart now"
          color="var(--pallete-color-black-3)"
          :main-color="now"

          @click="$emit('act', {reboot: true})"
      />
      <Button
          v-if="!now"

          text="Restart later"
          main-color

          @click="$emit('act', {reboot: false})"
      />
    </template>
  </Popup>
</template>