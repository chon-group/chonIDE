package org.masos.embed.sysconfig.model;

public class User {

    private String username;

    private String password;

    public User() {
    }

    public User(String username, String password) {
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
}
