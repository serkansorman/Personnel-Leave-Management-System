package com.j32bit.leave.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
		System.out.println("Entered sendMessage rest");
		ServiceFacade.getInstance().getMessageDAO().sendMessage(message);
	
	}
	
	
	@Path("/getAllMessages")
	@POST
	@RolesAllowed({"projectManager","admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> getAllMessages(String email) throws Exception{
		System.out.println("Entered getAllMessages rest");
		return ServiceFacade.getInstance().getMessageDAO().getAllMessages(email);		
	}
	
	
	@Path("/deleteMessage")
	@POST
	@RolesAllowed({"projectManager","admin"})
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteMessage(String messageID) throws Exception {
		System.out.println("Entered deleteMessage rest");
		ServiceFacade.getInstance().getMessageDAO().deleteMessage(messageID);
		
	}



}
