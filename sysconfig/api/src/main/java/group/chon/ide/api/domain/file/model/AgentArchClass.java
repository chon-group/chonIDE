package group.chon.ide.api.domain.file.model;

public enum AgentArchClass {

    JASON("Jason"), ARGO("Argo"), COMMUNICATOR("Communicator");

    private String name;

    AgentArchClass(String name) {
        this.name = name;
    }

    public static boolean has(String name) {
        for (AgentArchClass value : values()) {
            if (value.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

}
