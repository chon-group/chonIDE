<template>
    <div class="project flex flex-col h-screen w-full">
        <ProjectHeader
                :configuration="configuration"
                :domain="domain"
                :project="project"
                :saving-project="savingProject"

                @message="$emit(AppEvent.MESSAGE, $event)"
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

            <div class="flex flex-col flex-grow">
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

            <RightBar
                    :current-file-type="currentFileType"
                    @selectedBoard="currentBoard = $event"
            />
        </div>

    </div>

</template>

<script>
import Util from "@/domain/Util";
import {AppEvent, FileType} from "@/domain/Enums";
import {API, EndPoints} from "@/domain/API";
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
            currentFile: this.mountNoSelectedFile(),
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
    methods: {
        mountNoSelectedFile() {
            return {name: "No file", sourceCode: ""};
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
                        this.currentFile = this.mountNoSelectedFile();
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
        Util.setTitle("Project");
        API.loadToken();
    },
    mounted() {
        const id = this.$route.params.id;

        API.get(EndPoints.PROJECTS, true, {params: {projectId: id, getType: 1}}).then((response) => {
            if (response.data.status === 200) {
                this.project = response.data.data;
                this.project.id = id;

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
    --bar-height: 42px;
    @apply overflow-y-hidden;
}

</style>