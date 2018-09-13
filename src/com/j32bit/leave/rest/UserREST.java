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

}
