package org.masos.embed.sysconfig.model.dto;

import java.util.StringJoiner;

/**
 * Classe DTO de usuário
 */
public class UserWrapper {

    /** Nome do usuário. */
    private String username;

    /** Senha do usuário. */
    private String password;

    /**
     * Construtor.
     */
    public UserWrapper() {
    }

    /**
     * Construtor.
     *
     * @param username {@link #username}
     * @param password {@link #password}
     */
    public UserWrapper(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return {@link #username}
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username {@link #username}
     */
    public void setUsername(String username) {
        this.username = username;
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
    public String toString() {
        return new StringJoiner(", ", UserWrapper.class.getSimpleName() + "[", "]").add(
                "username='" + this.username + "'").add("password='" + this.password + "'").toString();
    }
}
