package group.chon.ide.news.domain.model.embeddedmas.environment.endogenous;

import group.chon.ide.news.domain.model.ProjectFile;
import lombok.AllArgsConstructor;

/**
 * Represents a simulated environment with a name and source code.
 */
@AllArgsConstructor
public class SimulatedEnviroment implements ProjectFile {

    /**
     * The simulated environment's name.
     */
    private String name;

    /**
     * The source code defining the simulated environment.
     */
    private String sourceCode;

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
