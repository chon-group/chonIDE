package group.chon.ide.api.domain.script;

/**
 * Classe de gerência dos scripts relacionados com os firmwares.
 */
public class FirmwareScriptManager {

    /** Comando para interação com o(s) firmware(s) do sistema. */
    private static final String FIRMWARE_MANAGER_COMMAND = "chonosFirmwareManager ";

    /** Script para listar as placas do sistema. */
    public static final String ARDUINO_LIST_BOARDS = FIRMWARE_MANAGER_COMMAND + "--list";

    /** Script para listar as bibliotecas das placas do sistema. */
    public static final String ARDUINO_LIST_LIBRARIES = FIRMWARE_MANAGER_COMMAND + "--listLibraries";

    /** Script para importação biblioteca para as placas do sistema. */
    private static final String ARDUINO_IMPORT_LIBRARY = FIRMWARE_MANAGER_COMMAND + "-i '%s'";

    /** Script para compilar um sketch para uma placa. */
    private static final String ARDUINO_COMPILE_SKETCH = FIRMWARE_MANAGER_COMMAND + "-s tempSketchSysConf -f %s -b %s";

    /** Script para dar deploy em uma placa. */
    private static final String ARDUINO_DEPLOY_SKETCH = FIRMWARE_MANAGER_COMMAND + "-d tempSketchSysConf -b %s -p %s";

    /**
     * Retorna o script formatado para importação de biblioteca.
     *
     * @param filePath Caminho da biblioteca.
     * @return Script formatado.
     */
    public static String mountArduinoImportLibScript(String filePath) {
        return String.format(ARDUINO_IMPORT_LIBRARY, filePath);
    }

    /**
     * Retorna o script formatado para compilação de um sketch.
     *
     * @param filePath  Caminho do sketch.
     * @param boardName Nome da placa para compilação.
     * @return Script formatado.
     */
    public static String mountArduinoCompileSketchScript(String filePath, String boardName) {
        return String.format(ARDUINO_COMPILE_SKETCH, filePath, boardName);
    }

    /**
     * Retorna o script para deploy de uma placa.
     *
     * @param boardName      Nome bem qualificado da placa.
     * @param serialPortPath Caminho da porta serial da placa.
     * @return Script formatado.
     */
    public static String mountArduinoDeploySketchScript(String boardName, String serialPortPath) {
        return String.format(ARDUINO_DEPLOY_SKETCH, boardName, serialPortPath);
    }
}
