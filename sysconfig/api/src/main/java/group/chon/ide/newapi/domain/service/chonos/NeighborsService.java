package group.chon.ide.newapi.domain.service.chonos;

import com.google.gson.reflect.TypeToken;
import group.chon.ide.newapi.GsonManager;
import group.chon.ide.newapi.domain.command.CommandExecutor;
import group.chon.ide.newapi.domain.model.chonos.Neighbor;

import java.util.List;

public class NeighborsService {

    public static final String LIST = "sudo chonos-neighbors --list";

    public static final String FORGET = "sudo chonos-neighbors --forget";

    private CommandExecutor executor;

    public List<Neighbor> findAll() {
        return GsonManager.get().fromJson(this.executor.execute(LIST), new TypeToken<List<Neighbor>>() {
        }.getType());
    }

    public void forget() {
        this.executor.execute(FORGET);
    }

}
