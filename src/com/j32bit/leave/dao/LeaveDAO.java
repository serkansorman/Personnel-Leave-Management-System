package com.j32bit.leave.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import com.j32bit.leave.bean.Leave;
import com.j32bit.leave.bean.LeaveResponse;
import com.j32bit.leave.bean.User;

public class LeaveDAO extends ConnectionHelper{
	
	public void start(Properties prop) throws Exception {
		super.init(prop);
	}
	
	public void addLeave(Leave leave) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO leaves (email,begin_date,end_date,status,work_days) VALUES (?,?,?,?,?)");
		
		preparedStmt.setString(1, leave.getOwner().getEmail());
		preparedStmt.setString(2, leave.getBeginDate());
		preparedStmt.setString(3, leave.getEndDate());
		preparedStmt.setString(4, leave.getStatus());
		preparedStmt.setInt(5, leave.getWorkDays());

		
		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
			
	}
	
	public ArrayList<Leave> getLeaveRequestsPM(String projectManager) throws Exception{
		
		Connection conn = getConnection();
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM users INNER JOIN leaves ");
		query.append("ON users.email = leaves.email AND projectManager=? AND status=?");
		
		PreparedStatement preparedStmt = conn.prepareStatement(query.toString());
		preparedStmt.setString(1, projectManager); // Sadece o proje yöneticisinin altında bulunan personelin iznini göster.
		preparedStmt.setString(2, "on Project Manager"); //Project manager tarafından onay bekleyen izinleri göster.
		
		ResultSet rst = preparedStmt.executeQuery();
	    ArrayList<Leave> leaveList = new ArrayList<>();
	    while (rst.next()) {
	    	Leave leave = new Leave();
	    	User user = new User();
	    	
	    	user.setEmail(rst.getString("email"));
	    	user.setName(rst.getString("full_name"));
	    	user.setTotalLeaveDays(rst.getInt("totalLeaveDays"));
	    	
	    	leave.setOwner(user);
	    	leave.setBeginDate(rst.getString("begin_date"));
	    	leave.setEndDate(rst.getString("end_date"));
	    	leave.setStatus(rst.getString("status"));
	    	leave.setId(rst.getLong("id"));
	    	leave.setWorkDays(rst.getInt("work_days"));

	        leaveList.add(leave);
	    }
	    
	    closeResultSet(rst);
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
		
		return leaveList;
		
	}
	
	
	public ArrayList<Leave> getLeaveRequestsAdmin() throws Exception{
		
		Connection conn = getConnection();
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM users INNER JOIN leaves ");
		query.append("ON users.email = leaves.email AND status=?");
		
		PreparedStatement preparedStmt = conn.prepareStatement(query.toString());
		preparedStmt.setString(1, "on Admin"); //Admin tarafından onay bekleyen izinleri göster.
		
		ResultSet rst = preparedStmt.executeQuery();
	    ArrayList<Leave> leaveList = new ArrayList<>();
	    while (rst.next()) {
	    	Leave leave = new Leave();
	    	User user = new User();
	    	
	    	user.setEmail(rst.getString("email"));
	    	user.setName(rst.getString("full_name"));
	    	user.setTotalLeaveDays(rst.getInt("totalLeaveDays"));
	    	
	    	leave.setOwner(user);
	    	leave.setBeginDate(rst.getString("begin_date"));
	    	leave.setEndDate(rst.getString("end_date"));
	    	leave.setStatus(rst.getString("status"));
	    	leave.setId(rst.getLong("id"));
	    	leave.setWorkDays(rst.getInt("work_days"));


	        leaveList.add(leave);
	    }
	    
	    closeResultSet(rst);
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
		
		return leaveList;
	}
	
	public void respondLeaveRequest(LeaveResponse leaveResponse) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement("UPDATE leaves SET status=? WHERE id=?");
		
		preparedStmt.setString(1, leaveResponse.getStatus());
		preparedStmt.setLong(2, Long.parseLong(leaveResponse.getLeaveID()));

		
		preparedStmt.execute();
		
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
	}
	
	
	
	public ArrayList<Leave> getLeaves(String email) throws Exception {
		
		Connection conn = getConnection();
		ArrayList<Leave> leaves = new ArrayList<Leave>();
		PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM leaves WHERE email=?");
		preparedStmt.setString(1, email);
		
		ResultSet rs = preparedStmt.executeQuery();

		while(rs.next()) {
			leaves.add(new Leave(rs.getString("begin_date"),rs.getString("end_date"),rs.getString("status"),rs.getLong("id")));
		}
	    closeResultSet(rs);	   
		closePreparedStatement(preparedStmt);
		closeConnection(conn);
		
		return leaves;
	}
	
	
	public void cancelLeave(String leaveID) throws Exception {
		
		Connection conn = getConnection();
		PreparedStatement pst= conn.prepareStatement("Delete from leaves where id=?");
		pst.setLong(1,Long.parseLong(leaveID));
		pst.executeUpdate();
		closePreparedStatement(pst);
	
		closeConnection(conn);

	}

}
