package group.chon.ide.news.domain.model;

import group.chon.ide.news.domain.model.embeddedmas.EmbeddedMAS;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Project {

    private final String name;

    private final EmbeddedMAS embeddedMAS;

}
