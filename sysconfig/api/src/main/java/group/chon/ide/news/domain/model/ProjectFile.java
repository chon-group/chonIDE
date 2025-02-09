package group.chon.ide.news.domain.model;

/**
 * Represents a file within a project.
 * Defines methods for retrieving and modifying the file's name and content.
 */
public interface ProjectFile {

    /**
     * Gets the name of the file.
     * 
     * @return The file name.
     */
    String getName();

    /**
     * Gets the content of the file.
     * 
     * @return The file content.
     */
    String getContent();

    /**
     * Sets the content of the file.
     * 
     * @param content The new content of the file.
     */
    void setContent(String content);

    /**
     * Sets the name of the file.
     * 
     * @param name The new name of the file.
     */
    void setName(String name);
}
