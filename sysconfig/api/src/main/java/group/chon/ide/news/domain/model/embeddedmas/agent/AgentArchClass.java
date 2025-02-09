package group.chon.ide.news.domain.model.embeddedmas.agent;

/**
 * Defines the architecture classes an agent can have.
 */
public enum AgentArchClass {

    JASON("Jason"), ARGO("Argo"), COMMUNICATOR("Communicator");

    private final String name;

    /**
     * Constructs an AgentArchClass with the given name.
     * 
     * @param name The name of the architecture class.
     */
    AgentArchClass(String name) {
        this.name = name;
    }

    /**
     * Checks if a given name matches any existing architecture class.
     * 
     * @param name The name to check.
     * @return {@code true} if the name exists, {@code false} otherwise.
     */
    public static boolean has(String name) {
        for (AgentArchClass value : values()) {
            if (value.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the name of the architecture class.
     * 
     * @return The architecture class name.
     */
    public String getName() {
        return name;
    }
}
