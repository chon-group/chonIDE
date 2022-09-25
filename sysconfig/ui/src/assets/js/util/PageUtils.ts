import axios from "axios";

export default class PageUtils {

    private static TITLE_SUFFIX = " - sysconfig";

    public static setTitle(name: string) {
        document.title = name + this.TITLE_SUFFIX;
    }

    public static async isLogged() {
        return axios.get("/sysconfig/users");
    }

    public static async isFirstAccess(){
        return axios.get("/sysconfig/users/first-access")
    }

}