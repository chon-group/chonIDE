<script>
import ExplorerFolder from "@/views/Project/explorer/ExplorerFolder.vue";
import ExplorerFile from "@/views/Project/explorer/ExplorerFile.vue";
import {AgentType, AppEvent, FileType, MessageType} from "@/domain/Enums";
import defaultSourceCode from "@/domain/content/default-source-codes.json";
import Util from "@/domain/Util";
import {API, EndPoints, Headers} from "@/domain/API";

const AGENT_DEFAULT_FILE_NAME = "newAgent", FIRMWARE_DEFAULT_FILE_NAME = "newSketch";

export default {
    name: "Explorer",
    computed: {
        FileType() {
            return FileType
        },
        JavinoLibraryName() {
            return "Javino";
        }
    },
    components: {ExplorerFile, ExplorerFolder},
    props: {
        configuration: {},
        project: {},
        currentFile: {}
    },
    data() {
        return {
            libraries: []
        }
    },
    mounted() {
        this.loadLibraries();
    },
    methods: {
        projectIsInvalid() {
            if (this.project.agents.length === 0) {
                this.$emit(AppEvent.MESSAGE, {content: "Unable to start SMA without agents", type: MessageType.ERROR});
                return true;
            }

            let hasSameName = false;
            for (let agent in this.project.agents) {
                for (let anotherAgent in this.project.agents) {
                    if (agent !== anotherAgent && agent.name === anotherAgent.name) {
                        hasSameName = true;
                        break;
                    }
                }
                if (hasSameName) {
                    return;
                }
            }

            if (hasSameName) {
                this.$emit(AppEvent.MESSAGE, {content: "There are agents with the same name", type: MessageType.ERROR});
                return true;
            }

            return false;
        },
        downloadMas() {
            if (this.projectIsInvalid()) {
                return;
            }
            API.post(EndPoints.MAS, {responseType: 'blob'}, this.project).then((response) => {
                if (response.status === 200) {
                    const filename = this.project.name + ".zip";
                    const blob = new Blob([response.data]);

                    const url = window.URL.createObjectURL(blob);

                    const a = document.createElement('a');
                    a.style.display = 'none';
                    a.href = url;
                    a.download = filename;
                    document.body.appendChild(a);
                    a.click();

                    window.URL.revokeObjectURL(url);
                }
            });
        },
        setCurrentFile(file) {
          this.$emit("setCurrentFile", file);
        },
        addAgentFileAction() {
            const agents = this.project.agents;
            agents.push( {
                name: AGENT_DEFAULT_FILE_NAME + (this.project.agents.length === 0 ? '' : this.project.agents.length +
                 1),
                archClass: AgentType.JASON,
                sourceCode: defaultSourceCode.agent
            });
        },
        addFirmwareFileAction() {
            const firmwares = this.project.firmwares;
            firmwares.push({
                name: FIRMWARE_DEFAULT_FILE_NAME,
                sourceCode: defaultSourceCode.firmware
            });
        },
        removeFileAction(index, files) {
            if (this.currentFile === files[index]) {
                if (index === 0) {
                    files.splice(index, 1);
                    if (files.length === 0) {
                        this.setCurrentFile({name: "No file", sourceCode: ""});
                    }
                } else {
                    this.setCurrentFile(files[index - 1]);
                    files.splice(index, 1);
                }
            } else {
                files.splice(index, 1);
            }
        },
        importLibrary() {
            let libraryInput = document.createElement('input');
            libraryInput.type = 'file';
            libraryInput.click();
            libraryInput.onchange = (event) => {
                let files = event.target.files;
                if (files.length === 0) {
                    this.$emit(AppEvent.MESSAGE, {content: "No file selected", type: MessageType.WARNING});
                    return;
                }
                let libraryFile = files[0];
                if (Util.isFileInvalid(libraryFile)) {
                    this.$emit(AppEvent.MESSAGE, {content: "File name cannot have space", type: MessageType.ERROR});
                    return;
                }
                API.post(EndPoints.LIBRARIES_IMPORT, Headers.MULTIPART_CONFIG, {file: libraryFile}).then((response) => {
                    if (response.data.status === 200) {
                        this.$emit(AppEvent.MESSAGE, {
                            content: response.data.data,
                            type: MessageType.SUCCESS
                        });
                        this.loadLibraries(true);
                    } else {
                        this.$emit(AppEvent.MESSAGE, {
                            content: response.data.data,
                            type: MessageType.ERROR
                        });
                    }
                });
            }
        },
        loadLibraries(refresh = false) {
            API.get(EndPoints.LIBRARIES, refresh).then((response) => {
                this.libraries = response.data.data;
            });
        },
        deleteLibrary(library) {
            API.delete(EndPoints.LIBRARIES, {params: {name: library.name}}).then(() => {
                this.loadLibraries(true);
            }).catch(() => {
                this.$emit(AppEvent.MESSAGE, {content: `It was not possible delete library ${library.name}`, type:
                    MessageType.ERROR});
            });
        },
        getAgentIcon(agentType) {
            if (agentType === "Argo") {
                return "argo-agent.svg";
            } else if (agentType === "Communicator") {
                return "communicator-agent.svg";
            } else {
                return "jason-agent.svg";
            }
        },
    }
}
</script>

