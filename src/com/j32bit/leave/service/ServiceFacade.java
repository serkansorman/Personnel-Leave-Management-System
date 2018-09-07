package com.j32bit.leave.service;


import java.util.Properties;

import com.j32bit.leave.dao.UserDAO;


public class ServiceFacade {
	
	private static ServiceFacade serviceFacade;
	private UserDAO userDAO;
	
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
	}
	
	

	public void finish() {
		/**
		 * 
		 */
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

}
