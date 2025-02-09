package group.chon.ide.news.domain.model.embeddedmas.masconfiguration;

import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;
import group.chon.ide.news.domain.model.embeddedmas.agent.AgentArchClass;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a MAS2J configuration file used in projects with the Jason Embedded language.
 * This file defines the structure of a Multi-Agent System (MAS), including its agents and infrastructure.
 */
@RequiredArgsConstructor
public class Mas2j implements MasConfiguration {

    /**
     * File extension for MAS2J configuration files.
     */
    public static final String MAS_STRUCTURE_FILE_EXTENSION = ".mas2j";

    private static final String AGENT_ARCH_CLASS_FIELD = "agentArchClass";
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";
    private static final String TAB = "\t";
    private static final String COLON = ":";
    private static final String SEMI_COLON = ";";
    private static final String OPEN_BRACE = "{";
    private static final String CLOSE_BRACE = "}";

    private static final String MAS_FIELD = "MAS";
    private static final String INFRASTRUCTURE_FIELD = "infrastructure";
    private static final String AGENTS_FIELD = "agents";
    private static final String AGENT_SOURCE_PATH_FIELD = "aslSourcePath";

    private static final String DEFAULT_INFRASTRUCTURE = "Centralised";
    private static final String DEFAULT_AGENTS_SOURCE = "\"asl\"";

    /**
     * The name of the MAS configuration.
     */
    private final String masName;

    /**
     * List of agents included in the MAS configuration.
     */
    private final List<Agent> agents;

    /**
     * Generates the base structure of the MAS2J configuration file.
     * 
     * @return A {@link StringBuffer} containing the partial MAS2J configuration.
     */
    private StringBuffer getCommonStructure() {
        StringBuffer structure = new StringBuffer();
        structure.append(MAS_FIELD).append(SPACE).append(masName).append(SPACE).append(OPEN_BRACE).append(NEW_LINE);
        structure.append(TAB).append(INFRASTRUCTURE_FIELD).append(COLON).append(SPACE)
                 .append(DEFAULT_INFRASTRUCTURE).append(NEW_LINE);
        structure.append(TAB).append(AGENTS_FIELD).append(COLON).append(NEW_LINE);

        for (Agent agent : this.agents) {
            structure.append(TAB).append(TAB).append(agent.getName());
            if (!agent.getArchClass().equals(AgentArchClass.JASON.getName())) {
                structure.append(SPACE).append(AGENT_ARCH_CLASS_FIELD).append(SPACE).append(agent.getArchClass());
            }
            structure.append(SEMI_COLON).append(NEW_LINE);
        }

        return structure;
    }

    /**
     * Generates the complete MAS2J configuration file content.
     * 
     * @return A string representing the full MAS2J configuration.
     */
    private String getCompleteStructure() {
        StringBuffer structure = this.getCommonStructure();
        structure.append(TAB).append(AGENT_SOURCE_PATH_FIELD).append(COLON).append(NEW_LINE);
        structure.append(TAB).append(TAB).append(DEFAULT_AGENTS_SOURCE).append(SEMI_COLON).append(NEW_LINE);
        structure.append(CLOSE_BRACE).append(NEW_LINE);

        return structure.toString();
    }

    @Override
    public void saveAgent(Agent agent) {
        while (true) {
            Iterator<Agent> iterator = this.agents.iterator();
            if (!iterator.hasNext()) {
                this.agents.add(agent);
                break;
            }
            Agent next = iterator.next();
            if (next.getName().equals(agent.getName())) {
                this.agents.remove(next);
                this.agents.add(agent);
                break;
            }
        }
    }

    @Override
    public Agent getAgent(String name) {
        while (true) {
            Iterator<Agent> iterator = this.agents.iterator();
            if (!iterator.hasNext()) {
                break;
            }
            Agent next = iterator.next();
            if (next.getName().equals(name)) {
                return next;
            }
        }
        return null;
    }

    @Override
    public void removeAgent(Agent agent) {
        while (true) {
            Iterator<Agent> iterator = this.agents.iterator();
            if (!iterator.hasNext()) {
                break;
            }
            Agent next = iterator.next();
            if (next.getName().equals(agent.getName())) {
                this.agents.remove(next);
            }
        }
    }

    /**
     * Returns the MAS2J configuration file name.
     * 
     * @return The name of the MAS2J file.
     */
    @Override
    public String getName() {
        return this.masName + MAS_STRUCTURE_FILE_EXTENSION;
    }

    @Override
    public void setName(String name) {
    }

    /**
     * Returns the MAS2J file content.
     * 
     * @return A string representing the full MAS2J configuration.
     */
    @Override
    public String getContent() {
        return this.getCompleteStructure();
    }

    @Override
    public void setContent(String content) {
    }
}
