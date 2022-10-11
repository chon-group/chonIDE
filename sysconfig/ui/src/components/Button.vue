<template>
  <a v-if="link != null" :href="link != null ? link : ''" :target="link != null ? '_blank' : ''" class="button"
     :class="[skinClasses,'u-row u-align-i-center u-justify-i-center']">
    <div v-if="transparent == null && navigation == null" class="button-background"></div>
    <Loading v-if="isLoading" ratio="14" border-width="1" aside-color="rgba(255,255,255,0.2)"
             main-color="white"/>
    <img v-else-if="icon != null" :src="require(`@/assets/media/icon/${icon}`)" class="button__icon">
    <span v-if="$slots.content != null">
      <slot name="content"></slot>
    </span>
  </a>
  <button type="button" v-else class="button" :class="[skinClasses,'u-row u-align-i-center u-justify-i-center']">
    <div v-if="transparent == null && navigation == null" class="button-background"></div>
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
    transparent: String,
    underlined: String,
    navigation: String,
    icon: String,
    iconRatio: String,
    color: String,
    rounded: String,
    noBorder: String,
    isLoading: Boolean,
    adjust: String,
    sidePadding: String,
    link: String
  },
  data() {
    return {
      iconRatioStyle: this.iconRatio != null ? this.iconRatio + "px" : "15px",
      skinClasses: this.mountSkinClasses(),
      colorStyle: this.color == null ? "var(--pallete-color-main-1)" : this.color,
      sidePaddingStyle: this.sidePadding == null ? 'initial' : this.sidePadding + 'px'
    }
  },
  methods: {
    mountSkinClasses() {
      let classes;
      if (this.transparent != null) {
        classes = "is-transparent"
      } else if (this.underlined != null) {
        classes = "is-underlined";
      } else if (this.navigation != null) {
        classes = "is-navigation";
      } else {
        classes = "is-filled";
      }

      if (this.adjust != null) {
        classes += " is-adjust";
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

      if (this.border != null) {
        classes += " is-rounded";
      } else if (this.noBorder != null) {
        classes += " has-not-border"
      }

      return classes;
    }
  }
}
</script>

<style scoped>
.button {
  gap: 8px;
  border-radius: var(--border-radius-item);
  border: none;
  cursor: pointer;
  width: fit-content;
  position: relative;
  overflow: hidden;
  background-color: v-bind(colorStyle);
  white-space: nowrap;
}

.button.has-not-border {
  border-radius: 0;
}

.button__icon {
  width: v-bind(iconRatioStyle);
  height: v-bind(iconRatioStyle);
}

.button.is-filled {
  color: var(--pallete-text-main);
  height: var(--action-ratio-1);
  padding: var(--action-padding-1);
}

.button.is-underlined {
  background-color: transparent;
  color: var(--pallete-text-main);
  text-decoration: underline;
}

.button.is-transparent {
  background-color: transparent;
  color: var(--pallete-text-main);
  height: var(--action-ratio-1);
  padding: var(--action-padding-1);
}

.button.is-transparent:hover {
  background-color: var(--pallete-color-black-3);
}

.button.is-navigation {
  height: var(--action-ratio-1);
  padding: 0 var(--ratio-3);
  background-color: transparent;
  color: var(--pallete-text-main);
  border-bottom: 1px solid var(--pallete-color-black-1);
  border-radius: 0;
}

.button.is-navigation:hover {
  border-bottom: 1px solid var(--pallete-text-main);
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

.button.is-underlined:hover {
  font-weight: 600;
}

.button.is-rounded {
  border-radius: var(--border-radius-total);
}

.button.is-icon {
  padding: 0;
  height: var(--action-ratio-1);
  width: var(--action-ratio-1);
}

.button.is-adjust {
  height: 100%;
}

.button.side-padding {
  padding-left: v-bind(sidePaddingStyle);
  padding-right: v-bind(sidePaddingStyle);
}

</style>