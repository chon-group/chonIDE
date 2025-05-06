<script>
/* eslint-disable */

import ExplorerFolder from "@/views/Project/leftbar/explorer/ExplorerFolder.vue";
import ExplorerFile from "@/views/Project/leftbar/explorer/ExplorerFile.vue";
import {AppEvent, FileType, MessageType} from "@/utils/enums";
import defaultAgentSourceCode from "raw-loader!@/assets/text/default-agent-source-code.txt";
import defaultFirmwareSourceCode from "raw-loader!@/assets/text/default-firmware-source-code.txt";
import {Api} from "@/services/chonide/api";
import {EndPoints} from "@/services/chonide/endPoints";
import {Headers} from "@/services/request";
import validateProject from "@/views/Project/util";
import Dragger from "@/components/general/Dragger.vue";

const AGENT_DEFAULT_FILE_NAME = "newAgent", FIRMWARE_DEFAULT_FILE_NAME = "newSketch";
const DEFAULT_AGENT_TYPE_NAME = "Jason";

export default {
  name: "Explorer",
  components: { Dragger },
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
    downloadMas() {
      if (validateProject(this.$emit, this.project)) {
        return;
      }
      Api.post(EndPoints.MAS, {responseType: 'blob'}, this.project).then((response) => {
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
    addAgentFileAction() {
      this.$emit("addAgent", {
        name: AGENT_DEFAULT_FILE_NAME + (this.project.agents.length === 0 ? '' : this.project.agents.length +
            1),
        archClass: DEFAULT_AGENT_TYPE_NAME,
        sourceCode: defaultAgentSourceCode
      });
    },
    addFirmwareFileAction() {
      this.$emit("addFirmware", {
        name: FIRMWARE_DEFAULT_FILE_NAME,
        sourceCode: defaultFirmwareSourceCode
      });
    },
    isFileInvalid(file) {
      return file.name.includes(" ")
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
        if (this.isFileInvalid(libraryFile)) {
          this.$emit(AppEvent.MESSAGE, {content: "File name cannot have space", type: MessageType.ERROR});
          return;
        }

        Api.post(EndPoints.LIBRARIES_IMPORT, Headers.MULTIPART_CONFIG, {file: libraryFile}).then((response) => {
          if (response.data.status === 200) {
            this.$emit(AppEvent.MESSAGE, {
              content: response.data.data,
              type: MessageType.SUCCESS
            });
            this.loadLibraries(true);
          }
        }).catch((response) => {
          this.$emit(AppEvent.MESSAGE, {
            content: response.response.data.data,
            type: MessageType.ERROR
          });
        });
      }
    },
    loadLibraries(refresh = false) {
      Api.get(EndPoints.LIBRARIES, refresh).then((response) => {
        this.libraries = response.data.data;
      });
    },
    deleteLibrary(library) {
      Api.delete(EndPoints.LIBRARIES, {params: {name: library.name}}).then(() => {
        this.loadLibraries(true);
      }).catch(() => {
        this.$emit(AppEvent.MESSAGE, {
          content: `It was not possible delete library ${library.name}`, type:
          MessageType.ERROR
        });
      });
    },
    getAgentIcon(agentType) {
      if (agentType === "jason.Argo") {
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
  <div class="explorer">
    <ExplorerFolder
        v-if="configuration.reasoningLayer"

        :has-add="false"
        :has-download="true"
        icon="sma.svg"
        icon-ratio="13px"
        name="Multi-Agent System"

        @download="downloadMas"
    >
      <template v-slot:content>

        <ExplorerFolder
            :class="currentFile == null ? 'select' : ''"
            add-message="New agent"
            icon="agents.svg"
            icon-ratio="15px"

            name="Agents"

            @add="addAgentFileAction"
        >
          <template v-slot:content>
            <ExplorerFile
                v-for="(agent, index) in project.agents" :key="index" ref="agents"
                :file="agent"
                :icon="getAgentIcon(agent.archClass)"
                :selected="currentFile === agent"
                icon-ratio="13px"

                @delete="$emit('removeAgent', index)"
                @edit="(editedAgent) => agent = editedAgent"
                @show="
                                    $emit('setCurrentFile', agent);
                                    $emit('setFileType', FileType.AGENT);
                                "

            />

          </template>
        </ExplorerFolder>

      </template>
    </ExplorerFolder>
    <ExplorerFolder
        v-if="configuration.firmwareLayer"
        add-message="New firmware"
        icon="firmwares.svg"
        icon-ratio="12px"
        name="Firmware"

        @add="addFirmwareFileAction"
    >
      <template v-slot:content>

        <ExplorerFile
            v-for="(firmware, index) in project.firmwares" :key="index"
            ref="firmwares"
            :file="firmware"
            :selected="currentFile === firmware"
            icon="ino.svg"
            icon-ratio="17px"

            @delete="$emit('removeFirmware', index)"
            @edit="(editedFirmware) => firmware = editedFirmware"
            @show="
                            $emit('setCurrentFile', firmware);
                            $emit('setFileType', FileType.FIRMWARE)
                        "
        />

        <ExplorerFolder
            v-if="configuration.firmwareLibraries" add-message="New library"
            has-refresh icon="libraries.svg" icon-ratio="12px"
            name="Libraries"
            @add="importLibrary"

            @refresh="loadLibraries(true)"
        >
          <template v-slot:content>

            <ExplorerFile
                v-for="(library, index) in libraries" :key="index"
                :can-delete="library.name !== JavinoLibraryName"
                :can-rename="false"
                :file="library"
                icon="library.svg"
                icon-ratio="11px"

                @delete="deleteLibrary(library)"
            />

          </template>
        </ExplorerFolder>
      </template>
    </ExplorerFolder>
    <Dragger bottom />
  </div>
</template>

<style scoped>

.explorer {
  min-width: 300px;
  height: 100px;
  @apply overflow-y-auto p-1.5 w-full flex-grow;
}

</style>