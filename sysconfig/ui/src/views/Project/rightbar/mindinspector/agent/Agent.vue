<script>
import Button from "@/components/general/Button.vue";
import Loading from "@/components/general/Loading.vue";
import {EndPoints} from "@/services/chonide/endPoints";
import {Api as API} from "@/services/chonide/api";
import BeliefTerms from "@/views/Project/rightbar/mindinspector/agent/BeliefTerms.vue";
import DeedTerms from "@/views/Project/rightbar/mindinspector/agent/DeedTerms.vue";

export default {
  name: "Agent",
  components: {DeedTerms, BeliefTerms, Loading, Button},
  props: {
    agentData: {},
    focus: Boolean
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
        this.cycle = this.agentData.cycleInfo.currentCycleNumber;
      } else {
        this.agent.cycleInfo.newerCycleNumber = this.agentData.cycleInfo.newerCycleNumber;
        this.agent.cycleInfo.olderCycleNumber = this.agentData.cycleInfo.olderCycleNumber;
      }
    }
  },
  methods: {
    getOutsideSources(sources) {
      return sources.filter(s => s.from !== this.agent.name)
    },
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
            this.canMove = true;
          }
        })
      }
    },
    typeCycle(event) {
      if (event.target.value < this.agent.cycleInfo.olderCycleNumber || !this.canMove) {
        event.target.value = this.agent.cycleInfo.olderCycleNumber;
      } else if (event.target.value > this.agent.cycleInfo.newerCycleNumber) {
        event.target.value = this.agent.cycleInfo.newerCycleNumber;
      }
      this.cycle = event.target.value;
      this.moving = true;
      this.moveToNewCycle();
    },
    previousCycle() {
      if (this.cycle < this.agent.cycleInfo.olderCycleNumber || !this.canMove) {
        return;
      }
      this.cycle--;
      this.moving = true;
      this.moveToNewCycle();
    },
    nextCycle() {
      if (this.cycle === this.agent.cycleInfo.newerCycleNumber) {
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
        if (source.structure.includes("self")) {
          return source.from;
        }
      }
      return sources[0];
    },
    getSelectedDeed(plan) {
      for (let j = 0; j < plan.deeds.length; j++) {
        let deed = plan.deeds[j]
        if (deed.selected) {
          return deed;
        }
      }
    },
    getSelectedIntention() {
      for (let i = 0; i < this.agent.intentions.length; i++) {
        let intention = this.agent.intentions[i];
        if (intention.state.toLowerCase() === 'selected') {
          return intention
        }
      }
    }
  }
}
</script>

<template>
  <div :class="focus ? 'h-full overflow-y-auto' : ''" class="agent">
    <div class="agent__header">
      <span class="agent__name">{{ agent.name }}</span>
      <div class="agent__controller">
        <Loading v-if="!canMove" border-width="1px" class="mx-2" ratio="15px"/>
        <Button
            v-if="moving"
            icon="back.svg"
            icon-ratio="11px"

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
                :max="agent.cycleInfo.newerCycleNumber"
                :size="agent.cycleInfo.newerCycleNumber.toString().length"
                class="agent__cycle__current-cycle__input"

                type="number"
                @keyup.enter="typeCycle"
            />
          </div>
          <span v-if="moving" class="agent__total-cycle">First: {{ agent.cycleInfo.olderCycleNumber }}</span>
          <span v-if="moving" class="agent__total-cycle">Last: {{ agent.cycleInfo.newerCycleNumber }}</span>
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
        <Button :color="!focus ? 'transparent' : 'var(--pallete-color-black-2)'"
                style="border: 1px solid var(--pallete-color-black-3)"
                :text="!focus ? 'Focus' : 'Unfocus'"

                @click="$emit('focusAgent')"/>
      </div>
    </div>

    <div class="agent__content">
      <span class="agent__content__title">Beliefs</span>
      <div v-if="agent.beliefs.length > 0" class="agent__content__items">
        <div v-for="(belief, index) in agent.beliefs" :key="index"
             class="agent__content__item agent__belief">
          <span class="agent__belief__name"
                @click="highlightAgentFile(getSourceFrom(belief.sources), belief.srcBeginLine, belief.srcEndLine)">
            {{ belief.structure }}
          </span>
          <BeliefTerms v-if="belief.terms" :terms="belief.terms" :wrap="true"/>
          <div v-if="getOutsideSources(belief.sources).length > 0" class="agent__content__item__source">
            <span class="text-aside">from</span>
            <span v-for="(source, index) in getOutsideSources(belief.sources)" :key="index">
                <span>{{ source.from }}</span>
                <span v-if="index < getOutsideSources(belief.sources).length - 1">,</span>
             </span>
          </div>
        </div>
      </div>
      <span v-else class="agent__content__item text-aside ">No beliefs</span>
    </div>
    <div class="agent__content">
      <span class="agent__content__title">Intentions</span>
      <div v-if="agent.intentions.length > 0" class="agent__content__items ">
        <div v-for="(intention, index) in agent.intentions" :key="index" :class="intention.state.toLowerCase()"
             class="agent__intention agent__content__item">

          <span class="agent__intention__name">!{{ intention.structure }}</span>

          <span v-if="intention.state.toLowerCase() !== 'selected'" class="agent__intention__status">
            <span class="text-aside">in</span>
            <span>{{ intention.state.toLowerCase() }}</span>
          </span>

          <div v-if="getOutsideSources(intention.sources).length > 0" class="agent__content__item__source">
            <span class="text-aside">from</span>
            <span v-for="(source, index) in getOutsideSources(intention.sources)" :key="index">
                <span>{{ source.from }}</span>
                <span v-if="index < getOutsideSources(intention.sources).length - 1">,</span>
             </span>
          </div>

        </div>
      </div>
      <span v-else class="agent__content__item text-aside ">No intentions</span>
    </div>

    <div class="agent__content">
      <span class="agent__content__title">Running plans</span>
      <div v-if="agent.runningPlans.length > 0" class="agent__content__items">
        <div v-for="(plan, index) in agent.runningPlans"
             :key="index"

             class="agent__content__item agent__plan"

             :class="getSelectedIntention() && getSelectedIntention().id === plan.intentionId ? 'selected' : ''"
        >
          <div class="flex gap-1.5 items-center">
            <span class="agent__plan__name"
                  @click="clickPlan($event,plan.source.from, plan.srcBeginLine, plan.srcEndLine)">
              {{ plan.structure }} {{ plan.parameters.structure }}
              <span v-if="plan.context != null">:</span>
            </span>
            <span v-if="plan.context != null"
                  :class="plan.context.structure.length > ContextContentLengthToWrapper ? 'hover:underline' : ''">
               <span :style="plan.context.structure.length > ContextContentLengthToWrapper ? 'display: none' : ''"
                     class="agent__plan__context__content">{{ plan.context.structure }}</span>
               <i v-if="plan.context.structure.length > ContextContentLengthToWrapper"
                  class="agent__plan__show context">&lt;Context&gt;</i>
            </span>
          </div>
          <div v-if="getSelectedDeed(plan)" class="agent__plan__deed">
            <span>{{ getSelectedDeed(plan).structure }}</span>
            <DeedTerms :terms="getSelectedDeed(plan).terms"/>
          </div>
        </div>
      </div>
      <span v-else class="agent__content__item text-aside">No running plans</span>
    </div>
  </div>
