package com.j32bit.leave.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

import com.j32bit.leave.bean.Leave;

public class LeaveDAO extends ConnectionHelper{
	
	public void addLeave(Leave leave) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO leaves (email,begin_date,end_date,status) VALUES (?,?,?,?)");
		
		preparedStmt.setString(1, leave.getOwner().getEmail());
		preparedStmt.setString(2, leave.getBeginDate());
		preparedStmt.setString(3, leave.getEndDate());
		preparedStmt.setString(4, leave.getStatus());
		
		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
			
	}
	
	public void start(Properties prop) throws Exception {
		super.init(prop);
	}

}
