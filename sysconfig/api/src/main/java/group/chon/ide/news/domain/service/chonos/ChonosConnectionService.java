package group.chon.ide.news.domain.service.chonos;

import group.chon.ide.news.domain.resourceaccess.CommandPromptRunner;
import group.chon.ide.news.domain.model.chonos.Network;

import java.util.List;

public class ChonosConnectionService {

    private final CommandPromptRunner commandPromptRunner;

    public ChonosConnectionService(CommandPromptRunner commandPromptRunner) {
        this.commandPromptRunner = commandPromptRunner;
    }

    public void apMode(String essid, String password) {

    }

    public void clientMode(String essid, String password) {

    }

    public List<Network> getNetworks() {
        return null;
    }

    public void forgetAllNetworks() {

    }

    public String getStatus() {
        return null;
    }


}
