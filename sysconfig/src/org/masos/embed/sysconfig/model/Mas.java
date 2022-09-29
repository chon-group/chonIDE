package org.masos.embed.sysconfig.model;

import java.io.Serializable;
import java.util.List;

public class Mas implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private List<Agent> agents;

    public Mas(String name, List<Agent> agents) {
        this.name = name;
        this.agents = agents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

}
