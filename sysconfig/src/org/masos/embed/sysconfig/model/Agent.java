package org.masos.embed.sysconfig.model;

import java.io.Serializable;

public class Agent implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String archClass;
	
	private String sourceCode;
	
	public Agent() {
		// TODO Auto-generated constructor stub
	}
	
	public Agent(String name, String archClass) {
		this.name = name;
		this.archClass = archClass;
	}
	
	public Agent(String name, String archClass, String sourceCode) {
		this.name = name;
		this.archClass = archClass;
		this.sourceCode = sourceCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArchClass() {
		return archClass;
	}

	public void setArchClass(String archClass) {
		this.archClass = archClass;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
}
