<template>
  <div :class="['button-container', buttonContainerClasses]">
    <component :class="[buttonClasses,'button u-align-i-center u-justify-i-center u-all-cover']"
               :is="element" :href="link" target="_blank">
      <Loading v-if="isLoading" ratio="14px" border-width="1px" aside-color="rgba(255,255,255,0.2)"
               main-color="white"/>
      <img v-else-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)" class="button__icon">
      <slot name="content"></slot>
    </component>
  </div>
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
    iconSense: {
      default: "left"
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
    link: String,
    margin: {
      default: "0px"
    }
  },
  computed: {
    element() {
      return this.link != null ? "a" : "span";
    },
    colorSytle() {
      if (this.mainColor) {
        return "var(--pallete-color-main-1)";
      }
      return this.color;
    },
    buttonContainerClasses() {
      let classes = "";
      if (this.height != null) {
        classes += " custom-height";
      }
      if (this.width != null) {
        classes += " custom-width";
      }
      return classes;
    },
    buttonClasses() {
      let classes = "";
      if (this.iconSense == "left") {
        classes += "u-row";
      } else if (this.iconSense == "right") {
        classes += "u-row-reverse";
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

.button-container {
  padding: v-bind(margin);
}

.custom-height {
  height: v-bind(height);
}

.custom-width {
  width: v-bind(width);
}

.button {
  background-color: v-bind(colorSytle);
  border-radius: var(--border-radius-item);
  font-size: var(--text-size-normal);
  color: var(--pallete-text-main);
  padding: 0 var(--action-side-padding);
  height: var(--action-height);

  gap: 8px;
  border: none;
  cursor: default;
  width: fit-content;
  position: relative;
  overflow: hidden;
  white-space: nowrap;
}

.side-padding {
  padding-left: v-bind(sidePadding);
  padding-right: v-bind(sidePadding);
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

</style>