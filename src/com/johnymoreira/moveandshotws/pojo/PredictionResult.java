package com.johnymoreira.moveandshotws.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.postgis.Polygon;

/**
 * POJO para definição de POIImage (Point of Interest Image)
 * 
 * @author Thiago Leonel
 */
@XmlRootElement
public class PredictionResult {
	private int id;
	private int prediction_id;
	private String result;
	private String status;

	public PredictionResult() {}
	
	public PredictionResult(int id, int prediction_id, String result, String status) {
		super();
		this.id = id;
		this.prediction_id = prediction_id;
		this.result = result;
		this.status = status;
	}

	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getPrediction_id() {
		return prediction_id;
	}


	public void setPrediction_id(int prediction_id) {
		this.prediction_id = prediction_id;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	
	
	
}
