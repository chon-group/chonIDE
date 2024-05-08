package group.chon.ide.api.domain.file.model;

import java.util.List;

public class Project {

    private Long id;

    private String name;

    private List<Agent> agents;

    private List<Firmware> firmwares;

    public Project(Long id, String name, List<Agent> agents, List<Firmware> firmwares) {
        this.id = id;
        this.name = name;
        this.agents = agents;
        this.firmwares = firmwares;
    }

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

    /**
     * @return {@link #id}
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id {@link #id}
     */
    public void setId(Long id) {
        this.id = id;
    }
}
