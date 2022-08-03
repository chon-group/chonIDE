package org.masos.embed.SysConf.model;

public class User {
	private String Username;
	private String Password;
	private String Session;

	public User() {
		
	}

	public User(String username, String password, String session) {
		super();
		this.Username = username;
		this.Password = password;
		this.Session = session;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		this.Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		this.Password = password;
	}

	public String getSession() {
		return Session;
	}

	public void setSession(String session) {
		this.Session = session;
	}
	
	public void logout() {
		this.Username = "null";
		this.Password = "null";
		this.Session = "null";
	}
}
