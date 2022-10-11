package org.masos.embed.sysconfig.model.dto;

public class DomainStatusDTO {

    private String domain;

    private String ddns_user;

    private String ddns_token;

    private String localAddress;

    private String registredAddress;

    public DomainStatusDTO() {
    }

    public DomainStatusDTO(String domain, String ddns_user, String ddns_token, String localAddress,
                           String registredAddress) {
        this.domain = domain;
        this.ddns_user = ddns_user;
        this.ddns_token = ddns_token;
        this.localAddress = localAddress;
        this.registredAddress = registredAddress;
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
     * @return {@link #ddns_user}
     */
    public String getDdns_user() {
        return this.ddns_user;
    }

    /**
     * @param ddns_user {@link #ddns_user}
     */
    public void setDdns_user(String ddns_user) {
        this.ddns_user = ddns_user;
    }

    /**
     * @return {@link #ddns_token}
     */
    public String getDdns_token() {
        return this.ddns_token;
    }

    /**
     * @param ddns_token {@link #ddns_token}
     */
    public void setDdns_token(String ddns_token) {
        this.ddns_token = ddns_token;
    }

    /**
     * @return {@link #localAddress}
     */
    public String getLocalAddress() {
        return this.localAddress;
    }

    /**
     * @param localAddress {@link #localAddress}
     */
    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    /**
     * @return {@link #registredAddress}
     */
    public String getRegistredAddress() {
        return this.registredAddress;
    }

    /**
     * @param registredAddress {@link #registredAddress}
     */
    public void setRegistredAddress(String registredAddress) {
        this.registredAddress = registredAddress;
    }
}
