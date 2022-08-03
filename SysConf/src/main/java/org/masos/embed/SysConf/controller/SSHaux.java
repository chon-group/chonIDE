package org.masos.embed.SysConf.controller;

import java.io.InputStream;
import com.jcraft.jsch.*;

public class SSHaux {
	
	private String 	hostname;
	private int		port;
	private boolean connected;
	
	JSch jsch = new JSch();
	java.util.Properties config = new java.util.Properties(); 
	Session session;
	Channel channel;
	ChannelSftp channelSftp;
	
	public SSHaux() {
		super();
		this.config.put("StrictHostKeyChecking", "no");
	}
	
	public SSHaux(String hostname, int port) {
		super();
		this.hostname = hostname;
		this.port = port;
		this.config.put("StrictHostKeyChecking", "no");
	}
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isConnected() {
		return connected;
	}

	private void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean connect(String username, String password) {
		try {
	    	session=jsch.getSession(username,getHostname(),getPort());
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
	    	setConnected(true);
	    	return isConnected();
		}catch(Exception e){
			System.out.println(e);
			setConnected(false);
			return isConnected();
		}
	}
	
	public boolean testConnect(String username, String password) {
		if(connect(username,password)==true) {
	    	disconnect();
	    	return true;
		}else {
			return false;
		}	
	}
	
	public void disconnect() {
		setConnected(false);
		session.disconnect();
	}
	
	public String exec(String command) {
		System.out.println(command);
		String output = new String();
		if (isConnected()) {
			try {
				channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(command);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				InputStream in = channel.getInputStream();
				channel.connect();
				byte[] tmp = new byte[1024];
				while (true) {
					while (in.available() > 0) {
						int i = in.read(tmp, 0, 1024);
						if (i < 0)
							break;
						output = output + (new String(tmp, 0, i));
					}
					if (channel.isClosed()) {
						if (channel.getExitStatus() != 0) {
							output = null;
						}
						break;
					}
				}
				channel.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		} else {
			output = null;
		}
		return output;
	}

	public void channelSFTPopen(){	
		try {
			this.channelSftp = (ChannelSftp) session.openChannel("sftp");
			this.channelSftp.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public void sendFile(String localFile, String remoteFile){
	    try {
			this.channelSftp.put(localFile, remoteFile);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public void channelSFTPclose() {
		this.channelSftp.exit();
	}
}
