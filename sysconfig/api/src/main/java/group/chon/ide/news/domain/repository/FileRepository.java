package group.chon.ide.news.domain.repository;

import java.io.InputStream;

/**
 * Repository interface for handling file operations.
 * Provides methods for reading, writing, renaming, deleting, and checking file existence.
 */
public interface FileRepository {

    /**
     * Reads the content of a file as a string.
     *
     * @param filePath The path of the file to be read.
     * @return The file content as a string.
     */
    String read(String filePath);

    /**
     * Reads the content of a file as an {@link InputStream}.
     *
     * @param filePath The path of the file to be read.
     * @return An {@link InputStream} containing the file content.
     */
    InputStream readInputStream(String filePath);

    /**
     * Saves content to a file at the specified path.
     * If the file already exists, it is overwritten.
     *
     * @param filePath The path where the file will be saved.
     * @param content The content to be written to the file.
     */
    void save(String filePath, String content);

    /**
     * Renames a file to a new name while keeping its original directory.
     *
     * @param oldPath The current file path.
     * @param newName The new name for the file.
     */
    void rename(String oldPath, String newName);

    /**
     * Deletes a file at the specified path.
     *
     * @param filePath The path of the file to be deleted.
     */
    void delete(String filePath);

    /**
     * Checks whether a file exists at the specified path.
     *
     * @param filePath The path of the file to check.
     * @return {@code true} if the file exists, otherwise {@code false}.
     */
    boolean exists(String filePath);
}