</template>

<style scoped>

.agent {
  @apply flex flex-col;
}

.agent__header {
  background-color: var(--pallete-color-black-1);
  border-bottom: 1px solid var(--pallete-color-black-3);
  border-top: 1px solid var(--pallete-color-black-3);
  @apply flex items-center justify-between gap-2 sticky top-0 py-2 px-4
}

.agent__controller {
  @apply flex gap-2 items-center top-0 sticky;
}

.agent__controller__buttons {
  @apply flex gap-1;
}

.agent__cycle {
  background-color: var(--pallete-color-black-2);
  border: 1px solid var(--pallete-color-black-3);
  height: 32px;
  @apply flex justify-center items-center rounded-md cursor-pointer;
}

.agent__content__title {
  @apply font-bold
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
  width: 60px;
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
  @apply mr-3 pl-3 whitespace-nowrap;
}

.agent__next-cycle {
  rotate: -90deg;
}

.agent__previous-cycle {
  rotate: 90deg;
}

.agent__name {
  @apply text-lg;
}

.agent__content {
  @apply flex flex-col gap-4 m-4;
}

.agent__content__items {
  @apply flex flex-col gap-2 items-start;
}

.agent__content__item {
  @apply flex items-center rounded-md w-fit break-all shrink-0;
}

.agent__belief {
  @apply gap-1.5 flex-wrap
}

.agent__intention {
  @apply gap-1.5
}

.agent__intention__status {
  @apply flex items-center gap-1.5
}

.agent__plan {
  @apply flex-col items-start gap-1.5;
}

.agent__content__item__source {
  @apply flex flex-wrap gap-1.5 whitespace-nowrap;
}

.agent__belief__name, .agent__intention__name, .agent__plan__name {
  color: var(--pallete-text-aside);
  @apply font-bold whitespace-nowrap
}

.agent__belief__name, .agent__plan__name {
  @apply cursor-pointer
}

.agent__belief__name:hover, .agent__plan__name:hover {
  @apply underline
}

.agent__belief__name {
  color: #1cb640;
}

.agent__intention.selected .agent__intention__name{
  color: #1f6fe1;
}

.agent__plan.selected .agent__plan__name{
  color: #e725ba;
}

.agent__plan__show {
  color: var(--pallete-text-aside);
  @apply whitespace-nowrap font-bold;
}

.agent__plan__show:hover {
  @apply underline;
}

.agent__plan__deed {
  font-family: 'JetBrains Mono', monospace;
  font-size: 12px;
  border-left: 2px solid var(--pallete-color-black-3);
  background-color: var(--pallete-color-black-2);
  @apply flex gap-3 flex-wrap py-2 px-4 rounded w-full
}

</style>