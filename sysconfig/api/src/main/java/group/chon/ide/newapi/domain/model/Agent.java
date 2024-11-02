package group.chon.ide.newapi.domain.model;

import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class Agent implements ProjectFile {

    private String name;

    private String sourceCode;

    private AgentArchClass archClass;

    @Override
    public String getFile() {
        return this.name;
    }

    @Override
    public byte[] getContent() {
        return this.sourceCode.getBytes(StandardCharsets.UTF_8);
    }
}
