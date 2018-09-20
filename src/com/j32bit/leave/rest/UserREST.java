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

import com.j32bit.leave.bean.Leave;
import com.j32bit.leave.bean.User;
import com.j32bit.leave.service.ServiceFacade;

@Path("/user")
public class UserREST {


	@Path("/getAuthenticatedUser")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public User getAuthenticatedUser(@Context HttpServletRequest request) {
		
		System.out.println("Entered getAuthenticatedUser rest");

		HttpSession session = request.getSession();
		User authenticatedUser = (User) session.getAttribute("LOGIN_USER");

	
		return authenticatedUser;
	}
	
	
	@Path("/getAllUsers")
	@GET
	@RolesAllowed("admin")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAllUser() throws Exception{
		System.out.println("Entered getAllUser rest");
		return ServiceFacade.getInstance().getUserDAO().getAllUsers();
	}
	

	@Path("/deleteUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void deleteUser(String email) throws Exception {
		System.out.println("Entered deleteUser rest");
		ServiceFacade.getInstance().getUserDAO().deleteUser(email);
	}
	

	@Path("/decreaseUserLeaveDays")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void decreaseUserLeaveDays(Leave leave) throws Exception {
		System.out.println("Entered decreaseUserLeaveDays rest");
		ServiceFacade.getInstance().getUserDAO().decreaseUserLeaveDays(leave);

	}
	
	@Path("/addUserLeaveDays")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void addUserLeaveDays(User user) throws Exception {
		System.out.println("Entered addUserLeaveDays rest");
		ServiceFacade.getInstance().getUserDAO().addUserLeaveDays(user);
	}
	
	@Path("/addNewUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void addNewUser(User user) throws Exception {
		System.out.println("Name:" + user.getName());
		System.out.println("Email:" + user.getEmail());
		System.out.println("ProjectManager:" + user.getProjectManager());
		System.out.println("Department:" + user.getDepartment());
		System.out.println("Password:" + user.getPassword());
		System.out.println("Total Leave Days:" + user.getTotalLeaveDays());

		
		for(int i=0;i<user.getRoles().size();i++) {
			System.out.println("Roles["+i+"]=" + user.getRoles().get(i) );
		}
		
		ServiceFacade.getInstance().getUserDAO().addNewUser(user);


		
	}
	@Path("/getEmployeersOfProjectManager")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("projectManager")
	public ArrayList<User> getEmployeersOfProjectManager(String emailOfProjectManager) throws Exception {
		System.out.println("projectManagerEmail:" + emailOfProjectManager);


		return ServiceFacade.getInstance().getUserDAO().getEmployeersOfProjectManager(emailOfProjectManager);


		
	}
	
	



}