package group.chon.ide.news.domain.model.embeddedmas.environment;

import group.chon.ide.news.domain.model.embeddedmas.environment.endogenous.EndogenousEnv;
import group.chon.ide.news.domain.model.embeddedmas.environment.exogenous.ExogenousEnv;
import lombok.Data;

@Data
public class Environment {

    private final ExogenousEnv exogenous;

    private final EndogenousEnv endogenous;

}
