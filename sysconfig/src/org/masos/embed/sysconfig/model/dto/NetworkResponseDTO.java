package org.masos.embed.sysconfig.model.dto;

import org.masos.embed.sysconfig.model.annotation.ResponseObject;

/**
 * Classe modelo que representa uma rede retornada como resposta para sysconfig.
 */
@ResponseObject
public class NetworkResponseDTO implements Comparable<NetworkResponseDTO> {

    /** Nome da rede. */
    private String essid;

    /** Frequência que ela opera. */
    private Double frequency;

    /** Qualidade da rede. */
    private Double quality;

    /** Flag que indica encriptação. */
    private Boolean encryption;

    /** Senha da rede, caso seja conhecida. */
    private String password;

    /**
     * Construtor.
     *
     * @param essid      {@link #essid}
     * @param frequency  {@link #frequency}
     * @param quality    {@link #quality}
     * @param encryption {@link #encryption}
     * @param password   {@link #password}
     */
    public NetworkResponseDTO(String essid, Double frequency, Double quality, Boolean encryption, String password) {
        this.essid = essid;
        this.frequency = frequency;
        this.quality = quality;
        this.encryption = encryption;
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
     * @return {@link #frequency}
     */
    public Double getFrequency() {
        return this.frequency;
    }

    /**
     * @param frequency {@link #frequency}
     */
    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    /**
     * @return {@link #quality}
     */
    public Double getQuality() {
        return this.quality;
    }

    /**
     * @param quality {@link #quality}
     */
    public void setQuality(Double quality) {
        this.quality = quality;
    }

    /**
     * @return {@link #encryption}
     */
    public Boolean getEncryption() {
        return this.encryption;
    }

    /**
     * @param encryption {@link #encryption}
     */
    public void setEncryption(Boolean encryption) {
        this.encryption = encryption;
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

    @Override
    public int compareTo(NetworkResponseDTO o) {
        return o.getQuality() > this.quality ? 1 : 0;
    }
}
