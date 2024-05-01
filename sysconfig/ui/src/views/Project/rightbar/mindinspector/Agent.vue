<script>
import Button from "@/components/Button.vue";
import axios from "axios";

export default {
    name: "Agent",
    components: {Button},
    props: {
        agentData: {},
        agentsUrl: String
    },
    data() {
        return {
            cycle: 0,
            agent: this.agentData,
            moving: false,
            canMove: true
        }
    },
    computed: {
        ContextContentLengthToWrapper() {
            return 20;
        },
        url() {
            return this.agentsUrl + "?agent=" + this.agent.name + "&cycle=" + this.cycle;
        }
    },
    watch: {
        agentData() {
            if (!this.moving) {
                this.agent = this.agentData;
                this.cycle = this.agentData.currentCycleNumber
            } else {
                this.agent.totalCycleNumber = this.agentData.totalCycleNumber
            }
        }
    },
    methods: {
        goToRealTime() {
            this.moving = false;
        },
        moveToNewCycle() {
            if (this.canMove) {
                this.canMove = false;
                axios.get(this.url).then((response) => {
                    if (response.status === 200) {
                        this.agent = response.data;
                    }
                }).then(() => {
                    this.canMove = true;
                });
            }
        },
        typeCycle(event) {
            if (event.target.value <= 0 || !this.canMove) {
                event.target.value = 1;
            }
            this.cycle = event.target.value;
            this.moving = true;
            this.moveToNewCycle();
        },
        previousCycle() {
            if (this.cycle <= 1 || !this.canMove) {
                return;
            }
            this.cycle--;
            this.moving = true;
            this.moveToNewCycle();
        },
        nextCycle() {
            if (this.cycle === this.agent.totalCycleNumber) {
                return;
            }
            this.cycle++;
            this.moving = true;
            this.moveToNewCycle();
        },
        clickPlan(event, agentName, beginLine, endLine) {
            let element = event.target;
            const elementClassList = element.classList;

            if (elementClassList.contains("agent__plan__show") && elementClassList.contains("context")) {
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
    <div class="agent" tabindex="0">
        <div class="flex justify-between gap-2">
            <span class="agent__name">{{ agent.name }}</span>
            <div class="agent__controller">
                <Button
                        style="border: 1px solid var(--pallete-color-black-3)"
                        v-if="moving"
                        text="Real time"

                        @click="goToRealTime"
                />
                <span class="px-3">Cycle</span>
                <div class="agent__cycle">
                    <input
                     type="number"
                     min="1"
                     :max="agent.totalCycleNumber"
                     maxlength="4"

                     class="agent__current-cycle"

                     v-model="cycle"
                     @focus="this.moving = true"

                     @keyup.enter="typeCycle"
                    />
                    <span
                        v-if="cycle !== agent.totalCycleNumber"

                        class="agent__total-cycle"
                    >{{ agent.totalCycleNumber }}</span>
                </div>
                <div class="flex gap-1" v-if="moving">
                    <Button
                     style="border: 1px solid var(--pallete-color-black-3)"
                     icon="arrow.svg"
                     icon-ratio="10px"
                     class="agent__previous-cycle"

                     @click="previousCycle"
                    />
                    <Button
                     style="border: 1px solid var(--pallete-color-black-3)"
                     icon="arrow.svg"
                     icon-ratio="10px"
                     class="agent__next-cycle"

                     @click="nextCycle"
                    />
                </div>
            </div>
        </div>

        <div class="agent__content">
            <span class="agent__content__title">Beliefs</span>
            <div class="agent__content__items" v-if="agent.beliefs.length > 0">
                <div class="agent__content__item agent__belief" v-for="(belief, index) in agent.beliefs"
                     :key="index"
                     @click="highlightAgentFile(getSourceFrom(belief.sources), belief.srcBeginLine, belief.srcEndLine)">
                    <span class="agent__belief__name">{{ belief.text }}.</span>
                    <div class="flex flex-wrap gap-0.5">
                     <span v-for="(source, index) in belief.sources" :key="index" class="agent__content__item__source">
                         {{ source.text }}
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
                    <span class="agent__intention__name">!{{ intention.text }}</span>
                    <div class="flex flex-wrap gap-0.5">
                     <span v-for="(source, index) in intention.sources" :key="index"
                           class="agent__content__item__source">
                         {{ source.text }}
                     </span>
                    </div>
                </div>
            </div>
            <span v-else class="agent__content__item ml-2 text-aside no-redirect">No intentions</span>
        </div>

        <div class="agent__content">
            <span class="agent__content__title">Plans</span>
            <div class="agent__content__items" v-if="agent.plans.length > 0">
                <div :class="['agent__content__item', 'agent__plan', plan.triggered ? 'triggered' : '']"
                     v-for="(plan, index) in agent.plans"

                     :key="index"

                     @click="clickPlan($event,plan.source.from, plan.srcBeginLine, plan.srcEndLine)"
                >
                    <div class="flex gap-1.5 items-center">
                     <span class="agent__plan__name">
                         {{ plan.text }}{{ plan.parameters.text }}<span v-if="plan.context !== null">:</span>
                     </span>
                        <span v-if="plan.context !== null"
                              :class="plan.context.text.length > ContextContentLengthToWrapper ? 'hover:underline' :
                              ''">
                         <span class="agent__plan__context__content"
                               :style="plan.context.text.length > ContextContentLengthToWrapper ? 'display: none' : ''">
                             {{ plan.context.text }}
                         </span>
                         <i class="agent__plan__show context"
                            v-if="plan.context.text.length > ContextContentLengthToWrapper">
                             &lt;Context&gt;
                         </i>
                     </span>
                        <span class="agent__content__item__source">{{ plan.source.text }}</span>
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

.agent__controller {
    @apply flex gap-2 items-center;
}

.agent__cycle {
    border: 1px solid var(--pallete-color-black-3);
    height: 32px;
    @apply flex justify-center items-center rounded-md;
}

.agent__total-cycle {
    color: var(--pallete-text-aside);
    border-left: 1px solid var(--pallete-color-black-3);
    @apply px-3;
}

.agent__current-cycle {
    color: var(--pallete-text-main);
    width: 60px;
    text-align: center;
    border: 0;
    background-color: transparent;
    border-left: 1px solid var(--pallete-color-black-3);
    @apply h-full;
}

.agent__current-cycle:hover, .agent__current-cycle:focus {
    background-color: var(--pallete-color-black-2);
}

.agent__next-cycle {
    rotate: -90deg;
}

.agent__previous-cycle {
    rotate: 90deg;
}

.agent__name {
    @apply text-xl;
}

.agent__content {
    @apply flex flex-col gap-2;
}

.agent__content__items {
    @apply flex flex-col ml-2 gap-2 items-start;
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

.agent__plan.triggered {
    border: 1px solid #e725ba;
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

.agent__plan__show {
    color: var(--pallete-text-aside);
    @apply whitespace-nowrap font-bold;
}

.agent__plan__show:hover {
    @apply underline;
}

</style>