<template>
  <component :class="[buttonClasses,'button items-center justify-center']"
             :is="element" :href="link" target="_blank" draggable="false">
    <Loading v-if="isLoading" ratio="14px" border-width="1px" aside-color="rgba(255,255,255,0.2)"
             main-color="white"/>
    <img v-else-if="icon != null" :src="iconUrl" class="button__icon">
    <span v-if="this.$slots.content == null && this.text != null">{{text}}</span>
    <slot name="content"></slot>
  </component>
</template>

<script>
import Loading from "@/components/Loading";
import {removeRipple, useRipple} from "@/composable/Ripple";

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
    text: String,
    mainColor: Boolean,
    color: {
      default: "transparent",
      required: false,
      type: String
    },
    noBorder: Boolean,
    isLoading: Boolean,
    height: String,
    link: String,
    widthFull: {
      default: false,
      type: Boolean
    }
  },
  mounted() {
    useRipple(this.$el);
  },
  beforeUnmount() {
    removeRipple(this.$el);
  },
  methods: {
    hasNotText() {
      return this.$slots.content == null && this.text == null;
    },
  },
  computed: {
    iconUrl() {
      return require(`@/assets/media/icon/${this.icon}`);
    },
    element() {
      return this.link != null ? "a" : "span";
    },
    colorSytle() {
      if (this.mainColor) {
        return "var(--pallete-color-main-1)";
      }
      return this.color;
    },
    padding() {
      return this.hasNotText() && this.icon != null ? "0px" : "12px";
    },
    width() {
      if (this.widthFull) {
        return "100%";
      }
      return this.hasNotText() && this.icon != null ? "32px" : "fit-content";
    },
    buttonClasses() {
      let classes = "";
      if (this.iconSense == "left") {
        classes += "flex";
      } else if (this.iconSense == "right") {
        classes += "flex flex-row-reverse";
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

.button {
  background-color: v-bind(colorSytle);
  font-size: var(--text-size-normal);
  color: var(--pallete-text-main);
  padding: 0 v-bind(padding);
  gap: 8px;
  height: var(--action-height);
  width: v-bind(width);
  @apply whitespace-nowrap overflow-hidden relative cursor-pointer border-none gap-2 rounded-md select-none shrink-0;
}

.button::before {
  content: "";
  transition: background-color 0.1s;
  @apply absolute w-full h-full block;
}

.button:hover::before {
  background-color: rgba(255, 255, 255, 0.07);
}

.button.has-no-border {
  border-radius: 0;
}

.button__icon {
  width: v-bind(iconRatio);
  height: v-bind(iconRatio);
}

</style>