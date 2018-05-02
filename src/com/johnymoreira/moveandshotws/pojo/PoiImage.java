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
public class PoiImage {
	private int id;
	private int poiId;
	private String status;
	private String imagePath;

	public PoiImage() {}
	
	/**
	 * Construtor sobrecarregado de foto de ponto de interesse.
	 * @param id int
	 * @param poiId int
	 * @param status String
	 * @param imagePath String
	 */
	public PoiImage(int id, int poiId , String status, String imagePath) {
		this.id = id;
		this.poiId = poiId;
		this.status = status;
		this.imagePath = imagePath;
	}

	/**
	 * Construtor sobrecarregado de foto de ponto de interesse.
	 * 
	 * @param poiId int
	 * @param status String
	 * @param imagePath String
	 */
	public PoiImage(int poiId , String status, String imagePath) {
		this.poiId = poiId;
		this.status = status;
		this.imagePath = imagePath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPoiId() {
		return poiId;
	}

	public void setPoiId(int poiId) {
		this.poiId = poiId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
}
