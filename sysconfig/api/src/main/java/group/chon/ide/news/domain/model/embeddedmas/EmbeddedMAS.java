package group.chon.ide.news.domain.model.embeddedmas;

import group.chon.ide.news.domain.model.embeddedmas.environment.Environment;
import group.chon.ide.news.domain.model.embeddedmas.masconfiguration.MasConfiguration;
import group.chon.ide.news.domain.model.embeddedmas.organization.Organization;
import group.chon.ide.news.domain.model.embeddedmas.agent.AgentLayer;
import lombok.Data;

@Data
public class EmbeddedMAS {

    private final AgentLayer agent;

    private final Environment environment;

    private final Organization organization;

    private final MasConfiguration masConfiguration;

}
