export default class Util {

    private static TITLE_SUFFIX = " - chonIDE";

    public static setTitle(name: string) {
        document.title = name + this.TITLE_SUFFIX;
    }

    public static mantainJustRegularCharacters(string: string) {
        return string.replace(/[^a-zA-Z0-9]/g, "");
    }

    public static mantainJustLetters(string: string) {
        return string.replace(/[^a-zA-Z]/g, "");
    }

    public static isFileInvalid(file: any) {
        return file.name.includes(" ");
    }

}



