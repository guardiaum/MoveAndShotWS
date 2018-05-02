package com.johnymoreira.moveandshotws.app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.johnymoreira.moveandshotws.workers.PoiImagePredictionWorker;

public class ServletContextClass implements ServletContextListener
    {
	private PoiImagePredictionWorker poiImagePredictionWorker;

    public void contextInitialized(ServletContextEvent arg0) 
    {
    	poiImagePredictionWorker = new PoiImagePredictionWorker(arg0.getServletContext());
    	poiImagePredictionWorker.start();
    }


    public void contextDestroyed(ServletContextEvent arg0) 
    {
    	  poiImagePredictionWorker.interrupt();   
    }

}