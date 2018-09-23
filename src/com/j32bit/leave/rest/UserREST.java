package com.j32bit.leave.rest;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.j32bit.leave.bean.Leave;
import com.j32bit.leave.bean.User;
import com.j32bit.leave.service.ServiceFacade;

@Path("/user")
public class UserREST {
	
	final Logger logger = Logger.getLogger(UserREST.class);

	@Path("/getAuthenticatedUser")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public User getAuthenticatedUser(@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		User authenticatedUser = (User) session.getAttribute("LOGIN_USER");
		
		logger.debug("Entered getAuthenticatedUser rest, user e-mail:"+authenticatedUser.getEmail());
		return authenticatedUser;
	}
	
	
	@Path("/getAllUsers")
	@GET
	@RolesAllowed("admin")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAllUser() throws Exception{
		logger.debug("Entered getAllUser rest");
		return ServiceFacade.getInstance().getUserDAO().getAllUsers();
	}
	

	@Path("/deleteUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void deleteUser(String email) throws Exception {
		ServiceFacade.getInstance().getUserDAO().deleteUser(email);
		logger.debug("Entered deleteUser rest, user email:"+email);
	}
	

	@Path("/decreaseUserLeaveDays")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void decreaseUserLeaveDays(Leave leave) throws Exception {
		ServiceFacade.getInstance().getUserDAO().decreaseUserLeaveDays(leave);
		logger.debug("Entered decreaseUserLeaveDays rest, leave owner:"+leave.getOwner().getEmail());
	}
	
	@Path("/addUserLeaveDays")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void addUserLeaveDays(User user) throws Exception {
		ServiceFacade.getInstance().getUserDAO().addUserLeaveDays(user);
		logger.debug("Entered addUserLeaveDays rest, user e-mail"+user.getEmail());
	}
	
	@Path("/addNewUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void addNewUser(User user) throws Exception {
		
		for(int i=0;i<user.getRoles().size();i++) 
			System.out.println("Roles["+i+"]=" + user.getRoles().get(i) );
		
		ServiceFacade.getInstance().getUserDAO().addNewUser(user);

		logger.debug("Name:" + user.getName());
		logger.debug("Email:" + user.getEmail());
		logger.debug("ProjectManager:" + user.getProjectManager());
		logger.debug("Department:" + user.getDepartment());
		logger.debug("Password:" + user.getPassword());
		logger.debug("Total Leave Days:" + user.getTotalLeaveDays());
		
	}
	
	
	@Path("/updateUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public void updateUser(User user) throws Exception {
		ServiceFacade.getInstance().getUserDAO().updateUser(user);
		logger.debug("Entered updateUser rest, user e-mail"+user.getEmail());
	}

	
	@Path("/getEmployeersOfProjectManager")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("projectManager")
	public ArrayList<User> getEmployeersOfProjectManager(String emailOfProjectManager) throws Exception {
		logger.debug("projectManagerEmail:" + emailOfProjectManager);
		return ServiceFacade.getInstance().getUserDAO().getEmployeersOfProjectManager(emailOfProjectManager);		
	}
	
	@Path("/removeEmployeeFromProject")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("projectManager")
	public void removeEmployeeFromProject(String employeeEmail) throws Exception {
		ServiceFacade.getInstance().getUserDAO().removeEmployeeFromProject(employeeEmail);
		logger.debug("employeeEmail:" + employeeEmail);
	}
	
	



}