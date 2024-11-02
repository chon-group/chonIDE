package group.chon.ide.newapi.domain.repository;

public interface FileRepository {

    void save(String filePath, byte[] content);

    void rename(String oldPath, String newPath);

    void delete(String filePath);

}
