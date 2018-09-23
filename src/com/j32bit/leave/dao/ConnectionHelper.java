package com.j32bit.leave.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.j32bit.leave.bean.ConnectionData;


public class ConnectionHelper {
	
	private ConnectionData connectionData;
	private final Logger logger = Logger.getLogger(ConnectionHelper.class);


	public void init(Properties prop) throws ClassNotFoundException{
		

		connectionData = new ConnectionData();
		
		connectionData.setDriver(prop.getProperty("database.jdbc.driver"));
		connectionData.setDbUrl(prop.getProperty("database.jdbc.url"));
		connectionData.setDbUserName(prop.getProperty("database.dbUserName"));
		connectionData.setDbPassword(prop.getProperty("database.dbPassword"));
		connectionData.setUseDataSource(Boolean.valueOf(prop.getProperty("database.useDataSource")));
		connectionData.setJNDIname(prop.getProperty("database.jndi.name"));
		
		logger.debug("Connection data bean initialized");
	}
	

	public Connection getConnection() throws Exception {
		
		if(connectionData.isUseDataSource()) 
			return getConnectionFromDataSource();
		
		return getConnectionLocal();		
	}
	
	public Connection getConnectionFromDataSource() throws Exception {
		
		InitialContext ic = new InitialContext();
		BasicDataSource ds = (BasicDataSource) ic.lookup(connectionData.getJNDIname());
		Connection conn = ds.getConnection();
		
		logger.debug("Connection established from data source");

		return conn;
	}
	
	
	public Connection getConnectionLocal() throws Exception {
		
		Class.forName(connectionData.getDriver());
		Connection conn = DriverManager.getConnection(connectionData.getDbUrl(), connectionData.getDbUserName(), connectionData.getDbPassword());
		logger.debug("Connection established from driver manager");

		return conn;
	}
	
	
	public Connection getTransactionConnection() throws Exception {
		Connection conn = getConnection();
		
		if (conn != null && !conn.isClosed()) {
			if (conn.getAutoCommit()) {
				conn.setAutoCommit(false);
			} else {
				conn.rollback();
			}
		}
		
		logger.debug("Transaction connection established");

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
	
	
	public void closeResultSet(ResultSet rs) throws Exception {
		if(rs != null)
			rs.close();
	}
	

	public void rollBackTransaction(Connection conn) {
		
		if (conn != null) {
			try {
				conn.rollback();			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
