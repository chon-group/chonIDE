package group.chon.ide.news.domain.model.file.embeddedmas.agent;

import group.chon.ide.news.domain.model.file.ProjectFile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Agent implements ProjectFile {

    private String name;

    private String sourceCode;

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
