package group.chon.ide.news.domain.model;

import group.chon.ide.news.domain.model.embeddedmas.EmbeddedMAS;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Represents a project that includes an Embedded Multi-Agent System (Embedded MAS).
 * A project consists of a name and an associated MAS model.
 */
@Data
@RequiredArgsConstructor
public class Project {

    /**
     * The name of the project.
     */
    private final String name;

    /**
     * The Embedded Multi-Agent System (MAS) associated with this project.
     */
    private final EmbeddedMAS embeddedMAS;
}
