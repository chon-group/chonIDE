<script>
import ExplorerFolder from "@/views/Project/explorer/ExplorerFolder.vue";
import ExplorerFile from "@/views/Project/explorer/ExplorerFile.vue";
import {AgentType, AppEvent, MessageType} from "@/domain/Enums";
import defaultSourceCode from "@/domain/content/default-source-codes.json";
import Util from "@/domain/Util";
import {API, EndPoints, Headers} from "@/domain/API";

const AGENT_DEFAULT_FILE_NAME = "newAgent", FIRMWARE_DEFAULT_FILE_NAME = "newSketch";

export default {
    name: "Explorer",
    components: {ExplorerFile, ExplorerFolder},
    props: {
        configuration: {},
        agents: [],
        firmwares: [],
        currentFile: {}
    },
    data() {
        return {
            libraries: [],
            firmwareFileIsOpen: true,
            agentFileIsOpen: true
        }
    },
    mounted() {
        this.loadLibraries();
    },
    methods: {
        setCurrentFile(file) {
          this.$emit("setCurrentFile", file);
        },
        addAgentFileAction() {
            this.project.agents.push({
                name: AGENT_DEFAULT_FILE_NAME + (this.project.agents.length === 0 ? '' : this.project.agents.length + 1),
                archClass: AgentType.JASON,
                sourceCode: defaultSourceCode.agent
            });
        },
        addFirmwareFileAction() {
            this.project.firmwares.push({
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
                        this.agentFileIsOpen = false;
                        this.firmwareFileIsOpen = false;
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

        <ExplorerFolder v-if="configuration.reasoningLayer" name="Multi-Agent System" :has-add="false" icon="sma.svg"
                        icon-ratio="13px" :has-download="true" @download="$emit('downloadMas')">
            <template v-slot:content>

                <ExplorerFolder name="Agents" @add="addAgentFileAction" add-message="New agent" icon="agents.svg"
                                icon-ratio="15px">
                    <template v-slot:content>
                        <ExplorerFile v-for="(agent, index) in agents"
                                      :key="index" :file="agent"
                                      :icon="getAgentIcon(agent.archClass)"
                                      icon-ratio="13px"
                                      @delete="removeFileAction(index, agents)"
                                      @edit="(editedAgent) => agent = editedAgent"
                                      :selected="currentFile === agent"
                                      ref="agents"
                                      @show="
                                        setCurrentFile(agent);
                                        firmwareFileIsOpen = false;
                                        agentFileIsOpen = true;
                        "/>

                    </template>
                </ExplorerFolder>

            </template>
        </ExplorerFolder>

        <ExplorerFolder v-if="configuration.firmwareLayer" name="Firmware" @add="addFirmwareFileAction"
                        add-message="New firmware"
                        icon="firmwares.svg" icon-ratio="12px">
            <template v-slot:content>

                <ExplorerFile v-for="(firmware, index) in firmwares"
                              :key="index" :file="firmware"
                              @delete="removeFileAction(index, firmwares)"
                              @edit="(editedFirmware) => firmware = editedFirmware"
                              icon="ino.svg"
                              icon-ratio="17px"
                              ref="firmwares"
                              :selected="currentFile === firmware"
                              @show="
                                  setCurrentFile(firmware);
                                  firmwareFileIsOpen = true;
                                  agentFileIsOpen = false;
                                "/>

                <ExplorerFolder v-if="configuration.firmwareLibraries" name="Libraries"
                                @add="importLibrary"
                                icon="libraries.svg"
                                icon-ratio="12px"
                                add-message="New library" has-refresh @refresh="loadLibraries(true)">
                    <template v-slot:content>

                        <ExplorerFile v-for="(library, index) in libraries"
                                      :key="index" :file="library" :can-rename="false"
                                      :can-delete="library.name !== 'Javino'"
                                      @delete="deleteLibrary(library)"
                                      icon="library.svg"
                                      icon-ratio="11px"/>

                    </template>
                </ExplorerFolder>
            </template>
        </ExplorerFolder>
    </div>
</template>