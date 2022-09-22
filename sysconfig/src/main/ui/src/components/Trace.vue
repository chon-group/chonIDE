<template>
  <div class="trace" :class="[state === 'entire' ? 'is-entire' : '', type == 'vertical' ? 'is-vertical' : '']"></div>
</template>

<script>
export default {
  name: "Trace",
  props: {
    state: String,
    type: String
  }
}
</script>

<style>
.trace {
  width: 100%;
  height: 1px;
  background-color: var(--pallete-color-black-3);
  border: none;
}

.trace.is-vertical {
  height: 100%;
  width: 1px;
}

.trace.is-entire::after {
  content: "";
  width: 100%;
  height: 1px;
  background-color: var(--pallete-color-black-3);
  border: none;
  position: absolute;
  left: 0;
}
</style>