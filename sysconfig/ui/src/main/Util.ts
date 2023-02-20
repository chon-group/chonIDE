export default class Util {

    private static TITLE_SUFFIX = " - chonide";

    public static setTitle(name: string) {
        document.title = name + this.TITLE_SUFFIX;
    }

}



