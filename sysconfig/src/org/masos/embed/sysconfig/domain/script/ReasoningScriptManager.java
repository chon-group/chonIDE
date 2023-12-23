package org.masos.embed.sysconfig.domain.script;

/**
 * Classe de gerência dos scripts do sistema embarcado.
 */
public class ReasoningScriptManager {

    /** Script para iniciar o SMA. */
    public static String EMBEDDED_MAS_START = "sudo chonosEmbeddedMAS --start";

    /** Script para parar o SMA. */
    public static String EMBEDDED_MAS_STOP = "sudo chonosEmbeddedMAS --stop";

    /** Script para importar o SMA. */
    private static String EMBEDDED_MAS_IMPORT = "sudo chonosEmbeddedMAS --import -f %s";

    /**
     * Retorna o script formatado para importação do SMA.
     *
     * @param filePath Caminho do arquivo do SMA para importação.
     * @return Script formatado.
     */
    public static String mountEmbeddedMASImportScript(String filePath) {
        return String.format(EMBEDDED_MAS_IMPORT, filePath);
    }

}
