<script>

import Button from "@/components/Button.vue";
import Header from "@/layout/Header.vue";
import {API, EndPoints} from "@/domain/API";
import {AppEvent, MessageType} from "@/domain/Enums";
import Loading from "@/components/Loading.vue";
import validateProject from "@/views/Project/util";

const DEFAULT_LINKS_PROTOCOL = "http://";
const MIND_INSPECTOR_PORT = ":3272";

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
        }
    },
    computed: {
        mindInspectorUrl() {
            return DEFAULT_LINKS_PROTOCOL + this.domain.domain + MIND_INSPECTOR_PORT;
        }
    },
    methods: {
        startMas() {
            if (validateProject(this.$emit, this.project)) {
                return;
            }
            this.startingMas = true;
            API.put(EndPoints.MAS, {
                params: {
                    action: "start"
                }
            }, this.project).then((response) => {
                this.$emit(AppEvent.MESSAGE, {
                    content: response.data.data.message,
                    type: MessageType.SUCCESS
                });
            }).finally(() => {
                this.startingMas = false;
            });
        },
        stopMas() {
            this.stopingMas = true;
            API.put(EndPoints.MAS, {params: {action: "stop"}}).then((response) => {
                if (response.data.status === 202) {
                    this.$emit(AppEvent.MESSAGE, {content: response.data.message, type: MessageType.WARNING});
                } else {
                    this.$emit(AppEvent.MESSAGE, {content: response.data.message, type: MessageType.SUCCESS});
                }
            }).finally(() => {
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
                    <Button icon-ratio="11px" icon="back.svg"/>
                </router-link>
                <router-link to="/connect" v-if="configuration.network">
                    <Button icon-ratio="12px" icon="wifi-quality-4.svg">
                        <template v-slot:content>
                            Networks
                        </template>
                    </Button>
                </router-link>
                <router-link to="/domain" v-if="configuration.ddns">
                    <Button icon-ratio="12px" icon="domain.svg">
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
                    <Loading v-if="savingProject" border-width="1px" ratio="11px"
                             main-color="var(--pallete-text-main)"/>
                    <img v-else src="../../../assets/media/icon/check.svg" style="width: 11px" alt="check-icon">
                </div>
            </div>
        </template>
        <template v-slot:right>
            <div class="flex items-center gap-1.5 h-full">
                <Button v-if="configuration.startMAS" icon="start.svg" icon-ratio="11px" @click="startMas"
                        :is-loading="startingMas"/>
                <Button v-if="configuration.stopMAS" icon="stop.svg" icon-ratio="11px" @click="stopMas"
                        :is-loading="stopingMas"/>
                <div class="project-action-separator"></div>
                <Button v-if="domain != null && configuration.mindInspector"
                        :link="mindInspectorUrl"
                        icon="mindinspector.svg"
                        icon-ratio="14px"
                        text="Mind Inspector"/>
            </div>
        </template>
    </Header>
</template>

<style>

</style>