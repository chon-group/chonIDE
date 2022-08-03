package org.masos.embed.SysConf.controller;

public class ShellCommands {
	
	private final String javinoPath		= "/opt/javino/";
	private final String confAPModeWLAN = "EmbedMAS-NewTask echo 1 > /opt/EmbedMAS/conf/apMode.conf";
	//private final String confClientWLAN = "cp /tmp/wpa /etc/wpa_supplicant/wpa_supplicant.conf; touch /tmp/javinoAPMode";
	private final String createWPAfile	= "cat "+this.javinoPath+"WLANs/*.conf > /etc/wpa_supplicant/wpa_supplicant.conf; touch /tmp/javinoAPMode";
	private final String restartWLAN 	= "systemctl stop wpa_supplicant.service; systemctl stop networking.service; systemctl stop dhcpcd.service; systemctl stop isc-dhcp-server.service; systemctl start wpa_supplicant.service; systemctl start networking.service; systemctl start dhcpcd.service; systemctl start isc-dhcp-server.service";
	private final String pathTask 		= "/tmp/javinoTask";
	private final String delWLANsConf 	= "rm -rf "+this.javinoPath+"WLANs/lan_*.conf";
	
	
	public ShellCommands() {
		
	}
	
	public String getJavinoPath() {
		return this.javinoPath;
	}
	public String getCreateWPAfile() {
		return this.createWPAfile;
	}
	
	public String getDelWLANsConf() {
		return this.delWLANsConf;
	}
	
	public String getAPModeWLAN() {
		return this.confAPModeWLAN;
	}
	
	/*public String getClientWLAN() {
		return this.confClientWLAN;
	}*/
	
	public String getRestartWLAN() {
		return this.restartWLAN;
	}
	
/*	public String createTask() {
		return "sudo rm /tmp/task; echo '#! /bin/sh' > /tmp/task";
	}
	
	public String addTask(String task) {
		return "echo '"+task+"' >> /tmp/task";
	}
	
	public String playTask() {
		return "mv /tmp/task "+this.pathTask;
	}*/
	
	
	public String getNewWLANConf(String connectESSID, String connectKEY) {
		return  "wpa_passphrase "+connectESSID+" "+connectKEY+" > "+this.javinoPath+"WLANs/lan_"+connectESSID+".conf";
	}

	

}
