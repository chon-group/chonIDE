<script>
export default {
    name: "Agent",
    props: {
        agent: {}
    }
}
</script>

<template>
 <div class="agent">
     <span class="agent__name">{{agent.name}}</span>
     <div class="agent__content">
         <span class="agent__content__title">Beliefs</span>
         <div class="agent__content__items" v-if="agent.beliefs.length > 0">
             <span class="agent__content__item" v-for="(belief, index) in agent.beliefs" :key="index">
                 {{belief}}
             </span>
         </div>
         <span v-else class="agent__content__item text-aside">No beliefs</span>
     </div>


     <div class="agent__content">
         <span class="agent__content__title">Intentions</span>
         <div class="agent__content__items"  v-if="agent.intentions.length > 0">
             <span class="agent__content__item" v-for="(intention, index) in agent.intentions" :key="index">
                 {{intention}}
             </span>
         </div>
         <span v-else class="agent__content__item text-aside">No intentions</span>
     </div>
     <div class="agent__content">
         <span class="agent__content__title">Plans</span>
         <div class="agent__content__items" v-if="agent.plans.length > 0">
             <span class="agent__content__item" v-for="(plan, index) in agent.plans" :key="index">
                 {{plan.name}}
             </span>
         </div>
         <span v-else class="no-content">No plans</span>
     </div>
 </div>
</template>

<style scoped>

.agent {
    border-bottom: 1px solid var(--pallete-color-black-3);
    @apply flex flex-col gap-3 p-4;
}

.agent__name {
    @apply text-xl mb-2;
}

.agent__content {
    @apply flex flex-col gap-2;
}

.agent__content__title {
}

.agent__content__items {
    @apply flex flex-col gap-2 items-start;
}

.agent__content__item {
    background-color: var(--pallete-color-black-2);
    font-family: 'JetBrains Mono', monospace;
    @apply py-1 px-1.5 rounded-md ml-4 w-fit break-all;
}

.no-content {
    @layer agent__content__item text-aside;
}

</style>