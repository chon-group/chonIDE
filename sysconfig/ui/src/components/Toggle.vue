<template>
  <div class="toggle">
    carambolas
  </div>
</template>

<script>
export default {
  name: "Toggle",
  mounted() {
    this.$el.parentElement.addEventListener("click", (event) => {
      this.$el.classList.append("is-open");
    });
  }
}
</script>

<style scoped>

.toggle {
  display: none;
  position: absolute;
  border-radius: var(--border-radius-container);
  padding: var(--ratio-2);
  min-width: 300px;
  background-color: var(--pallete-color-black-2);
}

.toggle.is-open{
  display: flex;
}

</style>