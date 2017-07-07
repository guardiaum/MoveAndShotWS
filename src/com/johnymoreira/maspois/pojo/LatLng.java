package com.johnymoreira.maspois.pojo;

public class LatLng {
	private Double latitude;
	private Double longitude;
	
	public LatLng() {
		super();
	}
	
	public LatLng(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
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
}
