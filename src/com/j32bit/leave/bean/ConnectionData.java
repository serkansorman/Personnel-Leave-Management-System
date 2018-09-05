package com.j32bit.leave.bean;

public class ConnectionData {
	
	private String driver;
	private String dbUrl;
	private String dbUserName;
	private String dbPassword;
	private boolean useDataSource;
	private String JNDIname;
	
	public ConnectionData() {
		
	}

	public ConnectionData(String driver, String dbUrl, String dbUserName, String dbPassword, boolean useDataSource,
			String jNDIname) {
		super();
		this.driver = driver;
		this.dbUrl = dbUrl;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		this.useDataSource = useDataSource;
		JNDIname = jNDIname;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public boolean isUseDataSource() {
		return useDataSource;
	}

	public void setUseDataSource(boolean useDataSource) {
		this.useDataSource = useDataSource;
	}

	public String getJNDIname() {
		return JNDIname;
	}

	public void setJNDIname(String jNDIname) {
		JNDIname = jNDIname;
	}
	
	
	
	

}
