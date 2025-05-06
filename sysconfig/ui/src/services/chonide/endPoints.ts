import {backendUrl} from "@/env";

const API_PREFIX = "/api";
const API_URL = backendUrl + API_PREFIX;

export class EndPoints {
    public static AUTH = backendUrl + "/auth";
    public static NEIGHBORS = backendUrl + "/neighbors";
    public static ATTRIBUTES = backendUrl + "/system/attributes";

    public static CONFIGURATION = API_URL + "/system/configuration";
    public static PROJECTS = API_URL + "/projects";
    public static PROJECTS_IMPORT = API_URL + "/projects/import";
    public static DOMAINS = API_URL + "/domains";
    public static USERS = API_URL + "/users";
    public static USERS_FIRST_ACCESS = API_URL + "/users/first-access";
    public static BOARDS = API_URL + "/boards";
    public static LIBRARIES = API_URL + "/libraries";
    public static LIBRARIES_IMPORT = API_URL + "/libraries/import";
    public static SYSTEM_REBOOT = API_URL + "/system/reboot";
    public static SYSTEM_POWEROFF = API_URL + "/system/poweroff";
    public static MAS = API_URL + "/mas";
    public static SKETCH_DEPLOY = API_URL + "/sketchs/deploy";
    public static SKETCH_COMPILE = API_URL + "/sketchs/compile";
    public static NETWORKS = API_URL + "/networks";
    public static NETWORKS_STATUS = API_URL + "/networks/status";
    public static NETWORKS_AP = API_URL + "/networks/ap";
    public static NETWORKS_CLIENT = API_URL + "/networks/client";
    public static MINDINSPECTOR = API_URL + "/sma/agents"

    public static GIT = API_URL + "/git";
    public static GIT_STATUS = API_URL + "/git/status";
    public static GIT_LOG = API_URL + "/git/log";
    public static GIT_LIST_BRANCHES = API_URL + "/git/listBranches";
    public static GIT_LIST_REMOTES = API_URL + "/git/listRemotes";
    public static GIT_LIST_TAGS = API_URL + "/git/listTags";
    public static GIT_INIT = API_URL + "/git/init";
    public static GIT_ADD = API_URL + "/git/add";
    public static GIT_COMMIT = API_URL + "/git/commit";
    public static GIT_PUSH = API_URL + "/git/push";
    public static GIT_CREATE_BRANCH = API_URL + "/git/createBranch";
    public static GIT_MERGE = API_URL + "/git/merge";
    public static GIT_PULL = API_URL + "/git/pull";
    public static GIT_CLONE = API_URL + "/git/clone";
    public static GIT_STASH = API_URL + "/git/stash";
    public static GIT_REBASE = API_URL + "/git/rebase";
    public static GIT_ADD_REMOTE = API_URL + "/git/addRemote";
    public static GIT_CREATE_TAG = API_URL + "/git/createTag";
    public static GIT_CHECKOUT = API_URL + "/git/checkout";
    public static GIT_APPLY_STASH = API_URL + "/git/applyStash";
}