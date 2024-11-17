package group.chon.ide.news.domain.model.file.embeddedmas.environment.exogenous;


import group.chon.ide.news.domain.model.file.ProjectFile;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Firmware implements ProjectFile {

    private String name;

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
