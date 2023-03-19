<template>
  <div :class="['button-container', buttonContainerClasses]">
    <component :class="[buttonClasses,'button items-center justify-center w-full h-full']"
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
    noBorder: Boolean,
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
        classes += "flex";
      } else if (this.iconSense == "right") {
        classes += "flex flex-row-reverse";
      }
      if (this.sidePadding != null) {
        classes += " side-padding";
      }
      if (this.noBorder) {
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
  height: var(--action-height);
}

.button {
  background-color: v-bind(colorSytle);
  font-size: var(--text-size-normal);
  color: var(--pallete-text-main);

  gap: 8px;
  @apply whitespace-nowrap overflow-hidden relative w-fit cursor-default border-none gap-2 rounded-sm py-0 px-3;
}

.custom-height {
  height: v-bind(height);
}

.custom-width {
  width: v-bind(width);
  padding: 0;
}

.side-padding {
  padding-left: v-bind(sidePadding);
  padding-right: v-bind(sidePadding);
}

.button::before {
  content: "";
  @apply absolute w-full h-full block;
}

.button:hover::before {
  background-color: rgba(255, 255, 255, 0.05);
}

.button.has-no-border {
  @apply border-none;
}

.button__icon {
  width: v-bind(iconRatio);
  height: v-bind(iconRatio);
}

</style>