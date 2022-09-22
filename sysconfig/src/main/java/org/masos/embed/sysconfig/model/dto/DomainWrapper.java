package org.masos.embed.sysconfig.model.dto;

/**
 * Classe DTO de domínio.
 */
public class DomainWrapper {

    /** Domínio informado. */
    private String domain;

    /** Usuário DDNS informado. */
    private String ddnsUser;

    /** Token DDNS informado. */
    private String ddnsToken;

    /**
     * Construtor.
     */
    public DomainWrapper() {
    }

    /**
     * Construtor.
     *
     * @param domain    {@link #domain}
     * @param ddnsUser  {@link #ddnsUser}
     * @param ddnsToken {@link #ddnsToken}
     */
    public DomainWrapper(String domain, String ddnsUser, String ddnsToken) {
        this.domain = domain;
        this.ddnsUser = ddnsUser;
        this.ddnsToken = ddnsToken;
    }

    /**
     * @return {@link #domain}
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * @param domain {@link #domain}
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return {@link #ddnsUser}
     */
    public String getDdnsUser() {
        return this.ddnsUser;
    }

    /**
     * @param ddnsUser {@link #ddnsUser}
     */
    public void setDdnsUser(String ddnsUser) {
        this.ddnsUser = ddnsUser;
    }

    /**
     * @return {@link #ddnsToken}
     */
    public String getDdnsToken() {
        return this.ddnsToken;
    }

    /**
     * @param ddnsToken {@link #ddnsToken}
     */
    public void setDdnsToken(String ddnsToken) {
        this.ddnsToken = ddnsToken;
    }
}
