package group.chon.ide.news.domain.model.file;

import group.chon.ide.news.domain.model.file.embeddedmas.EmbeddedMAS;
import lombok.Data;

@Data
public class Project {

    private String name;

    private EmbeddedMAS embeddedMAS;

}
