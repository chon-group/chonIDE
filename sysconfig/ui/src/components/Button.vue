<template>
  <a v-if="link != null" :href="link != null ? link : ''" :target="link != null ? '_blank' : ''" class="button"
     :class="[skinClasses,'u-row u-align-i-center u-justify-i-center']">
    <Loading v-if="isLoading" ratio="14px" border-width="1px" aside-color="rgba(255,255,255,0.2)"
             main-color="white"/>
    <img v-else-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)" class="button__icon">
    <slot name="content"></slot>
  </a>
  <button v-else type="button" class="button" :class="[skinClasses,'u-row u-align-i-center u-justify-i-center']">
    <Loading v-if="isLoading" ratio="14px" border-width="1px" aside-color="rgba(255,255,255,0.2)"
             main-color="white"/>
    <img v-else-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)" class="button__icon">
    <slot name="content"></slot>
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
    mainColor: Boolean,
    color: {
      default: "var(--pallete-color-black-3)",
      required: false,
      type: String
    },
    noBorder: String,
    isLoading: Boolean,
    sidePadding: String,
    height: String,
    width: String,
    link: String
  },
  computed: {
    colorSytle() {
      if (this.mainColor) {
        return "var(--pallete-color-main-1)";
      }
      return this.color;
    },
    skinClasses() {
      let classes = "";
      if (this.height != null) {
        classes += " custom-height";
      }
      if (this.width != null) {
        classes += " custom-width";
      }

      if (this.sidePadding != null) {
        classes += " side-padding";
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

  background-color: v-bind(colorSytle);
  border-radius: var(--border-radius-item);
  font-size: var(--text-size-normal);
  color: var(--pallete-text-main);
  padding: 0 var(--action-side-padding);
  height: var(--action-height);
  position: relative;
}

.button.custom-height {
  height: v-bind(height);
  padding-top: 0;
  padding-bottom: 0;
}

.button.custom-width {
  width: v-bind(width);
  padding-left: 0;
  padding-right: 0;
}

.button::before {
  content: "";
  display: block;
  width: 100%;
  height: 100%;
  position: absolute;
}

.button:hover::before {
  background-color: rgba(255, 255, 255, 0.05);
}

.button.has-no-border {
  border-radius: 0;
}

.button__icon {
  width: v-bind(iconRatio);
  height: v-bind(iconRatio);
}

.button.side-padding {
  padding-left: v-bind(sidePadding);
  padding-right: v-bind(sidePadding);
}

</style>