<script>

import Button from "@/components/general/Button.vue";
import Header from "@/components/Header.vue";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {AppEvent, MessageType} from "@/utils/enums";
import Loading from "@/components/general/Loading.vue";
import validateProject from "@/views/Project/util";
import NetworksPopup from "@/components/popups/networksPopup/NetworksPopup.vue";
import DomainPopup from "@/components/popups/DomainPopup.vue";

const DEFAULT_LINKS_PROTOCOL = "http://";
const MINDINSPECTOR_PORT = ":3272";

export default {
  name: "ProjectHeader",
  components: {DomainPopup, NetworksPopup, Loading, Header, Button},
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
      restart: false,
      networkOpen: false
    }
  },
  computed: {
    mindInspectorUrl() {
      if (this.domain == null) {
        return "";
      }
      return DEFAULT_LINKS_PROTOCOL + this.domain.domain + MINDINSPECTOR_PORT;
    },
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
  <NetworksPopup @message="$emit('message', $event)" v-if="networkOpen" @closed="networkOpen = false"/>
  <Header>
    <template v-slot:left>
      <div class="flex items-center h-full">
        <router-link to="/home">
          <Button icon="back.svg" icon-ratio="11px"/>
        </router-link>
        <Button icon="wifi-quality-3.svg" icon-ratio="15px" v-if="configuration != null && configuration.network"
                text="Network" @click="networkOpen = true"/>
        <Button icon="domain.svg" icon-ratio="13px" text="Bot name" v-if="configuration != null && configuration.ddns">
          <template v-slot:content>
            Domain
            <DomainPopup @message="$emit('message', $event)"/>
          </template>
        </Button>
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
        <Button v-if="configuration.mindInspector === 1" icon="terminal.svg" icon-ratio="16px"
                :link="mindInspectorUrl"/>
      </div>
    </template>
  </Header>
</template>

<style>

</style>