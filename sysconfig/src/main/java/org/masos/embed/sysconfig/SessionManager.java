package org.masos.embed.sysconfig;

import org.masos.embed.sysconfig.model.User;

/**
 * Classe que representa a "sessão" do usuário no sysconfig.
 */
public class SessionManager {

    /** Usuário em memória. */
    private static User user;

    /**
     * @return {@link #user}
     */
    public static User getUser() {
        return user;
    }

    /**
     * @param user {@link #user}
     */
    public static void setUser(User user) {
        SessionManager.user = user;
    }
}
