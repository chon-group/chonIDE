<script>
export default {
  name: "DeedTerms",
  props: {
    terms: {},
    separate: {
      default: true,
      type: Boolean
    }
  },
  methods: {
    getValueClasses(value) {
      if (typeof value === 'number') {
        return 'number'
      } else if (typeof value === 'string' && value.includes("\"")) {
        return 'string'
      } else if (value === undefined) {
        return 'undefined'
      }
    }
  }
}
</script>

<template>
  <div class="terms">
    <div v-for="(term, index) in terms" :key="index" class="flex gap-1.5">
      <div v-if="Array.isArray(term.value)">
        <div v-if="term.value.length === 0" class="flex gap-1.5">
          <span v-if="term.name" class="variable">{{ term.name }}:</span>
          <span>[]</span>
        </div>
        <div v-else class="flex flex-col gap-3">
          <div class="flex items-center gap-1.5 w-fit">
            <span v-if="term.name" class="variable">{{ term.name }}:</span>
            <span class="variable">{{ term.name && term.name.includes('(') ? '(' : '[' }}</span>
          </div>
          <DeedTerms v-if="term.value.length > 0" :terms="term.value" class="ml-4"/>
          <span class="variable">{{ term.name && term.name.includes('(') ? ')' : ']' }}</span>
        </div>

      </div>
      <span v-else class="flex gap-1.5">
        <span v-if="term.name" class="variable">{{ term.name }}:</span>
        <span :class="getValueClasses(term.value)" class="value">
          {{ term.value === undefined ? 'null' : term.value }}
        </span>
      </span>
      <span v-if="index < terms.length - 1" class="self-end">,</span>
    </div>
  </div>
</template>

<style scoped>

.terms {
  font-family: 'JetBrains Mono', monospace;
  font-size: 12px;
  @apply flex flex-col gap-4 flex-wrap
}

.variable {
  color: #c57e3b;
}

.value.string {
  color: #318527;
}

.value.undefined {
  color: #318527;
}

.value.number {
  color: #5086ea;
}

</style>