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

import org.apache.log4j.Logger;

import com.j32bit.leave.bean.Leave;
import com.j32bit.leave.bean.LeaveResponse;
import com.j32bit.leave.service.ServiceFacade;

@Path("/leave")
public class LeaveRest {
	
	final Logger logger = Logger.getLogger(LeaveRest.class);

	
	@Path("/addLeave")
	@POST
	@RolesAllowed({"projectManager","employee"})
	@Consumes(MediaType.APPLICATION_JSON)
	public void addLeave(Leave leave) throws Exception {
		ServiceFacade.getInstance().getLeaveDAO().addLeave(leave);
		logger.debug("Entered addLeave rest, user email:"+leave.getOwner().getEmail());
	}
	
	
	@Path("/getLeaveRequestsPM")
	@POST
	@RolesAllowed("projectManager")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Leave> getLeaveRequestsPM(String projectManager) throws Exception{
		logger.debug("Entered getLeaveRequestsPM rest, projectManager:"+projectManager);
		return ServiceFacade.getInstance().getLeaveDAO().getLeaveRequestsPM(projectManager);
	}
	
	
	@Path("/getLeaveRequestsAdmin")
	@GET
	@RolesAllowed("projectManager")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Leave> getLeaveRequestsAdmin() throws Exception{
		logger.debug("Entered getLeaveRequestsAdmin rest");
		return ServiceFacade.getInstance().getLeaveDAO().getLeaveRequestsAdmin();

	}
	
	

	@Path("/respondLeaveRequest")
	@POST
	@RolesAllowed({"projectManager","admin"})
	@Consumes(MediaType.APPLICATION_JSON)
	public void respondLeaveRequest(LeaveResponse leaveRespond) throws Exception {
		logger.debug("Entered acceptLeaveRequest rest id:"+leaveRespond.getLeaveID());
		ServiceFacade.getInstance().getLeaveDAO().respondLeaveRequest(leaveRespond);
					
	}
	
	
	@Path("/getLeaves")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Leave> getLeaves(String email) throws Exception{
		logger.debug("Entered getLeaves rest, email:"+email);
		return ServiceFacade.getInstance().getLeaveDAO().getLeaves(email);
	}
	

	@Path("/cancelLeave")
	@POST
	@RolesAllowed("employee")
	@Consumes(MediaType.APPLICATION_JSON)
	public void cancelLeave(String leaveID) throws Exception {
		System.out.println("Entered cancelLeave rest, leaveID:"+leaveID);
		ServiceFacade.getInstance().getLeaveDAO().cancelLeave(leaveID);	
	}
	

	

}
