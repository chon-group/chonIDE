package group.chon.ide.api.domain.script;

/**
 * Classe de gerência dos scripts relacionados com o sistema.
 */
public class SystemScriptManager {

    /** Comando para retornar a configuração do sistema. */
    public static final String GET_CONFIGURATION = "cat /opt/group.chon/conf/release.conf";

    /** Desliga o sistema. */
    public static final String POWER_OFF = "poweroff";

    /** Reinicia o sistema. */
    public static final String REBOOT = "reboot";

}