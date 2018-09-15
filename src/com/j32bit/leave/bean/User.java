package com.j32bit.leave.bean;

import java.util.ArrayList;

public class User {
	
	private String email;
	private String password;
	private String name;
	private String department;
	private int totalLeaveDays;
	private String projectManager;
	private ArrayList<String> roles;
	private ArrayList<String> beginDates;
	private ArrayList<String> endDates;
	private ArrayList<String> statuss;
	
	public User() {
		
	}
	
	public User(String email, String password, String name, String department, int totalLeaveDays,
			String projectManager) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.department = department;
		this.totalLeaveDays = totalLeaveDays;
		this.projectManager = projectManager;
	}
	
	
	public User(String email, String password, String name, String department, int totalLeaveDays,
			String projectManager, ArrayList<String> beginDates, ArrayList<String> endDates, ArrayList<String> statuss) {
		
		this(email,password,name,department,totalLeaveDays,projectManager);
		this.beginDates=beginDates;
		this.endDates=endDates;
		this.statuss=statuss;
	}

	public ArrayList<String> getBeginDates(){
		return beginDates;
	}
	public ArrayList<String> getEndDates(){
		return endDates;
	}
	public ArrayList<String> getStatuss(){
		return statuss;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getTotalLeaveDays() {
		return totalLeaveDays;
	}

	public void setTotalLeaveDays(int totalLeaveDays) {
		this.totalLeaveDays = totalLeaveDays;
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

	public void setStatuss(ArrayList<String> statuss) {
		this.statuss=statuss;
	}
	public void setBeginDates(ArrayList<String> beginDates) {
		this.beginDates=beginDates;
	}
	public void setEndDates(ArrayList<String> endDates) {
		this.endDates=endDates;
	}
	

}
