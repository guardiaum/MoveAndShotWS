package com.johnymoreira.maspois.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.postgis.Geometry;
import org.postgis.Polygon;

@XmlRootElement
public class Poi {
	private int id;
	private String name;
	private String type;
	private Polygon shotAreaPolygon;
	private Geometry shotImagesArea;
	private Double latitude;
	private Double longitude;
	private String imageAddress;

	public Poi() {
	}
	
	public Poi(String name, String type, Double latitude, Double longitude) {
		this.name = name;
		this.type = type;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Poi(int id, String name, String type, Polygon polygon) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.shotAreaPolygon = polygon;
	}
	
	public Poi(int id, String name, String type, Polygon shotAreaPolygon,
			Geometry shotImagesArea, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.shotAreaPolygon = shotAreaPolygon;
		this.shotImagesArea = shotImagesArea;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	@XmlTransient
	public Polygon getShotAreaPolygon() {
		return shotAreaPolygon;
	}
	
	public String getShotAreaPolygonSt() {
		if (shotAreaPolygon != null) {
			return shotAreaPolygon.toString();
		} 
		return null;
	}
	
	public void setShotAreaPolygon(Polygon polygon) {
		this.shotAreaPolygon = polygon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getShotImagesAreaSt(){
		if (shotImagesArea!= null) {
			return shotImagesArea.toString();
		}
		return null;
	}
	
	@XmlTransient
	public Geometry getShotImagesArea() {
		return shotImagesArea;
	}

	public void setShotImagesArea(Geometry shotImagesArea) {
		this.shotImagesArea = shotImagesArea;
	}

	public String getImageAddress() {
		return imageAddress;
	}

	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}
	
}
