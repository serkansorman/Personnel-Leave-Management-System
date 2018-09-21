package com.j32bit.leave.bean;

public class Message {
	
	private String sender;
	private String receiver;
	private String title;
	private String content;
	private long id;
	

	public Message() {
		
	}

	public Message(String sender, String receiver, String title, String content) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.title = title;
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
	
	
	

}
