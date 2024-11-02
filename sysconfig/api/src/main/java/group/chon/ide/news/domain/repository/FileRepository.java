package group.chon.ide.news.domain.repository;

public interface FileRepository {

    byte[] read(String filePath);

    void save(String filePath, byte[] content);

    void rename(String oldPath, String newName);

    void delete(String filePath);

}
