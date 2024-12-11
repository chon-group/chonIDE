<script>
export default {
  name: "BeliefTerms",
  props: {
    terms: {},
    wrap: Boolean
  }
}
</script>

<template>
  <div class="terms">
    <span v-if="wrap">(</span>
    <div v-for="(term, index) in terms" :key="index" class="flex items-center">
      <div v-if="Array.isArray(term.value)" class="flex items-center">
        <span>{{ term.name ? '[' : '(' }}</span>
        <BeliefTerms :terms="term.value"/>
        <span>{{ term.name ? '] ' : ')' }}</span>
      </div>
      <span v-else class="flex items-center">
        <span>{{ term.value }}</span>
      </span>
      <span v-if="index < terms.length - 1">,</span>
    </div>
    <span v-if="wrap">)</span>
  </div>
</template>

<style scoped>

.terms {
  font-family: 'JetBrains Mono', monospace;
  font-size: 12px;
  @apply flex items-center flex-wrap
}

</style>