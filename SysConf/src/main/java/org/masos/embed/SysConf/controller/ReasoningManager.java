package org.masos.embed.SysConf.controller;

public class ReasoningManager {
	private String EmbeddedMASImportCMD 	= "chonosEmbeddedMAS --import -f ";
	private String EmbeddedMASStartCMD		= "chonosEmbeddedMAS --start";	
	private String EmbeddedMASStopCMD		= "chonosEmbeddedMAS --stop";
	
	
			
	public ReasoningManager() {
		
	}

	private String getEmbeddedMASImportCMD() {
		return EmbeddedMASImportCMD;
	}
	
	public String getEmbeddedMASImportCMD(String strPathFile) {
		return getEmbeddedMASImportCMD()+strPathFile;
	}

	public void setEmbeddedMASImportCMD(String embeddedMASImportCMD) {
		EmbeddedMASImportCMD = embeddedMASImportCMD;
	}

	public String getEmbeddedMASStartCMD() {
		return EmbeddedMASStartCMD;
	}
	
	public String getEmbeddedMASStartCMD(String strPathMas2jFile) {
		return getEmbeddedMASStartCMD()+" -f "+strPathMas2jFile;
	}

	public void setEmbeddedMASStartCMD(String embeddedMASStartCMD) {
		EmbeddedMASStartCMD = embeddedMASStartCMD;
	}

	public String getEmbeddedMASStopCMD() {
		return EmbeddedMASStopCMD;
	}

	public void setEmbeddedMASStopCMD(String embeddedMASStopCMD) {
		EmbeddedMASStopCMD = embeddedMASStopCMD;
	}

}
