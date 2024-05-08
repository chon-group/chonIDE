<script>
import Button from "@/components/Button.vue";
import Loading from "@/components/Loading.vue";
import {EndPoints} from "@/services/chonide/endPoints";
import {Api as API} from "@/services/chonide/api";

export default {
  name: "Agent",
  components: {Loading, Button},
  props: {
    agentData: {}
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
      return EndPoints.MINDINSPECTOR + "?agent=" + this.agent.name + "&cycle=" + this.cycle;
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
    editCycle() {
      this.moving = true;
      this.$refs.cycleInput.focus();
    },
    goToRealTime() {
      this.moving = false;
    },
    moveToNewCycle() {
      if (this.canMove) {
        this.canMove = false;
        API.get(this.url).then((response) => {
          if (response.status === 200) {
            this.agent = response.data.data;
          }
        }).then(() => {
          this.canMove = true;
        });
      }
    },
    typeCycle(event) {
      if (event.target.value <= 0 || !this.canMove) {
        event.target.value = 1;
      } else if (event.target.value > this.agent.totalCycleNumber) {
        event.target.value = this.agent.totalCycleNumber;
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
        <Loading v-if="!canMove" border-width="1px" class="mx-2"
                 main-color="var(--pallete-text-main)" ratio="15px"/>
        <Button
            v-if="moving"
            icon="back.svg"
            icon-ratio="11px"
            style="border: 1px solid var(--pallete-color-black-3)"

            @click="goToRealTime"
        />
        <div class="agent__cycle" @click="editCycle">
          <div class="agent__cycle__current-cycle">
            <div class="agent__cycle__current-cycle__text">
              <span>Cycle</span>
            </div>
            <input
                ref="cycleInput"
                v-model="cycle"
                :max="agent.totalCycleNumber"
                :size="cycle.toString().length"
                class="agent__cycle__current-cycle__input"

                maxlength="4"

                min="1"

                type="number"
                @keyup.enter="typeCycle"
            />
          </div>
          <span
              v-if="moving"
              class="agent__total-cycle"
          >{{ agent.totalCycleNumber }}</span>
        </div>
        <div v-if="moving" :class="['agent__controller__buttons', !canMove ? 'pointer-events-none select-none' : '']">
          <Button
              class="agent__previous-cycle"
              icon="arrow.svg"
              icon-ratio="10px"
              style="border: 1px solid var(--pallete-color-black-3)"

              @click="previousCycle"
          />
          <Button
              class="agent__next-cycle"
              icon="arrow.svg"
              icon-ratio="10px"
              style="border: 1px solid var(--pallete-color-black-3)"

              @click="nextCycle"
          />
        </div>
      </div>
    </div>

    <div class="agent__content">
      <span class="agent__content__title">Beliefs</span>
      <div v-if="agent.beliefs.length > 0" class="agent__content__items">
        <div v-for="(belief, index) in agent.beliefs" :key="index"
             class="agent__content__item agent__belief"
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
      <div v-if="agent.intentions.length > 0" class="agent__content__items no-redirect">
        <div v-for="(intention, index) in agent.intentions" :key="index" class="agent__content__item">
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
      <div v-if="agent.plans.length > 0" class="agent__content__items">
        <div v-for="(plan, index) in agent.plans"
             :key="index"

             :class="['agent__content__item', 'agent__plan', plan.triggered ? 'triggered' : '']"

             @click="clickPlan($event,plan.source.from, plan.srcBeginLine, plan.srcEndLine)"
        >
          <div class="flex gap-1.5 items-center">
                     <span class="agent__plan__name">
                         {{ plan.text }}{{ plan.parameters.text }}<span v-if="plan.context !== null">:</span>
                     </span>
            <span v-if="plan.context !== null"
                  :class="plan.context.text.length > ContextContentLengthToWrapper ? 'hover:underline' :
                              ''">
                         <span :style="plan.context.text.length > ContextContentLengthToWrapper ? 'display: none' : ''"
                               class="agent__plan__context__content">
                             {{ plan.context.text }}
                         </span>
                         <i v-if="plan.context.text.length > ContextContentLengthToWrapper"
                            class="agent__plan__show context">
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

.agent__controller__buttons {
  @apply flex gap-1;
}

.agent__cycle {
  border: 1px solid var(--pallete-color-black-3);
  background-color: var(--pallete-color-black-2);
  height: 32px;
  @apply flex justify-center items-center rounded-md cursor-pointer;
}

.agent__cycle__current-cycle {
  @apply flex gap-1.5 justify-center items-center h-full pr-1.5;
}

.agent__cycle__current-cycle__text {
  @apply flex items-center h-full ml-3;
}

.agent__cycle__current-cycle__input {
  color: var(--pallete-text-main);
  text-align: center;
  border: 0;
  background-color: transparent;
  @apply cursor-pointer rounded-md py-0.5;
}

.agent__cycle__current-cycle__input:hover {
  background-color: var(--pallete-color-black-3);
}

.agent__cycle__current-cycle__input:focus {
  background-color: var(--pallete-color-black-3);
}

.agent__total-cycle {
  color: var(--pallete-text-aside);
  border-left: 1px solid var(--pallete-color-black-3);
  @apply mr-3 pl-3;
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