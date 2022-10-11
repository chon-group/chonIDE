package org.masos.embed.sysconfig.model;

public class ArduinoBoard {

    private String board;

    private String fqbn;

    private String core;

    private String port;

    public ArduinoBoard(String board, String fqbn, String core, String port) {
        this.board = board;
        this.fqbn = fqbn;
        this.core = core;
        this.port = port;
    }

    /**
     * @return {@link #board}
     */
    public String getBoard() {
        return this.board;
    }

    /**
     * @param board {@link #board}
     */
    public void setBoard(String board) {
        this.board = board;
    }

    /**
     * @return {@link #fqbn}
     */
    public String getFqbn() {
        return this.fqbn;
    }

    /**
     * @param fqbn {@link #fqbn}
     */
    public void setFqbn(String fqbn) {
        this.fqbn = fqbn;
    }

    /**
     * @return {@link #core}
     */
    public String getCore() {
        return this.core;
    }

    /**
     * @param core {@link #core}
     */
    public void setCore(String core) {
        this.core = core;
    }

    /**
     * @return {@link #port}
     */
    public String getPort() {
        return this.port;
    }

    /**
     * @param port {@link #port}
     */
    public void setPort(String port) {
        this.port = port;
    }
}
