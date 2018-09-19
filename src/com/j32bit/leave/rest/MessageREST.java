package com.j32bit.leave.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.j32bit.leave.bean.Message;
import com.j32bit.leave.service.ServiceFacade;

@Path("/message")
public class MessageREST {
	
	@Path("/sendMessage")
	@POST
	@RolesAllowed({"projectManager","admin"})
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMessage(Message message) throws Exception{
		System.out.println("Entered message rest");
		ServiceFacade.getInstance().getMessageDAO().sendMessage(message);
	
	}


}
