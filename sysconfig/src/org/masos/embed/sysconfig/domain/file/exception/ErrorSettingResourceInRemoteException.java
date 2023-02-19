package org.masos.embed.sysconfig.domain.file.exception;

public class ErrorSettingResourceInRemoteException extends RuntimeException {

    public ErrorSettingResourceInRemoteException(String fileName, Throwable throwable) {
        super("Erro setando o recurso remotamente para o arquivo: " + fileName, throwable);
    }
}
