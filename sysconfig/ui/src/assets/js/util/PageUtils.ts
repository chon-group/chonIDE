import axios from "axios";

export default class PageUtils {

    private static TITLE_SUFFIX = " - chonide";

    public static setTitle(name: string) {
        document.title = name + this.TITLE_SUFFIX;
    }

    public static async isFirstAccess(){
        return axios.get("/chonide/users/first-access")
    }

}