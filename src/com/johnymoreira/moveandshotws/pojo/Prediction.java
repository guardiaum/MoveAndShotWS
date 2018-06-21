package com.johnymoreira.moveandshotws.pojo;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO para definição de Prediction 
 * 
 * @author Thiago Leonel
 */
@XmlRootElement
public class Prediction {
	private int id;
	private int poiImageId;
	private Timestamp timestamp;
	private Double execution_time;

	public Prediction() {}
	

	public Prediction(int id, int poiImageId , Timestamp timestamp, Double execution_time) {
		this.id = id;
		this.poiImageId = poiImageId;
		this.timestamp = timestamp;
		this.execution_time = execution_time;
	}


	public Prediction(int poiImageId , Timestamp timestamp, Double execution_time) {
		this.poiImageId = poiImageId;
		this.timestamp = timestamp;
		this.execution_time = execution_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPoiImageId() {
		return poiImageId;
	}

	public void setPoiImageId(int poiImageId) {
		this.poiImageId = poiImageId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Double getExecution_time() {
		return execution_time;
	}

	public void setExecution_time(Double execution_time) {
		this.execution_time = execution_time;
	}

	
	
	
}
