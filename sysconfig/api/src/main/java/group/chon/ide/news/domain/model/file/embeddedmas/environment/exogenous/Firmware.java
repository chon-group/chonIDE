package group.chon.ide.news.domain.model.file.embeddedmas.environment.exogenous;


import group.chon.ide.news.domain.model.file.ProjectFile;

import java.nio.charset.StandardCharsets;

public class Firmware implements ProjectFile {

    private String name;

    private String sourceCode;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public byte[] getContent() {
        return this.sourceCode.getBytes(StandardCharsets.UTF_8);
    }
}
