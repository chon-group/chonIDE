package group.chon.ide.news.domain.service.chonos;

import com.google.gson.reflect.TypeToken;
import group.chon.ide.news.GsonManager;
import group.chon.ide.news.domain.command.CommandExecutor;
import group.chon.ide.news.domain.model.chonos.Board;
import group.chon.ide.news.domain.model.chonos.Neighbor;

import java.util.List;

public class FirmwareService {

    private static final String FIRMWARE_MANAGER_COMMAND = "sudo chonosFirmwareManager ";

    public static final String ARDUINO_LIST_BOARDS = FIRMWARE_MANAGER_COMMAND + "--listBoards";

    public static final String ARDUINO_LIST_LIBRARIES = FIRMWARE_MANAGER_COMMAND + "--listLibraries";

    private static final String ARDUINO_IMPORT_LIBRARY = FIRMWARE_MANAGER_COMMAND + "--importLibrary -f %s";

    private static final String ARDUINO_COMPILE_SKETCH = FIRMWARE_MANAGER_COMMAND + "-s tempSketchSysConf -f %s -b %s";

    private static final String ARDUINO_DEPLOY_SKETCH = FIRMWARE_MANAGER_COMMAND + "-d tempSketchSysConf -b %s -p %s";

    private static final String ARDUINO_REMOVE_LIBRARY = FIRMWARE_MANAGER_COMMAND + "--removeLibrary %s";

    private CommandExecutor executor;

    public List<Board> findAllBoards() {
        return GsonManager.get().fromJson(this.executor.execute(ARDUINO_LIST_BOARDS), new TypeToken<List<Neighbor>>() {
        }.getType());
    }

    public List<String> findAllLibraries() {
        return GsonManager.get().fromJson(this.executor.execute(FIRMWARE_MANAGER_COMMAND), new TypeToken<List<String>>() {
        }.getType());
    }

}
