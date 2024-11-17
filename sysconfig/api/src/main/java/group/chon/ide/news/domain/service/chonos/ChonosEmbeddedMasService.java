package group.chon.ide.news.domain.service.chonos;

import group.chon.ide.news.domain.resourceaccess.CommandPromptRunner;

public class ChonosEmbeddedMasService {

    public static String EMBEDDED_MAS_START = "chonosEmbeddedMAS --start";

    public static String EMBEDDED_MAS_STOP = "chonosEmbeddedMAS --stop";

    private static String EMBEDDED_MAS_IMPORT = "chonosEmbeddedMAS --import -f %s";

    private final CommandPromptRunner commandPromptRunner;

    public ChonosEmbeddedMasService(CommandPromptRunner commandPromptRunner) {
        this.commandPromptRunner = commandPromptRunner;
    }

    public void start() {
    }

    public void stop() {
    }

    public void importMas(String masPath) {
    }


}
