package com.j32bit.leave.service;


import java.util.Properties;

import com.j32bit.leave.dao.LeaveDAO;
import com.j32bit.leave.dao.UserDAO;


public class ServiceFacade {
	
	private static ServiceFacade serviceFacade;
	private UserDAO userDAO;
	private LeaveDAO leaveDAO;
	
	public ServiceFacade(){
		
	}
	
	public static ServiceFacade getInstance() {
		if(serviceFacade == null)
			serviceFacade = new ServiceFacade();
		return serviceFacade;
	}
	
	public void start(Properties prop) throws ClassNotFoundException {
		userDAO = new UserDAO();
		userDAO.start(prop);
		
		leaveDAO = new LeaveDAO();
	}
	
	

	public void finish() {
		/**
		 * 
		 */
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	public LeaveDAO getLeaveDAO() {
		return leaveDAO;
	}

}
