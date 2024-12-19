package group.chon.ide.news.app.service.chonos;

import group.chon.ide.news.app.service.chonos.firmware.ChonosFirmwareService;
import group.chon.ide.news.app.service.chonos.firmware.RemoteChonosFirmwareService;
import group.chon.ide.news.app.service.system.HardwareService;
import group.chon.ide.news.app.accessmethods.CommandPromptRunner;
import group.chon.ide.news.app.accessmethods.SshHandler;

public class ChonosModules {

    private ChonosConnectionService connection;

    private ChonosFirmwareService firmware;

    private ChonosNeighborsService neighbors;

    private ChonosEmbeddedMasService embeddedMas;

    private ChonosDDNSService ddns;

    private HardwareService bot;

    public ChonosModules(CommandPromptRunner commandPromptRunner) {
        this.loadContext(commandPromptRunner);
    }

    private void loadContext(CommandPromptRunner commandPromptRunner) {
        this.connection = new ChonosConnectionService(commandPromptRunner);
        this.firmware = commandPromptRunner instanceof SshHandler ? new RemoteChonosFirmwareService((SshHandler) commandPromptRunner) : new ChonosFirmwareService(commandPromptRunner);
        this.neighbors = new ChonosNeighborsService(commandPromptRunner);
        this.embeddedMas = new ChonosEmbeddedMasService(commandPromptRunner);
        this.ddns = new ChonosDDNSService(commandPromptRunner);
        this.bot = commandPromptRunner instanceof SshHandler ? new HardwareService() : null;
    }

    public void bindCommandPromptRunner(CommandPromptRunner commandPromptRunner) {
        this.loadContext(commandPromptRunner);
    }

    public ChonosConnectionService connection() {
        return this.connection;
    }

    public ChonosFirmwareService firmware() {
        return this.firmware;
    }

    public ChonosNeighborsService neighbors() {
        return this.neighbors;
    }

    public ChonosEmbeddedMasService embeddedMas() {
        return this.embeddedMas;
    }

    public ChonosDDNSService ddns() {
        return this.ddns;
    }

    public HardwareService bot() {
        return this.bot;
    }
}
