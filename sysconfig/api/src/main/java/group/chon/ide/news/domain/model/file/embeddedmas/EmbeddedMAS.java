package group.chon.ide.news.domain.model.file.embeddedmas;

import group.chon.ide.news.domain.model.file.embeddedmas.agent.AgentLayer;
import group.chon.ide.news.domain.model.file.embeddedmas.environment.Environment;
import group.chon.ide.news.domain.model.file.embeddedmas.masconfiguration.MasConfiguration;
import group.chon.ide.news.domain.model.file.embeddedmas.organization.Organization;
import lombok.Data;

@Data
public class EmbeddedMAS {

    private AgentLayer agent;

    private Environment environment;

    private Organization organization;

    private MasConfiguration masConfiguration;

}
