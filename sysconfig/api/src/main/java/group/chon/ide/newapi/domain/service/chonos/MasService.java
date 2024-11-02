package group.chon.ide.newapi.domain.service.chonos;

import group.chon.ide.newapi.domain.command.CommandExecutor;

public class MasService {

    public static String EMBEDDED_MAS_START = "chonosEmbeddedMAS --start";

    public static String EMBEDDED_MAS_STOP = "chonosEmbeddedMAS --stop";

    private static String EMBEDDED_MAS_IMPORT = "chonosEmbeddedMAS --import -f %s";

    private CommandExecutor executor;

    public void start() {
        this.executor.execute(EMBEDDED_MAS_START);
    }

    public void stop() {
        this.executor.execute(EMBEDDED_MAS_START);
    }

    public void importMas(String masPath) {
        this.executor.execute(String.format(EMBEDDED_MAS_IMPORT, masPath));
    }


}
