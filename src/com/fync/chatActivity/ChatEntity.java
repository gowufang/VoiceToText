package com.fync.chatActivity;


public class ChatEntity {
	private int avatar;
	private String content;
	private String time;
	private boolean isLeft;//�Ƿ�Ϊ�յ�����Ϣ�������
	
	public ChatEntity(int avatar,String content,String time,boolean isLeft){
		this.avatar = avatar;
		this.content = content;
		this.time = time;
		this.isLeft = isLeft;
	}
	
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isLeft() {
		return isLeft;
	}
	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}
}
