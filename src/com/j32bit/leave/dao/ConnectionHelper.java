package com.j32bit.leave.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;


import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.j32bit.leave.bean.ConnectionData;


public class ConnectionHelper {
	
	private ConnectionData connectionData;

	public void init(Properties prop) throws ClassNotFoundException{
		
		System.out.println("Connection data bean initialized");

		connectionData = new ConnectionData();
		
		connectionData.setDriver(prop.getProperty("database.jdbc.driver"));
		connectionData.setDbUrl(prop.getProperty("database.jdbc.url"));
		connectionData.setDbUserName(prop.getProperty("database.dbUserName"));
		connectionData.setDbPassword(prop.getProperty("database.dbPassword"));
		connectionData.setUseDataSource(Boolean.valueOf(prop.getProperty("database.useDataSource")));
		connectionData.setJNDIname(prop.getProperty("database.jndi.name"));
		
	}
	

	public Connection getConnection() throws Exception {
		Connection conn = null;
		if(connectionData.isUseDataSource()) {
			InitialContext ic = new InitialContext();
			BasicDataSource ds = (BasicDataSource) ic.lookup(connectionData.getJNDIname());
			conn = ds.getConnection();
		}else {
			
			Class.forName(connectionData.getDriver());
			conn = DriverManager.getConnection(connectionData.getDbUrl(), connectionData.getDbUserName(), connectionData.getDbPassword());
	
		}
		
		conn.setAutoCommit(false);		
		return conn;
	}
	

	public void closeConnection(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}
	
	
	public void closePreparedStatement (PreparedStatement pst) throws SQLException {
		if(pst != null)
			pst.close();
	}
	

	public void rollBackTransaction(Connection conn) {
		
		if (conn != null) {
			try {
				conn.rollback();			
			} catch (SQLException excep) {
				excep.printStackTrace();
			}
		}
	}

}
