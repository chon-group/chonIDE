<template>
    <div class="project flex flex-col h-screen w-full">
        <ProjectHeader
                :configuration="configuration"
                :domain="domain"
                :project="project"
                :saving-project="savingProject"

                @message="$emit(AppEvent.MESSAGE, $event)"
                @smaRunning="smaRunning = $event"
        />

        <div class="flex w-full h-full">
            <LeftBar
                    :configuration="configuration"
                    :project="project"
                    :current-file="currentFile"

                    @addFirmware="addFirmware"
                    @removeFirmware="removeFirmware"
                    @addAgent="addAgent"
                    @removeAgent="removeAgent"
                    @setCurrentFile="currentFile = $event"
                    @setFileType="currentFileType = $event"
            />

            <div v-if="currentFile.name.length === 0 && currentFile.sourceCode.length === 0"
             class="flex items-center justify-center w-full h-full">
                <span class="text-aside">Start creating a new agent or firmware file</span>
            </div>
            <div class="main" v-else>
                <TabController
                        :current-file="currentFile"
                        :current-board="currentBoard"
                        :current-file-type="currentFileType"

                        @changeArchClass="currentFile.archClass = $event"
                />
                <Coder
                        :sourceCode="currentFile.sourceCode"
                        :selected-lines="selectedLines"
                        :sma-running="smaRunning"

                        @sourceCode="currentFile.sourceCode = $event"

                        @message="$emit(AppEvent.MESSAGE, $event)"
                />
                <Console :domain="domain" ref="console"/>
            </div>

            <RightBar
                    :current-file-type="currentFileType"
                    :domain="domain"
                    :sma-running="smaRunning"

                    @highlightAgentFile="highlightAgentFile"
                    @selectedBoard="currentBoard = $event"
            />
        </div>

    </div>

</template>

<script>
/* eslint-disable */

import GeneralUtil from "@/utils/generalUtil";
import {AppEvent, FileType} from "@/utils/enums";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import router, {Routes} from "@/router";
import Coder from "@/views/Project/main/Coder.vue";
import TabController from "@/views/Project/main/TabController.vue";
import ProjectHeader from "@/views/Project/header/ProjectHeader.vue";
import Console from "@/views/Project/main/Console.vue";
import LeftBar from "@/views/Project/leftbar/LeftBar.vue";
import RightBar from "@/views/Project/rightbar/RightBar.vue";

export default {
    name: "Project",
    components: {RightBar, LeftBar, Console, ProjectHeader, TabController, Coder},
    data() {
        return {
            project: {name: "", agents: [], firmwares: []},
            currentFile: {name:"", sourceCode: ""},
            currentBoard: null,
            currentFileType: FileType.AGENT,
            domain: null,
            savingProject: false,
            configuration: {},
            smaRunning: false,
            selectedLines: { beginLine: 0, endLine: 0}
        }
    },
    watch: {
        project: {
            async handler(newProjectValue) {
                this.savingProject = true;
                await Api.put(EndPoints.PROJECTS, {}, newProjectValue).then(() => {
                    setTimeout(() => {
                        this.savingProject = false;
                    }, 100);
                });
            },
            deep: true
        }
    },
    computed: {
        AppEvent() {
            return AppEvent;
        },
        FileType() {
            return FileType;
        }
    },
    methods: {
        highlightAgentFile(agentFile) {
            let agentWasFind = false;
            for(let i = 0; i < this.project.agents.length; i++) {
                let agent = this.project.agents[i];
                if (agent.name === agentFile.agentName) {
                    agentWasFind = true;
                    this.currentFile = agent;
                    break;
                }
            }
            if (!agentWasFind) {
                return;
            }
            this.selectedLines.beginLine = agentFile.beginLine;
            this.selectedLines.endLine = agentFile.endLine;
            setTimeout(() => {
                this.selectedLines.beginLine = 0;
                this.selectedLines.endLine = 0;
            }, 500);
        },
        addAgent(agent) {
            this.project.agents.push(agent);
        },
        removeAgent(index) {
            this.removeFile(index, this.project.agents);
        },
        addFirmware(firmware) {
            this.project.firmwares.push(firmware);
        },
        removeFirmware(index) {
            this.removeFile(index, this.project.firmwares);
        },
        removeFile(index, files) {
            if (this.currentFile === files[index]) {
                if (index === 0) {
                    files.splice(index, 1);
                    if (files.length === 0) {
                        this.currentFile = {name:"", sourceCode: ""};
                    } else {
                        this.currentFile = files[index];
                    }
                } else {
                    this.currentFile = files[index - 1];
                    files.splice(index, 1);
                }
            } else {
                files.splice(index, 1);
            }
        }
    },
    setup() {
        GeneralUtil.setTitle("Project");
        Api.loadToken();
    },
    mounted() {
        const id = this.$route.params.id;

        Api.get(EndPoints.PROJECTS, {params: {projectId: id, getType: 1}}).then((response) => {
            if (response.data.status === 200) {
                this.project = response.data.data;
                this.project.id = id;

                GeneralUtil.setTitle(this.project.name);

                if (this.project.agents.length > 0) {
                    this.currentFile = this.project.agents[0];
                } else if (this.project.firmwares.length > 0) {
                    this.currentFile = this.project.firmwares[0];
                }
            }
        }).catch(() => {
            router.push(Routes.HOME);
        });

        Api.get(EndPoints.CONFIGURATION).then((response) => {
            if (response.data.status === 200) {
                this.configuration = response.data.data;
            }
        });

        Api.get(EndPoints.DOMAINS).then((response) => {
            this.domain = response.data.data;
        });
    }
}
</script>

<style scoped>

.project {
    --bar-height: 42px;
    @apply overflow-y-hidden;
}

.main {
    min-width: 50px;
    @apply flex flex-col flex-grow;
}

</style>