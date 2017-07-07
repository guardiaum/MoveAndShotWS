package com.johnymoreira.maspois.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.johnymoreira.maspois.facade.MaSPOIsFacade;
import com.johnymoreira.maspois.util.Constants;

public class ConnectionUtil {
	
	public static Connection createConnection() throws SQLException {
		
		Properties p = MaSPOIsFacade.loadSetup();
		return DriverManager.getConnection(p.getProperty(Constants.DATABASE_URL), p.getProperty(Constants.DATABASE_USER), p.getProperty(Constants.DATABASE_PASSWORD));
	}

}
