package group.chon.ide.news.app.service.chonos;

import group.chon.ide.news.app.accessmethods.CommandPromptRunner;
import group.chon.ide.news.app.model.Network;

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

    public Network getStatus() {
        return null;
    }


}
