<script>
export default {
    name: "Agent",
    props: {
        agent: {}
    },
    computed: {
        ContextContentLengthToWrapper() {
            return 20;
        }
    },
    methods: {
        clickPlan(event, agentName, beginLine, endLine) {
            let element = event.target;
            const elementClassList = element.classList;

            if (elementClassList.contains("agent__plan__show") && elementClassList.contains("body")) {
                element.style.display = "none";
                element.parentElement.querySelector(".agent__plan__body__content").style.removeProperty("display");
            } else if (elementClassList.contains("agent__plan__body__content") || elementClassList.contains("agent__plan__body__content__line")) {
                if (elementClassList.contains("agent__plan__body__content")) {
                    element.parentElement.querySelector(".agent__plan__show").style.removeProperty("display");
                    element.style.display = "none";
                } else {
                    element.parentElement.parentElement.querySelector(".agent__plan__show").style.removeProperty("display");
                    element.parentElement.style.display = "none";
                }
            } else if (elementClassList.contains("agent__plan__show") && elementClassList.contains("context")) {
                element.style.display = "none";
                element.parentElement.querySelector(".agent__plan__context__content").style.removeProperty("display");
            } else if (elementClassList.contains("agent__plan__context__content")) {
                element.parentElement.querySelector(".agent__plan__show").style.removeProperty("display");
                element.style.display = "none";
            } else {
                this.highlightAgentFile(agentName, beginLine, endLine);
            }
        },
        highlightAgentFile(agentName, beginLine, endLine) {
            this.$emit("highlightAgentFile", {
                agentName: agentName,
                beginLine: beginLine,
                endLine: endLine
            });
        },
        getSourceFrom(sources) {
            if (sources.length === 1) {
                return sources[0].from;
            }
            for (let i = 0; i < sources.length; i++) {
                let source = sources[i];
                if (source.text.includes("self")) {
                    return source.from;
                }
            }
            return sources[0];
        }
    }
}
</script>

<template>
    <div class="agent">
        <span class="agent__name">{{ agent.name }}</span>
        <div class="agent__content">
            <span class="agent__content__title">Beliefs</span>
            <div class="agent__content__items" v-if="agent.beliefs.length > 0">
                <div class="agent__content__item agent__belief" v-for="(belief, index) in agent.beliefs"
                     :key="index"
                     @click="highlightAgentFile(getSourceFrom(belief.sources), belief.srcBeginLine, belief.srcEndLine)">
                    <span class="agent__belief__name">{{ belief.text }}</span>
                    <div class="flex flex-wrap gap-0.5">
                     <span v-for="(source, index) in belief.sources" :key="index" class="agent__content__item__source">
                         [{{ source.text }}]
                     </span>
                    </div>
                </div>
            </div>
            <span v-else class="agent__content__item text-aside no-redirect ml-2">No beliefs</span>
        </div>
        <div class="agent__content">
            <span class="agent__content__title">Intentions</span>
            <div class="agent__content__items no-redirect" v-if="agent.intentions.length > 0">
                <div class="agent__content__item" v-for="(intention, index) in agent.intentions" :key="index">
                    <span class="agent__intention__name">{{ intention.text }}</span>
                    <div class="flex flex-wrap gap-0.5">
                     <span v-for="(source, index) in intention.sources" :key="index"
                           class="agent__content__item__source">
                         [{{ source.text }}]
                     </span>
                    </div>
                </div>
            </div>
            <span v-else class="agent__content__item ml-2 text-aside no-redirect">No intentions</span>
        </div>
        <div class="agent__content">
            <span class="agent__content__title">Plans</span>
            <div class="agent__content__items flex-col" v-if="agent.plans.length > 0">
                <div class="agent__content__item agent__plan"
                     v-for="(plan, index) in agent.plans"

                     :key="index"

                     @click="clickPlan($event,plan.source.from, plan.srcBeginLine, plan.srcEndLine)"
                >
                    <div class="flex gap-1.5 items-center">
                     <span class="agent__plan__name">
                         {{ plan.text }}{{ plan.parameters }}<span v-if="plan.context.length !== 0">:</span>
                     </span>
                        <span v-if="plan.context.length !== 0"
                              :class="plan.context.length > ContextContentLengthToWrapper ? 'hover:underline' : ''">
                         <span class="agent__plan__context__content"
                               :style="plan.context.length > ContextContentLengthToWrapper ? 'display: none' : ''">
                             {{ plan.context }}
                         </span>
                         <i class="agent__plan__show context"
                            v-if="plan.context.length > ContextContentLengthToWrapper">
                             &lt;Context&gt;
                         </i>
                     </span>
                        <span class="agent__content__item__source">[{{ plan.source.text }}]</span>
                    </div>
                    <div class="flex flex-col">
                        <i class="agent__plan__show body self-center">
                            &lt;Body&gt;
                        </i>
                        <div class="agent__plan__body__content" style="display: none">
                         <span v-for="(bodyLine, index) in plan.body" :key="index"
                               class="agent__plan__body__content__line">{{ bodyLine }}</span>
                        </div>
                    </div>
                </div>
            </div>
            <span v-else class="agent__content__item text-aside ml-2">No plans</span>
        </div>
    </div>
</template>

<style scoped>

.agent {
    border-bottom: 1px solid var(--pallete-color-black-3);
    @apply flex flex-col gap-4 p-4;
}

.agent__name {
    @apply text-xl;
}

.agent__content {
    @apply flex flex-col gap-2;
}

.agent__content__items {
    @apply flex flex-wrap ml-2 gap-2 items-start;
}

.agent__content__item {
    background-color: var(--pallete-color-black-2);
    border: 1px solid var(--pallete-color-black-3);
    font-family: 'JetBrains Mono', monospace;
    @apply flex items-center gap-1.5 py-1 px-1.5 rounded-md w-fit break-all;
}

.agent__belief:hover, .agent__plan:hover {
    @apply cursor-pointer;
}

.agent__plan {
    @apply flex-col items-start gap-1.5;
}

.agent__content__item__source {
    font-size: 11px;
    @apply whitespace-nowrap;
}

.agent__belief {
    @apply flex-row;
}

.agent__belief__name, .agent__intention__name, .agent__plan__name {
    @apply font-bold;
}

.agent__belief__name {
    color: #1cb640;
}

.agent__intention__name {
    color: #1f6fe1;
}

.agent__plan__name {
    color: #e725ba;
}

.agent__plan__body__content {
    @apply flex flex-col gap-0.5;
}

.agent__plan__body__content:hover {
    text-decoration: underline;
}

.agent__plan__show {
    color: var(--pallete-text-aside);
    @apply whitespace-nowrap font-bold;
}

.agent__plan__show:hover {
    @apply underline;
}

</style>