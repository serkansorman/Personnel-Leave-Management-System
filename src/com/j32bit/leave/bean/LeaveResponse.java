package com.j32bit.leave.bean;

public class LeaveResponse {
	
	private String sender;
	private String leaveID;
	private String status;
	
	public LeaveResponse() {
	}
	

	public LeaveResponse(String sender, String leaveID, String status) {
		this.sender = sender;
		this.leaveID = leaveID;
		this.status = status;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getLeaveID() {
		return leaveID;
	}


	public void setLeaveID(String leaveID) {
		this.leaveID = leaveID;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
}
