package com.j32bit.leave.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import com.j32bit.leave.bean.Message;

public class MessageDAO extends ConnectionHelper{
	
	public void start(Properties prop) throws Exception {
		super.init(prop);
	}
	
	public void sendMessage(Message message) throws Exception{
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO messages (sender_email,receiver_email,title,content) VALUES (?,?,?,?)");
		
		preparedStmt.setString(1, message.getSender());
		preparedStmt.setString(2, message.getReceiver());
		preparedStmt.setString(3, message.getTitle());
		preparedStmt.setString(4, message.getContent());
		
		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
	}
	
	
	public ArrayList<Message> getAllMessages(String email) throws Exception{
		
		Connection conn = getConnection();
		ArrayList<Message> messages = new ArrayList<Message>();
		PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM messages WHERE receiver_email=?");
		preparedStmt.setString(1, email);
		
		ResultSet rs = preparedStmt.executeQuery();

		while(rs.next()) {
			Message message = new Message();
			
			message.setSender(rs.getString("sender_email"));
			message.setTitle(rs.getString("title"));
			message.setContent(rs.getString("content"));
			message.setId(rs.getLong("id"));
			
			messages.add(message);
			
		}
	    closeResultSet(rs);	   
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
		
		return messages;
		
	}
	
	public void deleteMessage(String messageID) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement pst= conn.prepareStatement("Delete from messages where id=?");
		pst.setLong(1,Long.parseLong(messageID));
		pst.executeUpdate();
		closePreparedStatement(pst);
		
		closePreparedStatement(pst);
		closeConnection(conn);
		
		
	}

}
