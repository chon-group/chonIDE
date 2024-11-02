package group.chon.ide.newapi.domain.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class StandardFileRepository implements FileRepository {
    @Override
    public void save(String filePath, byte[] content) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            try {
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
    public void rename(String oldPath, String newPath) {
        Path path = Paths.get(oldPath);
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + oldPath);
        }
        try {
            Files.move(path, Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
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
