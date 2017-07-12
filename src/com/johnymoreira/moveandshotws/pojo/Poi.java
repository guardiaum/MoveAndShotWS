package com.johnymoreira.moveandshotws.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.postgis.Polygon;

/**
 * POJO para definição de POI (Point of Interest)
 * 
 * @author Johny Moreira
 *
 */
@XmlRootElement
public class Poi {
	private int id;
	private String name;
	private String type;
	private Polygon shotAreaPolygon;
	private LatLng poiCoordinate;
	private String imageAddress;

	public Poi() {}

	/**
	 * Construtor sobrecarregado de ponto de interesse.
	 * 
	 * @param name String
	 * @param type String
	 * @param poiCoordinate {@link LatLng}
	 */
	public Poi(String name, String type, LatLng poiCoordinate) {
		this.name = name;
		this.type = type;
		this.poiCoordinate = poiCoordinate;
	}
	
	/**
	 * Construtor sobrecarregado de ponto de interesse e área para captura de fotografias
	 * 
	 * @param id int
	 * @param name String
	 * @param type String
	 * @param shotAreaPolygon {@link Polygon}
	 * @param poiCoordinate {@link LatLng}
	 * @param imageAddress String
	 */
	public Poi(int id, String name, String type, Polygon shotAreaPolygon,
			LatLng poiCoordinate, String imageAddress) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.shotAreaPolygon = shotAreaPolygon;
		this.poiCoordinate = poiCoordinate;
		this.imageAddress = imageAddress;
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

	public String getImageAddress() {
		return imageAddress;
	}

	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}

	public LatLng getPoiCoordinate() {
		return poiCoordinate;
	}

	public void setPoiCoordinate(LatLng poiCoordinate) {
		this.poiCoordinate = poiCoordinate;
	}
	
}
