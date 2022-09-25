<template>
  <button type="button" class="button" :class="[skinClasses, 'u-row u-align-i-center u-justify-i-center']">
    <div v-if="skin == null" class="button-background"></div>
    <Loading v-if="isLoading" ratio="16" border-width="3px" aside-color="rgba(255,255,255,0.2)"
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
    skin: String,
    icon: String,
    iconRatio: String,
    color: String,
    border: String,
    isLoading: Boolean
  },
  data() {
    return {
      iconRatioStyle: this.iconRatio != null ? this.iconRatio + "px" : "15px",
      skinClasses: this.mountSkinClasses(),
      colorStyle: this.color == null ? "var(--pallete-color-main-1)" : this.color
    }
  },
  methods: {
    mountSkinClasses() {
      let classes;
      if (this.skin === "transparent") {
        classes = "is-transparent"
      } else if (this.skin === "underlined") {
        classes = "is-underlined";
      } else if (this.skin === "navigation") {
        classes = "is-navigation";
      } else {
        classes = "is-filled";
      }

      if (this.icon != null) {
        if (this.$slots.content != null) {
          classes += " has-icon";
        } else {
          classes += " is-icon";
        }
      }

      if (this.border === 'rounded') {
        classes += " is-rounded";
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
}

.button__icon {
  width: v-bind(iconRatioStyle);
  height: v-bind(iconRatioStyle);
}

.button.is-filled {
  color: var(--pallete-text-main);
  background-color: v-bind(colorStyle);
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

</style>