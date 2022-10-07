package org.masos.embed.sysconfig.model.dto;

import org.masos.embed.sysconfig.model.Agent;

import java.util.List;

public class MasDTO extends DTO {

    private String masName;

    private List<Agent> agents;

    private String masStructure;

    private String masCompleteStructure;

    public MasDTO(String masName, List<Agent> agents, String masStructure, String masCompleteStructure) {
        this.masName = masName;
        this.agents = agents;
        this.masStructure = masStructure;
        this.masCompleteStructure = masCompleteStructure;
    }

    public String getMasName() {
        return masName;
    }

    public void setMasName(String masName) {
        this.masName = masName;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public String getMasStructure() {
        return masStructure;
    }

    public void setMasStructure(String masStructure) {
        this.masStructure = masStructure;
    }

    public String getMasCompleteStructure() {
        return masCompleteStructure;
    }

    public void setMasCompleteStructure(String masCompleteStructure) {
        this.masCompleteStructure = masCompleteStructure;
    }
}
