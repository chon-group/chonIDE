package group.chon.ide.api.domain.file.exception;

public class ErrorCreatingFolderException extends RuntimeException{

    public ErrorCreatingFolderException(String folderName) {
        super("Erro na criação da pasta: " + folderName);
    }

}
