package group.chon.ide.api.api.authentication;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton com os usuários autenticados do sistema.
 */
public class SecurityContextHolder {

    /** Instância de SecurityContextHolder. */
    private static SecurityContextHolder _instance;

    /** Mapa de usuários autenticados por JWT. */
    @Getter
    private Map<String, AuthenticatedUser> authenticatedUsersByToken;

    /**
     * Construtor.
     */
    private SecurityContextHolder() {
        this.authenticatedUsersByToken = new HashMap<>();
    }

    public static SecurityContextHolder get() {
        if (_instance == null) {
            _instance = new SecurityContextHolder();
        }
        return _instance;
    }

}
