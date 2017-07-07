package com.johnymoreira.maspois.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.johnymoreira.maspois.dao.PoiDAO;
import com.johnymoreira.maspois.pojo.Poi;
import com.johnymoreira.maspois.util.Constants;

public class MaSPOIsFacade implements ServletContextListener{
	
	static ClassLoader classLoader = MaSPOIsFacade.class.getClassLoader();
	private static String contextPath;
	private static Properties properties = null;
	
	public static String getContextPath(){
		return contextPath;
	}
	
	public static Properties loadSetup(){
		if (properties==null) {
			properties = new Properties();
		
			String pathname = contextPath+System.getProperty("file.separator")+Constants.DATABASE_INI_FILE;
			if (contextPath == null){
				pathname = Constants.DATABASE_INI_FILE;
			}
			
			File f = new File(pathname);
			if (f.exists()) {
				try {
					FileInputStream inStream = new FileInputStream(pathname);
					properties.load(inStream);
					inStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent ctx) {}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		contextPath = contextEvent.getServletContext().getRealPath("")+System.getProperty("file.separator")+"WEB-INF";
	}

	public static int storePoi(Poi poi) {
		return PoiDAO.storePoi(poi);
	}

	public static void removePoi(String id) {
		PoiDAO.removePoi(id);
	}

	public static List<Poi> getPois() {
		return PoiDAO.getPois();
	}

	public static Poi getPoi(int id) {
		return PoiDAO.getPoi(id);
	}
	
	public static void update(Poi poi){
		PoiDAO.update(poi);
	}
}
