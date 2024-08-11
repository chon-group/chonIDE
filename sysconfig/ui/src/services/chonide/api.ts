import axios from "axios";
import {uuid} from "vue-uuid"
import router from "@/router";
import {Routes} from "@/router/routes";
import {EndPoints} from "@/services/chonide/endPoints";

export class DataHandler {

    static configuration: any;

}

export class Api {

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
                localStorage.setItem(uuid.v5("token", Api.NAMESPACE), response.data.data);
            }
            return response;
        });
    }

    public static loadToken() {
        const token = localStorage.getItem(uuid.v5("token", Api.NAMESPACE));
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
            if (error.response.status === 401) {
                router.push(Routes.LOGIN);
            }
            throw error;
        });
    }

}