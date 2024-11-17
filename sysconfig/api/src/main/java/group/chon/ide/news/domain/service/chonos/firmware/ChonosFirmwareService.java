package group.chon.ide.news.domain.service.chonos.firmware;

import group.chon.ide.news.domain.resourceaccess.CommandPromptRunner;
import group.chon.ide.news.domain.model.chonos.Board;
import group.chon.ide.news.domain.model.chonos.Library;

import java.util.List;

public class ChonosFirmwareService {

    private static final String FIRMWARE_MANAGER_COMMAND = "sudo chonosFirmwareManager ";

    public static final String ARDUINO_LIST_BOARDS = FIRMWARE_MANAGER_COMMAND + "--listBoards";

    public static final String ARDUINO_LIST_LIBRARIES = FIRMWARE_MANAGER_COMMAND + "--listLibraries";

    private static final String ARDUINO_IMPORT_LIBRARY = FIRMWARE_MANAGER_COMMAND + "--importLibrary -f %s";

    private static final String ARDUINO_COMPILE_SKETCH = FIRMWARE_MANAGER_COMMAND + "-s tempSketchSysConf -f %s -b %s";

    private static final String ARDUINO_DEPLOY_SKETCH = FIRMWARE_MANAGER_COMMAND + "-d tempSketchSysConf -b %s -p %s";

    private static final String ARDUINO_REMOVE_LIBRARY = FIRMWARE_MANAGER_COMMAND + "--removeLibrary %s";

    private final CommandPromptRunner commandPromptRunner;

    public ChonosFirmwareService(CommandPromptRunner commandPromptRunner) {
        this.commandPromptRunner = commandPromptRunner;
    }

    public List<Board> getBoards() {
        return null;
    }

    public List<Library> getImportedLibraries() {
        return null;
    }

    public void removeLibrary(String name) {

    }

    public void importLibrary(String libraryFilePath) {

    }

    public void compileSketch(String name, Board board) {

    }

    public void deploy(Board board) {

    }

}
