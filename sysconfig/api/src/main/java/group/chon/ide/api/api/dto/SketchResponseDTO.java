package group.chon.ide.api.api.dto;

/**
 * Class that represents a Sketch from the chonIDE.
 */
public class SketchResponseDTO {
 
    /** The board name. */
    private String boardName;

    /** The source code of the firmware. */
    private String code;

    /**
     * Constructor.
     *
     * @param boardName      {@link #boardName}
     * @param code   {@link #code}
     */
    public SketchResponseDTO(String boardName, String code) {
        this.boardName = boardName;
        this.code = code;
    }

    /**
     * @return {@link #boardName}
     */
    public String getBoardName() {
        return this.boardName;
    }

    /**
     * @param boardName {@link #boardName}
     */
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
    
    /**
     * @return {@link #code}
     */
    public String getcode() {
        return this.code;
    }

    /**
     * @param code {@link #code}
     */
    public void setcode(String code) {
        this.code = code;
    }

}
