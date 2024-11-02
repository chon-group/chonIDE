package group.chon.ide.news.domain.repository;

// Implementações de SSH Executor.
public class RemoteFileRepository implements FileRepository{
    @Override
    public byte[] read(String filePath) {
        return new byte[0];
    }

    @Override
    public void save(String filePath, byte[] content) {

    }

    @Override
    public void rename(String oldPath, String newName) {

    }

    @Override
    public void delete(String filePath) {

    }
}
