package group.chon.ide.news.domain.model.embeddedmas;

import group.chon.ide.news.domain.model.embeddedmas.environment.Environment;
import group.chon.ide.news.domain.model.embeddedmas.masconfiguration.MasConfiguration;
import group.chon.ide.news.domain.model.embeddedmas.organization.Organization;
import group.chon.ide.news.domain.model.embeddedmas.agent.Agency;
import lombok.Data;

/**
 * Represents an Embedded Multi-Agent System (Embedded MAS).
 * This class encapsulates the main components of an MAS, including agents, environment, 
 * organization structure, and configuration settings.
 */
@Data
public class EmbeddedMAS {

    /**
     * The agency managing the agents in the MAS.
     */
    private final Agency agent;

    /**
     * The environment in which the MAS operates.
     */
    private final Environment environment;

    /**
     * The organizational structure of the MAS.
     */
    private final Organization organization;

    /**
     * The configuration settings of the MAS.
     */
    private final MasConfiguration masConfiguration;
}
