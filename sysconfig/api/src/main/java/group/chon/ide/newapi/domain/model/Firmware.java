package group.chon.ide.newapi.domain.model;


import java.nio.charset.StandardCharsets;

public class Firmware implements ProjectFile {

    private String name;

    private String sourceCode;

    @Override
    public String getFile() {
        return this.name;
    }

    @Override
    public byte[] getContent() {
        return this.sourceCode.getBytes(StandardCharsets.UTF_8);
    }
}
