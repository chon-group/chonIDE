package org.masos.embed.SysConf.controller;

public class FirmwareManager {
	private String ArduinoListBoardsCMD 	= "chonosFirmwareManager --list";
	private String ArduinoListLibsCMD		= "chonosFirmwareManager --listLibraries";
	private String ArduinoImportLibCMD		= "chonosFirmwareManager -i ";
	private String ArduinoCompileSketchCMD	= "chonosFirmwareManager -s tempSketchSysConf";
	private String ArduinoDeploySketchCMD	= "chonosFirmwareManager -d tempSketchSysConf";
	
	public FirmwareManager() {
		
	}

	public String getArduinoListBoardsCMD() {
		return ArduinoListBoardsCMD;
	}

	public void setArduinoListBoardsCMD(String arduinoListBoardsCMD) {
		ArduinoListBoardsCMD = arduinoListBoardsCMD;
	}

	public String getArduinoListLibsCMD() {
		return ArduinoListLibsCMD;
	}

	public void setArduinoListLibsCMD(String arduinoListLibsCMD) {
		ArduinoListLibsCMD = arduinoListLibsCMD;
	}

	private String getArduinoImportLibCMD() {
		return ArduinoImportLibCMD;
	}

	public void setArduinoImportLibCMD(String arduinoImportLibCMD) {
		ArduinoImportLibCMD = arduinoImportLibCMD;
	}
	
	public String getArduinoImportLibCMD(String strFilePath) {
		return getArduinoImportLibCMD()+strFilePath;
	}

	private String getArduinoCompileSketchCMD() {
		return ArduinoCompileSketchCMD;
	}
	
	public String getArduinoCompileSketchCMD(String strFilePath, String strBoardFQDN) {
		return getArduinoCompileSketchCMD()+
				" -f "+strFilePath+
				" -b "+strBoardFQDN;
	}

	public void setArduinoCompileSketchCMD(String arduinoCompileSketchCMD) {
		ArduinoCompileSketchCMD = arduinoCompileSketchCMD;
	}

	private String getArduinoDeploySketchCMD() {
		return ArduinoDeploySketchCMD;
	}
	
	public String getArduinoDeploySketchCMD(String strBoardFQDN, String pathSerialPort) {
		return getArduinoDeploySketchCMD()+
				" -b "+strBoardFQDN+
				" -p "+pathSerialPort;
	}	

	public void setArduinoDeploySketchCMD(String arduinoDeploySketchCMD) {
		ArduinoDeploySketchCMD = arduinoDeploySketchCMD;
	}
	
	
	

}
