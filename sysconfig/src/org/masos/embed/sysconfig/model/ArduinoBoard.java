package org.masos.embed.sysconfig.model;

import java.io.Serializable;

public class ArduinoBoard implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String port;
	
	private String board;
	
	private String sourceCode;

	private String boardConfig;

	public ArduinoBoard() {
		super();
	}
	
	public ArduinoBoard(String name, String port, String board) {
		super();
		this.name = name;
		this.port = port;
		this.board = board;
	}

	public ArduinoBoard(String name, String port, String board, String sourceCode, String boardConfig) {
		super();
		this.name = name;
		this.port = port;
		this.board = board;
		this.sourceCode = sourceCode;
		this.boardConfig = boardConfig;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getBoardConfig() {
		return boardConfig;
	}

	public void setBoardConfig(String boardConfig) {
		this.boardConfig = boardConfig;
	}

}
