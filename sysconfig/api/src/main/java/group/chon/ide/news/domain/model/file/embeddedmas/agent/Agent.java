package group.chon.ide.news.domain.model.file.embeddedmas.agent;

import group.chon.ide.news.domain.model.file.ProjectFile;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

@Data
@RequiredArgsConstructor
public class Agent implements ProjectFile {

    private final String name;

    private final String sourceCode;

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
