package com.j32bit.leave.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import com.j32bit.leave.bean.Leave;
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
		
		
		if(rs2.next()) {
			user.setEmail(rs2.getString("email"));
			user.setPassword(rs2.getString("user_pass"));
			user.setName(rs2.getString("full_name"));
			user.setDepartment(rs2.getString("department"));
			user.setProjectManager(rs2.getString("projectManager"));
			user.setTotalLeaveDays(rs2.getInt("totalLeaveDays"));
			
			
			ArrayList<String> roles = new ArrayList<String>();
			while(rs.next()) 
				roles.add(rs.getString("role_name"));
		
			
			if(roles != null)
				user.setRoles(roles);
		}
		

	    closeResultSet(rs);
	    closeResultSet(rs2);
	    
		closePreparedStatement(preparedStmt);
		closePreparedStatement(preparedStmt2);

		closeConnection(conn);
		return user;
		
	}
	
	
	public User getUserForAllUsers(String email,Connection conn) throws Exception {
		
		User user = new User();
		PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM user_roles WHERE email=?");
		PreparedStatement preparedStmt2 = conn.prepareStatement("SELECT * FROM users WHERE email=?");

		preparedStmt.setString(1, email);
		preparedStmt2.setString(1, email);
		
		ResultSet rs = preparedStmt.executeQuery();
		ResultSet rs2 = preparedStmt2.executeQuery();
		
		
		if(rs2.next()) {
			user.setEmail(rs2.getString("email"));
			user.setPassword(rs2.getString("user_pass"));
			user.setName(rs2.getString("full_name"));
			user.setDepartment(rs2.getString("department"));
			user.setProjectManager(rs2.getString("projectManager"));
			user.setTotalLeaveDays(rs2.getInt("totalLeaveDays"));
			
			
			ArrayList<String> roles = new ArrayList<String>();
			while(rs.next()) 
				roles.add(rs.getString("role_name"));
		
			
			if(roles != null)
				user.setRoles(roles);
		}
		

	    closeResultSet(rs);
	    closeResultSet(rs2);
	    
		closePreparedStatement(preparedStmt);
		closePreparedStatement(preparedStmt2);

		
		return user;
		
	}
	
	
	
	public ArrayList<User> getAllUsers() throws Exception{
		
		Connection conn = getConnection();
		PreparedStatement pre = conn.prepareStatement("Select * from users");
	    ResultSet rst = pre.executeQuery();
	    ArrayList<User> userList = new ArrayList<>();
	    while (rst.next()) {
	       User user = getUserForAllUsers(rst.getString("email"),conn);
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
		
		
		closePreparedStatement(pstUser);
		closePreparedStatement(pstRole);
		closeConnection(conn);

	}
	
	
	public void decreaseUserLeaveDays(Leave leave) throws Exception{
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("UPDATE users SET totalLeaveDays=? WHERE email=?");
		
		//Personelin izin gününden alınan izin düşülür.
		preparedStmt.setInt(1,leave.getOwner().getTotalLeaveDays() - leave.getWorkDays() );
		preparedStmt.setString(2, leave.getOwner().getEmail());

		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
	}
	
	public void addUserLeaveDays(User user) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("UPDATE users SET totalLeaveDays=? WHERE email=?");
		
		preparedStmt.setInt(1,user.getTotalLeaveDays());
		preparedStmt.setString(2, user.getEmail());

		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
	}

	public void addNewUser(User user)  throws Exception {
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement(" insert into users (email, user_pass, full_name, department, projectManager,totalLeaveDays)"
		        + " values (?, ?, ?, ?, ?, ?)");
		
		preparedStmt.setString(1, user.getEmail());
		preparedStmt.setString(2, user.getPassword());
		preparedStmt.setString(3, user.getName());
		preparedStmt.setString(4, user.getDepartment());
		preparedStmt.setString(5, user.getProjectManager());
		preparedStmt.setInt(6, user.getTotalLeaveDays());
		
		
		PreparedStatement preparedStmt2 = conn.prepareStatement(" insert into user_roles (email,role_name)"
		        + " values (?, ?)");
		
		for(int i=0;i<user.getRoles().size();i++) {
			preparedStmt2.setString(1, user.getEmail());
			preparedStmt2.setString(2, user.getRoles().get(i));
			preparedStmt2.execute();
		}

		
		
		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);

	}
	
	public ArrayList<User> getEmployeersOfProjectManager(String emailOfProjectManager) throws Exception{
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM users WHERE projectManager=?");
		preparedStmt.setString(1, emailOfProjectManager);
		ResultSet rst = preparedStmt.executeQuery();
		
	    ArrayList<User> userList = new ArrayList<>();
		 
		 
	    while (rst.next()) {
	       User user = new User(rst.getString("email"), rst.getString("user_pass"), 
	    		   rst.getString("full_name"),rst.getString("department"),
	    		   rst.getInt("totalLeaveDays"),rst.getString("projectManager"));
	        userList.add(user);
	    }
	    
	    closePreparedStatement(preparedStmt);
		closeConnection(conn);	
	    
	    return userList;

	}
	
	public void removeEmployeeFromProject(String employeeEmail) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("UPDATE users SET projectManager=? WHERE email=?");
		
		preparedStmt.setString(1,"none");
		preparedStmt.setString(2, employeeEmail);
		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
	}

	public void updateUser(User user) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("UPDATE users SET user_pass=? WHERE email=?");
		
		preparedStmt.setString(1,user.getPassword());
		preparedStmt.setString(2, user.getEmail());

		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);		
	}
}