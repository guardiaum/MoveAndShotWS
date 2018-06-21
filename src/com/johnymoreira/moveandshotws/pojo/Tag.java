package com.johnymoreira.moveandshotws.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.postgis.Polygon;

/**
 * POJO para definição de Tag 
 * 
 * @author Thiago Leonel
 */
@XmlRootElement
public class Tag {
	private int id;
	private int poiImageId;
	private String word;
	private int position;
	
	public Tag() {}
	

	public Tag(int poiImageId, String word, int position) {
		super();
		this.id = id;
		this.poiImageId = poiImageId;
		this.word = word;
		this.position = position;
	}
	
	
	
	public Tag(int id, int poiImageId, String word, int position) {
		super();
		this.id = id;
		this.poiImageId = poiImageId;
		this.word = word;
		this.position = position;
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
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}

	
	
	
	
}
