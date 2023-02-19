package org.masos.embed.sysconfig.domain.file.model;

import java.util.List;

public class Project {

    private String name;

    private List<Agent> agents;

    private List<Firmware> firmwares;

    public Project(String name, List<Agent> agents, List<Firmware> firmwares) {
        this.name = name;
        this.agents = agents;
        this.firmwares = firmwares;
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
     * @return {@link #agents}
     */
    public List<Agent> getAgents() {
        return this.agents;
    }

    /**
     * @param agents {@link #agents}
     */
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    /**
     * @return {@link #firmwares}
     */
    public List<Firmware> getFirmwares() {
        return this.firmwares;
    }

    /**
     * @param firmwares {@link #firmwares}
     */
    public void setFirmwares(List<Firmware> firmwares) {
        this.firmwares = firmwares;
    }
}
