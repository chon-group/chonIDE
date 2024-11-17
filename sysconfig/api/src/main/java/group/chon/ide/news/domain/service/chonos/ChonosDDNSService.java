package group.chon.ide.news.domain.service.chonos;

import group.chon.ide.news.domain.resourceaccess.CommandPromptRunner;

public class ChonosDDNSService {

    /** Comando para gerenciamento de DDNS. */
    private static final String DDNS_MANAGER_COMMAND = "chonosDDNSManager ";

    /** Script para configurar o domínio DDNS de acesso ao sistema. */
    private static final String DDNS_CONF = DDNS_MANAGER_COMMAND + "--conf -d %s -u %s -t %s";

    /** Script para status do domínio atual. */
    public static final String DDNS_STATUS = DDNS_MANAGER_COMMAND + "--status";

    private final CommandPromptRunner commandPromptRunner;

    public ChonosDDNSService(CommandPromptRunner commandPromptRunner) {
        this.commandPromptRunner = commandPromptRunner;
    }

    public String getStatus() {
        return null;
    }

    public void configure(String domain, String user, String token) {

    }
    
}
