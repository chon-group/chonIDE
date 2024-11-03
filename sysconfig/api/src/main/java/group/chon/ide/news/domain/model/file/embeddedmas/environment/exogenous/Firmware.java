package group.chon.ide.news.domain.model.file.embeddedmas.environment.exogenous;


import group.chon.ide.news.domain.model.file.ProjectFile;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class Firmware implements ProjectFile {

    private final String name;

    private final String sourceCode;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public byte[] getContent() {
        return this.sourceCode.getBytes(StandardCharsets.UTF_8);
    }
}
