package group.chon.ide.news.domain.resourceaccess.file;

import java.io.InputStream;

public interface FileRepository {

    String read(String filePath);

    InputStream readInputStream(String filePath);

    void save(String filePath, String content);

    void rename(String oldPath, String newName);

    void delete(String filePath);

    boolean exists(String filePath);

}
