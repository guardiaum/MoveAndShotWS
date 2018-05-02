package com.johnymoreira.moveandshotws.workers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import com.johnymoreira.moveandshotws.dao.PoiImageDAO;
import com.johnymoreira.moveandshotws.pojo.PoiImage;
import com.johnymoreira.moveandshotws.util.CaffeUtil;
import com.johnymoreira.moveandshotws.util.Constants;

import redis.clients.jedis.Jedis;
import redis.rmq.Callback;
import redis.rmq.Consumer;

public class PoiImagePredictionWorker extends Thread {

	private List<Thread> runningJobs = new ArrayList<Thread>();
	
	@Context
	private ServletContext wsc;
	private String contextPath;
	
	public PoiImagePredictionWorker(ServletContext wsc) {
		this.wsc = wsc;
		this.contextPath = this.wsc.getRealPath("");
	}
	
	public void run() {
		Consumer c = new Consumer(new Jedis("localhost"), "imagepredictionworker", Constants.REDIS_PENDENT_POI_IMAGES_TOPIC);
		c.consume(new Callback() {
			
			@Override
			public void onMessage(final String poiImageId) { 
				System.out.println("Starting to predict the new image with ID " + poiImageId);
				System.out.println("Running jobs:");
				for(Thread job : runningJobs) {
					System.out.println(job.getName());
				}
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {			
						PoiImage poiImage = PoiImageDAO.getPoiImage(Integer.parseInt(poiImageId));
						String image_path = contextPath+System.getProperty("file.separator")+poiImage.getImagePath();
						CaffeUtil.predict(poiImage,image_path);
					}
				});
				runningJobs.add(t);
				t.run();	
			}
		});
	}
	
	
	

}
