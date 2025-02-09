package group.chon.ide.news.domain.model.embeddedmas.masconfiguration;

import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;

/**
 * Represents a JCM configuration file for Multi-Agent System (MAS) projects using the JaCaMo framework.
 * This file defines the structure of a Multi-Agent System (MAS), including its agents and infrastructure.
 * Currently, all methods are unimplemented and will throw {@link UnsupportedOperationException}.
 */
public class Jcm implements MasConfiguration {

    /**
     * Throws an {@link UnsupportedOperationException} as this method is not implemented.
     * 
     * @return Nothing, as it always throws an exception.
     * @throws UnsupportedOperationException always
     */
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this method is not implemented.
     * 
     * @return Nothing, as it always throws an exception.
     * @throws UnsupportedOperationException always
     */
    @Override
    public String getContent() {
        throw new UnsupportedOperationException("Unimplemented method 'getContent'");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this method is not implemented.
     * 
     * @param content The content to be set (not supported).
     * @throws UnsupportedOperationException always
     */
    @Override
    public void setContent(String content) {
        throw new UnsupportedOperationException("Unimplemented method 'setContent'");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this method is not implemented.
     * 
     * @param name The name to be set (not supported).
     * @throws UnsupportedOperationException always
     */
    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'setName'");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this method is not implemented.
     * 
     * @param agent The agent to be saved (not supported).
     * @throws UnsupportedOperationException always
     */
    @Override
    public void saveAgent(Agent agent) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAgent'");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this method is not implemented.
     * 
     * @param name The name of the agent to retrieve.
     * @return Nothing, as it always throws an exception.
     * @throws UnsupportedOperationException always
     */
    @Override
    public Agent getAgent(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'getAgent'");
    }

    /**
     * Throws an {@link UnsupportedOperationException} as this method is not implemented.
     * 
     * @param agent The agent to be removed (not supported).
     * @throws UnsupportedOperationException always
     */
    @Override
    public void removeAgent(Agent agent) {
        throw new UnsupportedOperationException("Unimplemented method 'removeAgent'");
    }
}
