package group.chon.ide.news.domain.model.embeddedmas.masconfiguration;

import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;

/**
 * Defines the configuration of a Multi-Agent System (MAS), 
 * including methods to add, retrieve, and remove agents from the configuration file.
 */
public interface MasConfiguration extends ProjectFile {

    /**
     * Saves an agent to the configuration file.
     * 
     * @param agent The agent to be saved.
     */
    void saveAgent(Agent agent);

    /**
     * Retrieves an agent by its name from the configuration file.
     * 
     * @param name The name of the agent.
     * @return The agent with the specified name, or {@code null} if not found.
     */
    Agent getAgent(String name);

    /**
     * Removes an agent from the configuration file.
     * 
     * @param agent The agent to be removed.
     */
    void removeAgent(Agent agent);
}
