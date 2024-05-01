import {backendUrl} from "@/env";

const API_PREFIX = "/api";
const API_URL = backendUrl + API_PREFIX;

export class EndPoints {
    public static AUTH = backendUrl + "/auth";
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
    public static CONFIGURATION = API_URL + "/system/configuration";
}