package group.chon.ide.news.domain.model.embeddedmas.environment.exogenous;

import group.chon.ide.news.domain.model.ProjectFile;
import lombok.RequiredArgsConstructor;

/**
 * Represents a library with a fixed name.
 * Unlike other {@link ProjectFile} implementations, a library does not contain source code.
 */
@RequiredArgsConstructor
public class Library implements ProjectFile {

    /**
     * The library's name.
     */
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("setName is not supported");
    }

    /**
     * Unsupported operation since a library does not have content.
     * 
     * @throws UnsupportedOperationException always
     */
    @Override
    public String getContent() {
        throw new UnsupportedOperationException("getContent is not supported for libraries");
    }

    /**
     * Unsupported operation since a library does not have content.
     * 
     * @throws UnsupportedOperationException always
     */
    @Override
    public void setContent(String content) {
        throw new UnsupportedOperationException("setContent is not supported for libraries");
    }
}
