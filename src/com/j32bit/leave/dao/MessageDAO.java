package com.j32bit.leave.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

}
