package group.chon.ide.api.domain.file.exception;

public class ErrorDeletingFileException extends RuntimeException{

    public ErrorDeletingFileException(String fileName) {
        super("Erro ao deletar o arquivo: " + fileName);
    }

}
