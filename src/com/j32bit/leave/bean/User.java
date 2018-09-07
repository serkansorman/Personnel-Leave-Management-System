package com.j32bit.leave.bean;

import java.util.ArrayList;

public class User {
	
	private String email;
	private String password;
	private String name;
	private String leaveDate;
	private ArrayList<String> roles;
	
	public User() {
		
		
	}
	public User(String email, String password, String name, String leaveDate, ArrayList<String> roles) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.leaveDate = leaveDate;
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

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

}
