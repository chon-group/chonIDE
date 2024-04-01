<template>
    <div class="project flex flex-col h-screen">

        <Popup title="Board response" ref="boardResponse" can-close width="var(--container-width-1)">
            <template v-slot:content>
                <div class="project__compiled-response">
                    {{ boardResponse }}
                </div>
            </template>
        </Popup>

        <ProjectHeader
                :configuration="configuration"
                :domain="domain"
                :project="project"

                @message="$emit(AppEvent.MESSAGE, $event)"
        />

        <div class="flex">
            <div class="project__left-bar">
                <div class="project__header-bar">
                    <span class="project__header-bar__title">{{ project.name }}</span>
                    <div class="project__project-status">
                        <Loading v-if="savingProject" border-width="1px" ratio="12px"
                                 main-color="var(--pallete-text-main)"/>
                        <img v-else src="@/assets/media/icon/check.svg" style="width: 11px">
                    </div>
                </div>
                <Explorer
                        :configuration="configuration"
                        :project="project"
                        :current-file="currentFile"

                        @setCurrentFile="currentFile = $event"
                        @fileType="currentFileType = $event"

                        style="height: calc(100vh - calc(2 * var(--bar-height)));"
                />
            </div>

            <div class="flex flex-col" style="width: calc(100vw - var(--explorer-width))">
                <TabController
                        :current-file="currentFile"
                        :current-board="currentBoard"
                        :current-file-type="currentFileType"

                        @changeArchClass="currentFile.archClass = $event"
                />
                <Coder
                        :sourceCode="currentFile.sourceCode"

                        @sourceCode="currentFile.sourceCode = $event"
                />
                <Console :domain="domain"/>
            </div>

            <Boards
                    v-if="currentFileType === FileType.FIRMWARE"

                    @selectedBoard="currentBoard = $event"

                    style="calc(100vh - var(--bar-height))"
            />
        </div>

    </div>

</template>

<script>
import Util from "@/domain/Util";
import {AppEvent, FileType} from "@/domain/Enums";
import Loading from "@/components/Loading";
import Popup from "@/components/Popup";
import {API, EndPoints} from "@/domain/API";
import router, {Routes} from "@/router";
import Coder from "@/views/Project/code/Coder.vue";
import Explorer from "@/views/Project/explorer/Explorer.vue";
import Boards from "@/views/Project/boards/Boards.vue";
import TabController from "@/views/Project/code/TabController.vue";
import ProjectHeader from "@/views/Project/ProjectHeader.vue";
import Console from "@/views/Project/code/Console.vue";

export default {
    name: "Project",
    components: {Console, ProjectHeader, TabController, Boards, Explorer, Coder, Loading, Popup},
    data() {
        return {
            project: {name: "", agents: [], firmwares: []},
            currentFile: {name: "No file", sourceCode: ""},
            currentBoard: null,
            currentFileType: FileType.AGENT,
            domain: null,
            savingProject: false,
            configuration: {},
        }
    },
    watch: {
        project: {
            async handler(newProjectValue) {
                this.savingProject = true;
                await API.put(EndPoints.PROJECTS, {}, newProjectValue).then(() => {
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
    setup() {
        Util.setTitle("Project");
        API.loadToken();
    },
    mounted() {
        API.get(EndPoints.PROJECTS, true, {params: {projectId: this.$route.params.id, getType: 1}}).then((response) => {
            if (response.data.status === 200) {
                this.project = response.data.data;
                this.project.id = this.$route.params.id;

                Util.setTitle(this.project.name);

                if (this.project.agents.length > 0) {
                    this.currentFile = this.project.agents[0];
                }
            }
        }).catch(() => {
            router.push(Routes.HOME);
        });

        API.get(EndPoints.CONFIGURATION, false).then((response) => {
            if (response.data.status === 200) {
                this.configuration = response.data.data;
            }
        });

        API.get(EndPoints.DOMAINS).then((response) => {
            this.domain = response.data.data;
        });
    }
}
</script>

<style scoped>

.project {
    --explorer-width: 300px;
    --bar-height: 42px;
    @apply overflow-y-hidden;
}

.project__compiled-response {
    background-color: var(--pallete-color-black-3);
    word-break: break-word;
    @apply p-2.5 rounded-sm text-xl;
}

.project__project-status {
    @apply select-none mr-1.5;
}

.project__left-bar {
    min-width: var(--explorer-width);
    background-color: var(--pallete-color-black-2);
    @apply flex flex-col;
}

</style>