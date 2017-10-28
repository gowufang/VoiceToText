package org.yhn.yq.common;

public class User implements java.io.Serializable{
	private static final long serialVersionUID=7981589250804078637l; 
	String operation="";
	int account=0;
	String password="";
	String nick="";
	int avatar=0; 
	String sex=""; 
	String time="";
	public User(int account, String password) {
		super();
		this.account = account;
		this.password = password;
	}
	public User(int account,String nick ,String sex ) {
		super();
		this.account = account;
		this.nick = nick;
		this.sex = sex;
	}
	public User() {
		super();
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public int getAccount() {
		return account;
	}
	public void setAccount(int account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	 
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	 
	 
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
