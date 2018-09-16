package com.j32bit.leave.bean;

public class Leave {
	
	private User owner;
	private String beginDate;
	private String endDate;
	private String status;
	private long id;
	
	public Leave() {
		
	}
	
	
	public Leave(String beginDate, String endDate, String status) {
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.status = status;
		
	}

	public Leave(User owner, String beginDate, String endDate, String status) {
		this(beginDate,endDate,status);
		this.owner = owner;
		
	}

	public Leave(User owner, String beginDate, String endDate, String status, long id) {
		this(owner,beginDate,endDate,status);
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
}
