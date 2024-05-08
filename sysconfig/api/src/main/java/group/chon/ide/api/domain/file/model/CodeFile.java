package group.chon.ide.api.domain.file.model;

public class CodeFile {

    private String name;

    private String sourceCode;

    public CodeFile(String name) {
        this.name = name;
    }

    public CodeFile(String name, String sourceCode) {
        this.name = name;
        this.sourceCode = sourceCode;
    }

    /**
     * @return {@link #name}
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name {@link #name}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return {@link #sourceCode}
     */
    public String getSourceCode() {
        return this.sourceCode;
    }

    /**
     * @param sourceCode {@link #sourceCode}
     */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
