<script>

import Button from "@/components/Button.vue";
import Header from "@/components/layout/Header.vue";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {AppEvent, MessageType} from "@/utils/enums";
import Loading from "@/components/Loading.vue";
import validateProject from "@/views/Project/util";

export default {
  name: "ProjectHeader",
  components: {Loading, Header, Button},
  props: {
    configuration: {},
    domain: {},
    project: {},
    savingProject: {
      default: false
    }
  },
  data() {
    return {
      startingMas: false,
      stopingMas: false,
      smaRunning: false,
      restart: false
    }
  },
  methods: {
    startMas() {
      if (validateProject(this.$emit, this.project)) {
        return;
      }
      this.startingMas = true;
      Api.put(EndPoints.MAS, {
        params: {
          action: "start"
        }
      }, this.project).then((response) => {
        this.$emit(AppEvent.MESSAGE, {
          content: response.data.data.message,
          type: MessageType.SUCCESS
        });
      }).finally(() => {
        this.$emit("smaRunning", true);
        this.smaRunning = true;
        this.startingMas = false;
      });
    },
    stopMas() {
      this.stopingMas = true;
      Api.put(EndPoints.MAS, {params: {action: "stop"}}).then((response) => {
        if (response.data.status === 202) {
          this.$emit(AppEvent.MESSAGE, {content: response.data.message, type: MessageType.WARNING});
        } else {
          this.$emit(AppEvent.MESSAGE, {content: response.data.message, type: MessageType.SUCCESS});
        }
      }).finally(() => {
        this.$emit("smaRunning", false);
        this.smaRunning = false;
        this.stopingMas = false;
      });
    },
  }
}

</script>

<template>
  <Header>
    <template v-slot:left>
      <div class="flex items-center h-full">
        <router-link to="/home">
          <Button icon="back.svg" icon-ratio="11px"/>
        </router-link>
        <router-link v-if="configuration.network" to="/connect">
          <Button icon="wifi-quality-4.svg" icon-ratio="12px">
            <template v-slot:content>
              Networks
            </template>
          </Button>
        </router-link>
        <router-link v-if="configuration.ddns" to="/domain">
          <Button icon="domain.svg" icon-ratio="12px">
            <template v-slot:content>
              Bot name
            </template>
          </Button>
        </router-link>
      </div>
    </template>
    <template v-slot:center>
      <div class="flex items-center gap-3">
        <span class="project__header-bar__title">{{ project.name }}</span>
        <div class="project__project-status">
          <Loading v-if="savingProject" border-width="1px" main-color="var(--pallete-text-main)"
                   ratio="11px"/>
          <img v-else alt="check-icon" src="@/assets/media/icon/check.svg" style="width: 11px">
        </div>
      </div>
    </template>
    <template v-slot:right>
      <div class="flex items-center gap-1.5 h-full">
        <Button v-if="configuration.startMAS" :is-loading="startingMas" icon="start.svg"
                icon-ratio="12px"
                @click="startMas"/>
        <Button v-if="configuration.stopMAS" :is-loading="stopingMas" icon="stop.svg" icon-ratio="12px"
                @click="stopMas"/>
      </div>
    </template>
  </Header>
</template>

<style>

</style>