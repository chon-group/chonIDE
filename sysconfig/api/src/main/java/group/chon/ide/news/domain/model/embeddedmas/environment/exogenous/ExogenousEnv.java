package group.chon.ide.news.domain.model.embeddedmas.environment.exogenous;

import lombok.Data;
import java.util.List;

/**
 * Represents an exogenous environment containing firmwares and libraries.
 */
@Data
public class ExogenousEnv {

    /**
     * List of firmwares in the environment.
     */
    private List<Firmware> firmwares;

    /**
     * List of libraries in the environment.
     */
    private List<Library> libraries;
}
