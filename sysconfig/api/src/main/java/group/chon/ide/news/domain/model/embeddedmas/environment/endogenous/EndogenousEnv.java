package group.chon.ide.news.domain.model.embeddedmas.environment.endogenous;

import lombok.Data;
import java.util.List;

/**
 * Represents an endogenous environment containing artifacts or simulated environments.
 */
@Data
public class EndogenousEnv {

    /**
     * List of artifacts in the environment.
     */
    private List<Artifact> artifacts;

    /**
     * List of simulated environments.
     */
    private List<SimulatedEnviroment> simulatedEnviroment;
}
