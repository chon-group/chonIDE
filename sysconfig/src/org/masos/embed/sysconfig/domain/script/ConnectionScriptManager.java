package org.masos.embed.sysconfig.domain.script;

/**
 * Classe de gerência dos scripts relacionados a conexão do sistema.
 */
public class ConnectionScriptManager {

    /** Comando para gerenciamento de DDNS. */
    private static final String DDNS_MANAGER_COMMAND = "chonosDDNSManager ";

    /** Script para configurar o domínio DDNS de acesso ao sistema. */
    private static final String DDNS_CONF = DDNS_MANAGER_COMMAND + "-o conf -d %s -u %s -t %s";

    /** Script para status do domínio atual. */
    public static final String DDNS_STATUS = DDNS_MANAGER_COMMAND + "--status";

    /** Comando para configuração de conexão wi-fi. */
    private static final String WIFI_CONF_COMMAND = "chonosWifiConf ";

    /** Script para esquecer as redes lembradas pelo sistema. */
    public static final String WIFI_FORGET = WIFI_CONF_COMMAND + "--forget";

    /** Script para listar as redes conhecidas do sistema. */
    public static final String WIFI_WELL_KNOWN_LIST = WIFI_CONF_COMMAND + "--list";

    /** Script para tentar reconectar com a rede default do sistema. */
    public static final String WIFI_TRY_RECONNECT = WIFI_CONF_COMMAND + "-m default";

    /** Script para realizar conexão encriptada em modo AP do sistema. */
    private static final String WIFI_AP_MODE_WITH_ENCRIPTION = WIFI_CONF_COMMAND + "-m ap -e %s -k %s";

    /** Script para realizar conexão sem encriptação em moto AP do sistema. */
    private static final String WIFI_AP_MODE_WITHOUT_ENCRIPTION = WIFI_CONF_COMMAND + "-m ap -e %s -k NONE";

    /** Script para realizar conexão em modo cliente com encriptação do sistema. */
    private static final String WIFI_CLIENT_MODE_WITH_ENCRIPTION = WIFI_CONF_COMMAND + "-m client -e %s -k %s --reboot";

    /** Script para realizar conexão em modo cliente sem encriptação do sistema. */
    private static final String WIFI_CLIENT_MODE_WITHOUT_ENCRIPTION = WIFI_CONF_COMMAND + "-m client -e %s --reboot";

    /** Comando para interação de conexão wi-fi. */
    private static final String WIFI_CONN_COMMAND = "chonosWifiConn ";

    /** Script para listar todas as redes wi-fi disponíveis. */
    public static final String WIFI_SCAN_LIST = WIFI_CONN_COMMAND + "--list";

    /** Script para mostrar o status de conexão do sistema. */
    public static final String WIFI_STATUS = WIFI_CONN_COMMAND + "--status";

    /** Usuário DDNS. */
    private static final String DDNS_DEFAULT_USER = "ddns_user";

    /** Token DDNS. */
    private static final String DDNS_DEFAULT_TOKEN = "ddns_token";

    /**
     * Retorna o script formatado para conexão em modo AP com encriptação.
     *
     * @param essid Essid da rede.
     * @param key   Senha da rede.
     * @return Script formatado.
     */
    public static String mountWifiAPModeScript(String essid, String key) {
        return String.format(WIFI_AP_MODE_WITH_ENCRIPTION, essid, key);
    }

    /**
     * Retorna o script formatado para conexão em modo AP sem encriptação.
     *
     * @param essid Essid da rede.
     * @return Script formatado.
     */
    public static String mountWifiAPModaScript(String essid) {
        return String.format(WIFI_AP_MODE_WITHOUT_ENCRIPTION, essid);
    }

    /**
     * Retorna o script formatado para conexão em modo cliente com encriptação.
     *
     * @param essid Essid da rede.
     * @param key   Senha de rede.
     * @return
     */
    public static String mountWifiClientModeScript(String essid, String key) {
        return String.format(WIFI_CLIENT_MODE_WITH_ENCRIPTION, essid, key);
    }

    /**
     * Retorna o script formatado para conexão em modo cliente sem encriptação.
     *
     * @param essid Essid da rede.
     * @return Script formatado.
     */
    public static String mountWifiClientModeScript(String essid) {
        return String.format(WIFI_CLIENT_MODE_WITHOUT_ENCRIPTION, essid);
    }

    /**
     * Retorna o script formatado para configuração do domínio DDNS.
     *
     * @param domain Domínio.
     * @param user   Usuário DDNS.
     * @param token  Senha DDNS.
     * @return Script formatado.
     */
    public static String mountDDNSConfScript(String domain, String user, String token) {
        return String.format(DDNS_CONF, domain, user, token);
    }

    /**
     * Retorna o script formatado para configuração do domínio DDNS default.
     *
     * @param domain Domínio.
     * @return Script formatado.
     */
    public static String mountDDNSConfScript(String domain) {
        return mountDDNSConfScript(domain, DDNS_DEFAULT_USER, DDNS_DEFAULT_TOKEN);
    }

}
