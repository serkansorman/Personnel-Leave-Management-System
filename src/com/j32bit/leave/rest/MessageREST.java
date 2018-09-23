package com.j32bit.leave.rest;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.j32bit.leave.bean.Message;
import com.j32bit.leave.service.ServiceFacade;

@Path("/message")
public class MessageREST {
	
	final Logger logger = Logger.getLogger(MessageREST.class);

	
	@Path("/sendMessage")
	@POST
	@RolesAllowed({"projectManager","admin"})
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMessage(Message message) throws Exception{
		ServiceFacade.getInstance().getMessageDAO().sendMessage(message);
		logger.debug("Entered sendMessage rest, sender:"+message.getSender());
	}
	
	
	@Path("/getAllMessages")
	@POST
	@RolesAllowed({"projectManager","admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> getAllMessages(String email) throws Exception{
		logger.debug("Entered getAllMessages rest, messages owner:"+email);
		return ServiceFacade.getInstance().getMessageDAO().getAllMessages(email);		
	}
	
	
	@Path("/deleteMessage")
	@POST
	@RolesAllowed({"projectManager","admin"})
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteMessage(String messageID) throws Exception {
		ServiceFacade.getInstance().getMessageDAO().deleteMessage(messageID);
		logger.debug("Entered deleteMessage rest, messageID:"+messageID);
	}



}
