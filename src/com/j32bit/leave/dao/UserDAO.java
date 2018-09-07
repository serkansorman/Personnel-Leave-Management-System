package com.j32bit.leave.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import com.j32bit.leave.bean.User;


public class UserDAO extends ConnectionHelper {
	
	public void start(Properties prop) throws ClassNotFoundException {
		super.init(prop);
	}
	
	public void finish() {
		/**
		 * 
		 */
	}
	
	public User getUser(String email) throws Exception {
	
		Connection conn = getConnection();
		User user = new User();
		PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM user_roles WHERE email=?");
		preparedStmt.setString(1, email);
		ResultSet rs = preparedStmt.executeQuery();
		
		if(rs.next()) {
			ArrayList<String> roles = new ArrayList<String>();
			user.setEmail(email);
			roles.add(rs.getString("role_name"));
			user.setRoles(roles);
		}
		
		
		return user;
		
	}

}
