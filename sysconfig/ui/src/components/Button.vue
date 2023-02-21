<template>
  <a v-if="link != null" :href="link != null ? link : ''" :target="link != null ? '_blank' : ''" class="button"
     :class="[skinClasses,'u-row u-align-i-center u-justify-i-center']">
    <div class="button-background"></div>
    <Loading v-if="isLoading" ratio="14" border-width="1" aside-color="rgba(255,255,255,0.2)"
             main-color="white"/>
    <img v-else-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)" class="button__icon">
    <span v-if="$slots.content != null">
      <slot name="content"></slot>
    </span>
  </a>
  <button type="button" v-else class="button" :class="[skinClasses,'u-row u-align-i-center u-justify-i-center']">
    <div class="button-background"></div>
    <Loading v-if="isLoading" ratio="14" border-width="1" aside-color="rgba(255,255,255,0.2)"
             main-color="white"/>
    <img v-else-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)" class="button__icon">
    <span v-if="$slots.content != null">
      <slot name="content"></slot>
    </span>
  </button>
</template>

<script>
import Loading from "@/components/Loading";

export default {
  name: 'Button',
  components: {Loading},
  props: {
    icon: String,
    iconRatio: {
      default: "15px",
      type: String,
      required: false
    },
    color: {
      default: "var(--pallete-color-black-2)",
      required: false,
      type: String
    },
    noBorder: String,
    isLoading: Boolean,
    heightAdjust: String,
    sidePadding: String,
    link: String
  },
  computed: {
    skinClasses() {
      let classes = "";
      if (this.heightAdjust != null) {
        classes += " is-height-adjust";
      }
      if (this.sidePadding != null) {
        classes += " side-padding";
      }
      if (this.icon != null) {
        if (this.$slots.content != null) {
          classes += " has-icon";
        } else {
          classes += " is-icon";
        }
      }
      if (this.noBorder != null) {
        classes += " has-no-border"
      }
      return classes;
    }
  }
}
</script>

<style scoped>
.button {
  gap: 8px;
  border: none;
  cursor: default;
  width: fit-content;
  position: relative;
  overflow: hidden;
  white-space: nowrap;

  background-color: v-bind(color);
  border-radius: var(--border-radius-item);
  font-size: var(--text-size-normal);
  color: var(--pallete-text-main);
  padding: 0 var(--action-side-padding);
  height: var(--action-height);
}

.button.has-no-border {
  border-radius: 0;
}

.button__icon {
  width: v-bind(iconRatio);
  height: v-bind(iconRatio);
}

.button.is-icon {
  padding: 0;
  height: var(--action-height);
  width: var(--action-height);
}

.button.is-height-adjust {
  height: 100%;
}

.button.side-padding {
  padding-left: v-bind(sidePadding);
  padding-right: v-bind(sidePadding);
}

.button-background {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}

.button-background:hover {
  background-color: rgba(255, 255, 255, 0.08);
}

</style>