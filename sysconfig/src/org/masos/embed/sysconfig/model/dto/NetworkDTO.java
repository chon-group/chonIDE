package org.masos.embed.sysconfig.model.dto;

/**
 * Classe DTO de rede.
 */
public class NetworkDTO {

    /** Nome da rede. */
    private String essid;

    /** Senha da rede. */
    private String password;

    /**
     * Construtor.
     */
    public NetworkDTO() {
    }

    /**
     * Construtor.
     *
     * @param essid    {@link #essid}
     * @param password {@link #password}
     */
    public NetworkDTO(String essid, String password) {
        this.essid = essid;
        this.password = password;
    }

    /**
     * @return {@link #essid}
     */
    public String getEssid() {
        return this.essid;
    }

    /**
     * @param essid {@link #essid}
     */
    public void setEssid(String essid) {
        this.essid = essid;
    }

    /**
     * @return {@link #password}
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password {@link #password}
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
