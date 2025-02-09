package group.chon.ide.news.domain.model.embeddedmas.environment;

import group.chon.ide.news.domain.model.embeddedmas.environment.endogenous.EndogenousEnv;
import group.chon.ide.news.domain.model.embeddedmas.environment.exogenous.ExogenousEnv;
import lombok.Data;

/**
 * Represents an environment composed of exogenous and endogenous components.
 */
@Data
public class Environment {

    /**
     * The exogenous environment, containing external dependencies like firmwares and libraries.
     */
    private final ExogenousEnv exogenous;

    /**
     * The endogenous environment, containing internal elements like artifacts and simulations.
     */
    private final EndogenousEnv endogenous;
}
