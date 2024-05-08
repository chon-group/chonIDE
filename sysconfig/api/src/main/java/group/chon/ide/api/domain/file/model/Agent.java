package group.chon.ide.api.domain.file.model;

public class Agent extends CodeFile {

    private String archClass;

    public Agent(String name, String archClass) {
        super(name);
        this.archClass = archClass;
    }

    public Agent(String name, String archClass, String sourceCode) {
        super(name, sourceCode);
        this.archClass = archClass;
    }

    /**
     * @return {@link #archClass}
     */
    public String getArchClass() {
        return this.archClass;
    }

    /**
     * @param archClass {@link #archClass}
     */
    public void setArchClass(String archClass) {
        this.archClass = archClass;
    }
}
