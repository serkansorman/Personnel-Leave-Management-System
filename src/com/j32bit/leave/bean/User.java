package com.j32bit.leave.bean;

import java.util.ArrayList;

public class User {
	
	private String email;
	private String password;
	private String name;
	private int totalLeaveDays;
	private String leaveDate;
	private String projectManager;
	private ArrayList<String> roles;
	
	public User() {
		
	}

	public User(String email, String password, String name, int totalLeaveDays, String leaveDate, String projectManager,
			ArrayList<String> roles) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.totalLeaveDays = totalLeaveDays;
		this.leaveDate = leaveDate;
		this.projectManager = projectManager;
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalLeaveDays() {
		return totalLeaveDays;
	}

	public void setTotalLeaveDays(int totalLeaveDays) {
		this.totalLeaveDays = totalLeaveDays;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	
	

	
	

}
