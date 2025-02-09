package group.chon.ide.news.domain.model.embeddedmas.agent;

import group.chon.ide.news.domain.model.ProjectFile;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an agent with a name, source code, and architecture class.
 */
@Data
@AllArgsConstructor
public class Agent implements ProjectFile {

    /**
     * The agent's name.
     */
    private String name;

    /**
     * The agent's source code.
     */
    private String sourceCode;

    /**
     * The agent's architecture class.
     */
    private AgentArchClass archClass;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getContent() {
        return this.sourceCode;
    }

    @Override
    public void setContent(String content) {
        this.sourceCode = content;
    }
}
