package org.masos.embed.sysconfig.model.executor;

public abstract class Executor {

    /**
     * Executa um comando no sistema.
     *
     * @param command Comando a ser executado.
     * @return Resposta do comando executado.
     */
    public abstract String execute(String command);

}
