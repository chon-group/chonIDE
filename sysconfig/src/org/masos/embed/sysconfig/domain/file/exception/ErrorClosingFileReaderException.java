package org.masos.embed.sysconfig.domain.file.exception;

public class ErrorClosingFileReaderException extends RuntimeException{

    public ErrorClosingFileReaderException(String fileName, Throwable throwable) {
        super("Erro fechando a leitura do arquivo: " + fileName, throwable);
    }

}
