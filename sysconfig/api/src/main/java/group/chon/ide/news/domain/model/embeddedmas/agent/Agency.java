package group.chon.ide.news.domain.model.embeddedmas.agent;

import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * Manages a collection of agents.
 */
@RequiredArgsConstructor
public class Agency {

    /**
     * List of agents in this agency.
     */
    private final List<Agent> agents;

}
