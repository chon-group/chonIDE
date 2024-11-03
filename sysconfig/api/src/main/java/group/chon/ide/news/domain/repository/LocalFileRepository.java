package group.chon.ide.news.domain.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LocalFileRepository implements FileRepository {
    @Override
    public byte[] read(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String filePath, byte[] content) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            try {
                Path folder = path.getParent();
                if (!Files.exists(folder)) {
                    Files.createDirectories(folder);
                }
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Files.write(path, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rename(String oldPath, String newName) {
        Path path = Paths.get(oldPath);
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + oldPath);
        }
        try {
            Files.move(path, path.getParent().resolve(newName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
