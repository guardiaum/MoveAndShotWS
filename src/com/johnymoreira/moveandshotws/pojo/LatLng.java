package com.johnymoreira.moveandshotws.pojo;

/**
 * POJO de definição de coordenadas
 * 
 * @author Johny Moreira
 *
 */
public class LatLng {
	private Double latitude;
	private Double longitude;
	
	/**
	 * Construtor sobrecarregado
	 * 
	 * @param latitude Double
	 * @param longitude Double
	 */
	public LatLng(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
}
