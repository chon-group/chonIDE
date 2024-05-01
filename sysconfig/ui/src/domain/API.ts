import axios from "axios";
import {uuid} from "vue-uuid"
import router from "@/router";
import {Routes} from "@/router/routes";


export class API {

    private static NAMESPACE = "65f9af5d-f23f-4065-ac85-da725569fdcd";

    public static auth(username: string, password: string, host: string) {
        return axios.post(EndPoints.AUTH, {}, {
            params: {
                username: username,
                password: password,
                host: host
            }
        }).then((response) => {
            if (response.status == 200) {
                axios.defaults.headers.common["Authorization"] = "Bearer " + response.data.data;
                localStorage.setItem(uuid.v5("token", API.NAMESPACE), response.data.data);
            }
            return response;
        });
    }

    public static loadToken() {
        const token = localStorage.getItem(uuid.v5("token", API.NAMESPACE));
        axios.defaults.headers.common["Authorization"] = "Bearer " + token;
    }

    public static get(url: string, config = {}) {
        return this.validate(axios.get(url, config)).then((response) => {
            if (response.status >= 200 && response.status < 300) {
                try {
                    response.data.data = JSON.parse(response.data.data);
                } catch (ignored) {
                    //
                }
            }
            return response;
        });
    }

    public static post(url: string, config = {}, data = {}) {
        return this.validate(axios.post(url, data, config));
    }

    public static put(url: string, config = {}, data = {}) {
        return this.validate(axios.put(url, data, config));
    }

    public static delete(url: string, config = {}) {
        return this.validate(axios.delete(url, config));
    }

    private static validate(requisition: Promise<any>): Promise<any> {
        return requisition.catch((error) => {
            if (error.response.status == 401) {
                router.push(Routes.LOGIN);
            }
            throw error;
        });
    }

}

const URL = "https://labredes.bot.chon.group:3270/chonide";
const API_PREFIX = "/api";
const API_URL = URL + API_PREFIX;

export class EndPoints {
    public static AUTH = URL + "/auth";
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

export class Headers {
    public static MULTIPART_CONFIG = {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    };
}


