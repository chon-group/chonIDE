<template>
  <div :class="[state === 'entire' ? 'is-entire' : '', type == 'vertical' ? 'is-vertical' : '']" class="trace"></div>
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
  height: 1px;
  background-color: var(--pallete-color-black-3);
  @apply w-full border-none;
}

.trace.is-vertical {
  width: 1px;
  @apply h-full;
}

.trace.is-entire::after {
  content: "";
  height: 1px;
  background-color: var(--pallete-color-black-3);
  @apply w-full border-none absolute left-0;
}
</style>