<template>
    <div class="overflow-y-auto p-1.5 h-full">

        <ExplorerFolder
                v-if="configuration.reasoningLayer"

                name="Multi-Agent System"
                :has-add="false"
                icon="sma.svg"
                icon-ratio="13px"
                :has-download="true"

                @download="downloadMas"
        >
            <template v-slot:content>

                <ExplorerFolder
                        name="Agents"
                        add-message="New agent"
                        icon="agents.svg"
                        icon-ratio="15px"

                        @add="addAgentFileAction"
                >
                    <template v-slot:content>
                        <ExplorerFile
                                v-for="(agent, index) in project.agents" :key="index" :file="agent"
                                :icon="getAgentIcon(agent.archClass)"
                                icon-ratio="13px"
                                :selected="currentFile === agent"
                                ref="agents"

                                @delete="removeFileAction(index, project.agents)"
                                @edit="(editedAgent) => agent = editedAgent"
                                @show="
                                    setCurrentFile(agent);
                                    $emit('fileType', FileType.AGENT);
                                "

                        />

                    </template>
                </ExplorerFolder>

            </template>
        </ExplorerFolder>

        <ExplorerFolder
                v-if="configuration.firmwareLayer"
                name="Firmware"
                add-message="New firmware"
                icon="firmwares.svg"
                icon-ratio="12px"

                @add="addFirmwareFileAction"
        >
            <template v-slot:content>

                <ExplorerFile
                        v-for="(firmware, index) in project.firmwares" :key="index"
                        :file="firmware"
                        icon="ino.svg"
                        icon-ratio="17px"
                        ref="firmwares"
                        :selected="currentFile === firmware"

                        @delete="removeFileAction(index, project.firmwares)"
                        @edit="(editedFirmware) => firmware = editedFirmware"
                        @show="
                            setCurrentFile(firmware);
                            $emit('fileType', FileType.FIRMWARE)
                        "
                />

                <ExplorerFolder
                        v-if="configuration.firmwareLibraries" name="Libraries"
                        add-message="New library" has-refresh @refresh="loadLibraries(true)"
                        icon="libraries.svg"
                        icon-ratio="12px"

                        @add="importLibrary"
                >
                    <template v-slot:content>

                        <ExplorerFile
                                v-for="(library, index) in libraries" :key="index"
                                :file="library"
                                :can-rename="false"
                                :can-delete="library.name !== JavinoLibraryName"
                                icon="library.svg"
                                icon-ratio="11px"

                                @delete="deleteLibrary(library)"
                        />

                    </template>
                </ExplorerFolder>
            </template>
        </ExplorerFolder>
    </div>
</template>