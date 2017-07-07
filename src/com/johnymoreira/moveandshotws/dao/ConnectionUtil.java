package com.johnymoreira.moveandshotws.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.johnymoreira.moveandshotws.facade.MaSPOIsFacade;
import com.johnymoreira.moveandshotws.util.Constants;

public class ConnectionUtil {
	
	public static Connection createConnection() throws SQLException {
		
		Properties p = MaSPOIsFacade.loadSetup();
		return DriverManager.getConnection(p.getProperty(Constants.DATABASE_URL), p.getProperty(Constants.DATABASE_USER), p.getProperty(Constants.DATABASE_PASSWORD));
	}

}
