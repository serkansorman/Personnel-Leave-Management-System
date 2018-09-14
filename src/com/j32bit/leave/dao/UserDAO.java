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
		PreparedStatement preparedStmt2 = conn.prepareStatement("SELECT * FROM users WHERE email=?");

		
		preparedStmt.setString(1, email);
		preparedStmt2.setString(1, email);

		
		ResultSet rs = preparedStmt.executeQuery();
		ResultSet rs2 = preparedStmt2.executeQuery();
		
		if(rs.next() && rs2.next()) {
			user.setEmail(rs2.getString("email"));
			user.setPassword(rs2.getString("user_pass"));
			user.setName(rs2.getString("full_name"));
			user.setDepartment(rs2.getString("department"));
			user.setProjectManager(rs2.getString("projectManager"));
			user.setTotalLeaveDays(rs2.getInt("totalLeaveDays"));
			
			ArrayList<String> roles = new ArrayList<String>();
			roles.add(rs.getString("role_name"));
			user.setRoles(roles);
		}
		
	    closeResultSet(rs);
	    closeResultSet(rs2);
		closePreparedStatement(preparedStmt);
		closePreparedStatement(preparedStmt2);

		closeConnection(conn);
		return user;
		
	}
	
	
	public ArrayList<User> getAllUsers() throws Exception{
		
		Connection conn = getConnection();
		PreparedStatement pre = conn.prepareStatement("Select * from users");
	    ResultSet rst = pre.executeQuery();
	    ArrayList<User> userList = new ArrayList<>();
	    while (rst.next()) {
	       User user = new User(rst.getString("email"), rst.getString("user_pass"), 
	    		   rst.getString("full_name"),rst.getString("department"),
	    		   rst.getInt("totalLeaveDays"),rst.getString("projectManager"));
	       userList.add(user);
	    }
	    
	    closeResultSet(rst);
		closePreparedStatement(pre);
		closeConnection(conn);
		
	    return userList;
	
	}
	
	
	
	
	public void deleteUser(String email) throws Exception {
		
		//users tablosundan silme
		Connection conn = getConnection();
		PreparedStatement pstUser = conn.prepareStatement("Delete from users where email=?");
		pstUser.setString(1,email);
		pstUser.executeUpdate();
		closePreparedStatement(pstUser);

		//users_role tablosundan silme
		PreparedStatement pstRole = conn.prepareStatement("Delete from user_roles where email=?");
		pstRole.setString(1,email);
		pstRole.executeUpdate();
		closePreparedStatement(pstRole);
		
	
		closeConnection(conn);

	}

}
