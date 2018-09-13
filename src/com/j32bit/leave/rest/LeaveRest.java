package com.j32bit.leave.rest;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.j32bit.leave.bean.Leave;
import com.j32bit.leave.service.ServiceFacade;

@Path("/leave")
public class LeaveRest {
	
	@Path("/addLeave")
	@POST
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON)
	public void addLeave(Leave leave) throws Exception {
		ServiceFacade.getInstance().getLeaveDAO().addLeave(leave);
	}
	
	
	@Path("/getLeaveRequests")
	@POST
	@RolesAllowed("projectManager")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Leave> getLeaveRequests(String projectManager) throws Exception{
		System.out.println("Entered getLeaveRequests rest");
		return ServiceFacade.getInstance().getLeaveDAO().getLeaveRequests(projectManager);
	}
	
	


}
