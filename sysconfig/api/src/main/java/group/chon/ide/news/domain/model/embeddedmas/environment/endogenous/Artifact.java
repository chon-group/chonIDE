package group.chon.ide.news.domain.model.embeddedmas.environment.endogenous;

import group.chon.ide.news.domain.model.ProjectFile;
import lombok.AllArgsConstructor;

/**
 * Represents an artifact with a name and source code.
 */
@AllArgsConstructor
public class Artifact implements ProjectFile {

    /**
     * The artifact's name.
     */
    private String name;

    /**
     * The artifact's source code.
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
