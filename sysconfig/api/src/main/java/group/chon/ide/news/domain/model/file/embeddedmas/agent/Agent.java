package group.chon.ide.news.domain.model.file.embeddedmas.agent;

import group.chon.ide.news.domain.model.file.ProjectFile;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class Agent implements ProjectFile {

    private String name;

    private String sourceCode;

    private AgentArchClass archClass;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public byte[] getContent() {
        return this.sourceCode.getBytes(StandardCharsets.UTF_8);
    }
}
