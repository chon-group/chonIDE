package org.masos.embed.sysconfig.model.executor;

public interface Executor {

    /**
     * Executa um comando no sistema.
     *
     * @param command Comando a ser executado.
     * @return Resposta do comando executado.
     */
    String execute(String command);

}
