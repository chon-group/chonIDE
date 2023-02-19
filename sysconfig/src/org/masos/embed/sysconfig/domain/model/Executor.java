package org.masos.embed.sysconfig.domain.model;

public interface Executor {

    /**
     * Executa um comando no sistema.
     *
     * @param command Comando a ser executado.
     * @return Resposta do comando executado.
     */
    String execute(String command, boolean mantainLineBreak);

}
