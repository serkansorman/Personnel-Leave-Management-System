package com.j32bit.leave.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;


public class UserDAO extends ConnectionHelper {
	
	public void start(Properties prop) throws ClassNotFoundException {
		super.init(prop);
	}
	
	public void finish() {
		/**
		 * 
		 */
	}
	
	public String getUserRole(String email) throws Exception {
	
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM user_roles WHERE email=?");
		preparedStmt.setString(1, email);
		ResultSet rs = preparedStmt.executeQuery();
		
		if(rs.next()) {
			return rs.getString("role_name");
		}
		
		return "unAuthorized";
	}

}
