package group.chon.ide.news.domain.model.embeddedmas.environment.exogenous;

import group.chon.ide.news.domain.model.ProjectFile;
import lombok.AllArgsConstructor;

/**
 * Represents a firmware with a name and source code.
 */
@AllArgsConstructor
public class Firmware implements ProjectFile {

    /**
     * The firmware's name.
     */
    private String name;

    /**
     * The firmware's source code.
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
