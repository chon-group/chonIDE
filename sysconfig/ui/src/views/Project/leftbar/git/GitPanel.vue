<template>
    <div class="git-panel">
      <div class="git-panel__header-bar">
        <span class="git-panel__header-bar__title">Git Panel</span>
      </div>
  
      <div class="git-panel__content">
        <span>Git Command</span>
        <select v-model="selectedCommand" class="git-panel__dropdown">
          <option v-for="cmd in commands" :key="cmd.action" :value="cmd.action">
            {{ cmd.label }}
          </option>
        </select>
  
        <div class="git-panel__inputs">
          <div v-if="selectedCommand === 'add'" class="git-panel__field">
            <label class="git-panel__input-label" for="files">Files (comma separated)</label>
            <input
              id="files"
              v-model="files"
              type="text"
              placeholder="Enter file names separated by commas"
              class="git-panel__input"
            />
          </div>
  
          <div v-if="selectedCommand === 'commit'" class="git-panel__field">
            <label class="git-panel__input-label" for="commitMessage">Commit Message</label>
            <input
              id="commitMessage"
              v-model="commitMessage"
              type="text"
              placeholder="Enter commit message"
              class="git-panel__input"
            />
          </div>
  
          <div v-if="selectedCommand === 'push' || selectedCommand === 'createBranch' || selectedCommand === 'checkout' || selectedCommand === 'merge' || selectedCommand === 'pull' || selectedCommand === 'rebase'" class="git-panel__field">
            <label class="git-panel__input-label" for="branch">Branch Name</label>
            <input
              id="branch"
              v-model="branch"
              type="text"
              placeholder="Enter branch name"
              class="git-panel__input"
            />
          </div>
  
          <div v-if="selectedCommand === 'push' || selectedCommand === 'pull' || selectedCommand === 'clone' || selectedCommand === 'addRemote'" class="git-panel__field">
            <label class="git-panel__input-label" for="remote">Remote URL</label>
            <input
              id="remote"
              v-model="remote"
              type="text"
              placeholder="Enter remote URL"
              class="git-panel__input"
            />
          </div>
  
          <div v-if="selectedCommand === 'addRemote'" class="git-panel__field">
            <label class="git-panel__input-label" for="remoteName">Remote Name</label>
            <input
              id="remoteName"
              v-model="remoteName"
              type="text"
              placeholder="Enter remote name"
              class="git-panel__input"
            />
          </div>
  
          <div v-if="selectedCommand === 'createTag'" class="git-panel__field">
            <label class="git-panel__input-label" for="tagName">Tag Name</label>
            <input
              id="tagName"
              v-model="tagName"
              type="text"
              placeholder="Enter tag name"
              class="git-panel__input"
            />
          </div>
        </div>
  
        <Button
          :text="'Executar ' + selectedLabel"
          color="var(--pallete-color-blue)"
          :mainColor="true"
          @click="executeCommand"
        />
  
        <div v-if="response" class="git-panel__response">
          <h4>Response:</h4>
          <strong>{{ command }}</strong>
          <pre>{{ response }}</pre>
        </div>
      </div>
      <Dragger top />
    </div>
  </template>
  
  <script>
  import Button from "@/components/general/Button.vue";
  import Dragger from "@/components/general/Dragger.vue";
  import { Api } from "@/services/chonide/api";
  import { EndPoints } from "@/services/chonide/endPoints";
  
  const commandMapping = {
    status: { method: "post", url: EndPoints.GIT_STATUS },
    log: { method: "post", url: EndPoints.GIT_LOG },
    listBranches: { method: "post", url: EndPoints.GIT_LIST_BRANCHES },
    listRemotes: { method: "post", url: EndPoints.GIT_LIST_REMOTES },
    listTags: { method: "post", url: EndPoints.GIT_LIST_TAGS },
    init: { method: "post", url: EndPoints.GIT_INIT },
    add: { method: "post", url: EndPoints.GIT_ADD },
    commit: { method: "post", url: EndPoints.GIT_COMMIT },
    push: { method: "post", url: EndPoints.GIT_PUSH },
    createBranch: { method: "post", url: EndPoints.GIT_CREATE_BRANCH },
    merge: { method: "post", url: EndPoints.GIT_MERGE },
    pull: { method: "post", url: EndPoints.GIT_PULL },
    clone: { method: "post", url: EndPoints.GIT_CLONE },
    stash: { method: "post", url: EndPoints.GIT_STASH },
    rebase: { method: "post", url: EndPoints.GIT_REBASE },
    addRemote: { method: "post", url: EndPoints.GIT_ADD_REMOTE },
    createTag: { method: "post", url: EndPoints.GIT_CREATE_TAG },
    checkout: { method: "post", url: EndPoints.GIT_CHECKOUT },
    applyStash: { method: "post", url: EndPoints.GIT_APPLY_STASH }
  };
  
  export default {
    name: "GitPanel",
    components: { Dragger, Button },
    props: {
      project: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        commitMessage: "",
        remote: "",
        branch: "",
        files: "",
        remoteName: "",
        tagName: "",
        username: "",
        token: "",
        selectedCommand: "status",
        response: null,
        command: null,
        commands: [
          { label: "git status", action: "status" },
          { label: "git init", action: "init" },
          { label: "git add", action: "add" },
          { label: "git commit", action: "commit" },
          { label: "git push", action: "push" },
          { label: "git log", action: "log" },
          { label: "git branch (list)", action: "listBranches" },
          { label: "git branch (create)", action: "createBranch" },
          { label: "git checkout", action: "checkout" },
          { label: "git merge", action: "merge" },
          { label: "git pull", action: "pull" },
          { label: "git clone", action: "clone" },
          { label: "git stash", action: "stash" },
          { label: "git stash apply", action: "applyStash" },
          { label: "git rebase", action: "rebase" },
          { label: "git remote (list)", action: "listRemotes" },
          { label: "git remote (add)", action: "addRemote" },
          { label: "git tag (list)", action: "listTags" },
          { label: "git tag (create)", action: "createTag" }
        ]
      };
    },
    computed: {
      selectedLabel() {
        const cmd = this.commands.find(c => c.action === this.selectedCommand);
        return cmd ? cmd.label : "";
      }
    },
    methods: {
      executeCommand() {
        this.command = "git " + this.selectedCommand
        const params = {
          projectName: this.project ? this.project.name : "",
          action: this.selectedCommand
        };
        if (!params.projectName) {
          this.response = "Projeto não informado.";
          return;
        }
  
        // git add
        if (this.selectedCommand === "add") {
          this.command = this.command + " " + this.files;
          params.files = this.files;
        }
  
        // git commit
        if (this.selectedCommand === "commit") {
          this.command = this.command + " -m \"" + this.commitMessage + "\"";
          params.message = this.commitMessage;
        }
  
        // git push, git pull
        if (this.selectedCommand === "push" || this.selectedCommand === "pull") {
          this.command = this.command + " " + this.remote + " " + this.branch
          params.remoteURL = this.remote;
          params.branchName = this.branch;
        }
  
        // git branch (list)
        if (this.selectedCommand === "listBranches") {
          this.command = "git branch";
        }
  
        // git merge, git checkout e git rebase
        if (
          this.selectedCommand === "merge" ||
          this.selectedCommand === "checkout" ||
          this.selectedCommand === "rebase"
        ) {
          this.command = this.command + " " + this.branch;
          params.branchName = this.branch;
        }
  
        // git branch (create)
        if (this.selectedCommand === "createBranch") {
          this.command = "git branch " + this.branch;
          params.branchName = this.branch;
        }
  
        // git clone
        if (this.selectedCommand === "clone") {
          this.command = this.command + " " + this.remote + " /" + params.projectName;
          params.remoteURL = this.remote;
        }
  
        // git stash apply
        if (this.selectedCommand === "applyStash") {
          this.command = "git stash apply";
        }
  
        // git remote (list)
        if (this.selectedCommand === "listRemotes") {
          this.command = "git remote -v";
        }
  
        // git remote add
        if (this.selectedCommand === "addRemote") {
          this.command = "git remote add " + this.remoteName + " " + this.remote
          params.remoteName = this.remoteName;
          params.remoteURL = this.remote;
        }
  
        // git tag (list)
        if (this.selectedCommand === "listTags") {
          this.command = "git tag";
        }
  
        // git tag (create)
        if (this.selectedCommand === "createTag") {
          this.command = "git tag " + this.tagName;
          params.tagName = this.tagName;
        }
  
        const commandConfig = commandMapping[this.selectedCommand];
        if (!commandConfig) {
          this.response = "Comando inválido.";
          return;
        }
  
        Api.post(EndPoints.GIT, {params})
          .then(result => {
            this.response = result.data.message;
          })
          .catch(error => {
            this.response =
              "Error: " +
              (error.response &&
              error.response.data &&
              error.response.data.message
                ? error.response.data.message
                : error.message);
          });
      }
    }
  };
  </script>
  
  <style>
  .git-panel {
    height: 1000px;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    background-color: var(--pallete-color-black-1);
    border-top: 1px solid var(--pallete-color-black-4);
    min-height: 40px;
    position: relative;
  }
  
  .git-panel__header-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0.5rem 1rem;
    background-color: var(--pallete-color-black);
    border-bottom: 1px solid var(--pallete-color-black-4);
    height: 40px;
  }
  
  .git-panel__content {
    display: flex;
    flex-direction: column;
    padding: 1rem;
    gap: 0.75rem;
    min-width: 300px;
    overflow-y: auto;
    flex-grow: 1;
  }
  
  .git-panel__dropdown {
    width: 100%;
    background-color: var(--pallete-color-black-3);
    color: var(--pallete-color-white);
    border: 1px solid var(--pallete-color-black-4);
    border-radius: 4px;
    padding: 0.5rem;
  }
  
  .git-panel__inputs {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .git-panel__field {
    display: flex;
    flex-direction: column;
  }
  
  .git-panel__input {
    width: 100%;
    padding: 0.5rem;
    color: var(--pallete-color-white);
    background-color: var(--pallete-color-black-3);
    border: 1px solid var(--pallete-color-black-4);
    border-radius: 4px;
    margin-bottom: 0.5rem;
  }
  
  .git-panel__input-label {
    margin-bottom: 0.5rem;
  }
  
  .git-panel__response {
    margin-top: 1rem;
    margin-bottom: 2rem;
    background-color: var(--pallete-color-black-1);
    color: var(--pallete-color-white);
    border: 1px solid var(--pallete-color-black-4);
    border-radius: 4px;
    padding: 1rem;
    font-size: 0.875rem;
    line-height: 1.5;
  }
  
  .git-panel__response h4 {
    margin-bottom: 0.5rem;
    font-size: 0.875rem;
    font-weight: 600;
  }
  
  .git-panel__response pre {
    font-size: 0.75rem;
    line-height: 1.4;
    white-space: pre-wrap;
  }
  </style>
  