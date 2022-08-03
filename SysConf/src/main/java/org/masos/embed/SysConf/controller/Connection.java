package org.masos.embed.SysConf.controller;

//import org.masos.embed.SysConf.controller.SSHaux;


public class Connection {
	private String WifiScanListCMD 		= "chonosWifiConn --list";
	private String WifiStatusCMD 		= "chonosWifiConn --status";
	private String WifiAPModeCMD 		= "chonosWifiConf -m ap";
	private String WifiForgetCMD	 	= "chonosWifiConf --forget";
	private String WifiWellKnownListCMD = "chonosWifiConf --list";
	private String WifiClientModeCMD	= "chonosWifiConf -m client";
	private String WifiTryReconnectCMD	= "chonosWifiConf -m default";
	private String DDNSConfCMD			= "chonosDDNSConf ";
	
	
	
	private String 	usernameSSH;
	private String 	passwordSSH;
	private String 	hostnameSSH;
	private Integer	portSSH;
	
	SSHaux ssh;
	
	public Connection() {
		
	}
	
	public Connection(String usernameSSH, String passwordSSH) {
		this.usernameSSH 	= usernameSSH;
		this.passwordSSH 	= passwordSSH;
		this.hostnameSSH 	= "localhost";
		this.portSSH		= 22;
		
		this.ssh = new SSHaux(this.hostnameSSH, this.portSSH);
	}
	
	public Connection(String username, String password, String hostname, Integer port) {
		this.usernameSSH	= username;
		this.passwordSSH	= password;
		this.hostnameSSH	= hostname;
		this.portSSH		= port;
		
		this.ssh = new SSHaux(this.hostnameSSH, this.portSSH);
	}


	public String getWifiScanListCMD() {
		return WifiScanListCMD;
	}

	public void setWifiScanListCMD(String wifiListCMD) {
		WifiScanListCMD = wifiListCMD;
	}

	public String getUsernameSSH() {
		return usernameSSH;
	}

	public void setUsernameSSH(String usernameSSH) {
		this.usernameSSH = usernameSSH;
	}

	public String getPasswordSSH() {
		return passwordSSH;
	}

	public void setPasswordSSH(String passwordSSH) {
		this.passwordSSH = passwordSSH;
	}

	public String getHostnameSSH() {
		return hostnameSSH;
	}

	public void setHostnameSSH(String hostnameSSH) {
		this.hostnameSSH = hostnameSSH;
	}

	public Integer getPortSSH() {
		return portSSH;
	}

	public void setPortSSH(Integer portSSH) {
		this.portSSH = portSSH;
	}

	public String getWifiStatusCMD() {
		return WifiStatusCMD;
	}

	public void setWifiStatusCMD(String wifiStatusCMD) {
		WifiStatusCMD = wifiStatusCMD;
	}

	public String getWifiAPModeCMD() {
		return WifiAPModeCMD;
	}

	public void setWifiAPModeCMD(String wifiAPMode) {
		WifiAPModeCMD = wifiAPMode;
	}

	public String getWifiAPMode(Integer intChannel, String strESSID, String strKey) {
		return getWifiAPModeCMD()+
					" -c "+Integer.toString(intChannel)+
					" -e "+strESSID+
					" -k "+strKey;
	}
	
	public String getWifiAPMode(String strESSID, String strKey) {
		return getWifiAPModeCMD()+
					" -e "+strESSID+
					" -k "+strKey;
	}
	
	public String getWifiAPMode(String strESSID) {
		return getWifiAPModeCMD()+
					" -e "+strESSID;
	}

	public String getWifiForgetCMD() {
		return WifiForgetCMD;
	}

	public void setWifiForgetCMD(String wifiForgetCMD) {
		WifiForgetCMD = wifiForgetCMD;
	}

	public String getWifiWellKnownListCMD() {
		return WifiWellKnownListCMD;
	}

	public void setWifiWellKnownListCMD(String wifiWellKnownListCMD) {
		WifiWellKnownListCMD = wifiWellKnownListCMD;
	}

	public String getWifiClientModeCMD() {
		return WifiClientModeCMD;
	}

	public void setWifiClientModeCMD(String wifiClientModeCMD) {
		WifiClientModeCMD = wifiClientModeCMD;
	}
	
	public String getWifiClientMode(String strESSID, String strKey) {
		return getWifiClientModeCMD()+
					" -e "+strESSID+
					" -k "+strKey;
	}

	public String getWifiTryReconnectCMD() {
		return WifiTryReconnectCMD;
	}

	public void setWifiTryReconnectCMD(String wifiTryReconnectCMD) {
		WifiTryReconnectCMD = wifiTryReconnectCMD;
	}

	private String getDDNSConfCMD() {
		return DDNSConfCMD;
	}
	
	public String getDDNSConfCMD(String strDomain, String strUser, String strToken) {
		return getDDNSConfCMD()+strDomain+" "+strUser+" "+strToken;
	}

	public void setDDNSConfCMD(String dDNSConfCMD) {
		DDNSConfCMD = dDNSConfCMD;
	}

}
