package org.masos.embed.sysconfig.model;

public enum AgentArchClassEnum {

    JASON("Jason"),
    ARGO("Argo"),
    COMMUNICATOR("Communicator"),

    ;
    private String name;

    AgentArchClassEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean has(String name) {
        for (AgentArchClassEnum value : values()) {
            if (value.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
