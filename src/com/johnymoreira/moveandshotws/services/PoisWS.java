package com.johnymoreira.moveandshotws.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.postgis.Polygon;

import com.johnymoreira.moveandshotws.facade.MoveAndShotFacade;
import com.johnymoreira.moveandshotws.pojo.LatLng;
import com.johnymoreira.moveandshotws.pojo.Poi;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/pois")
public class PoisWS {
	
	private static final String REPOSITORY_PATH = "fotos";
	
	@Context
	private ServletContext wsc;
	
	@POST
	@Path("/send")
	@Consumes(MediaType.APPLICATION_JSON)
	public int sendPois(@FormParam("name") String name, @FormParam("type") String type,
			@FormParam("latitude") Double latitude, @FormParam("longitude") Double longitude
			) {
		//System.out.println("POI: "+name);
		Poi poi = new Poi(name, type, latitude, longitude);
		int id = MoveAndShotFacade.storePoi(poi);
		return id;
	}

	@GET
	@Path("/getPois")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Poi> getPois() {
		List<Poi> pois = MoveAndShotFacade.getPois();
		//System.out.println("pois.length:" + pois.size());
		return pois;
	}

	@POST
	@Path("/delete_poi")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removePoi(@FormParam("poi_id") String id) {
		MoveAndShotFacade.removePoi(id);
	}

	@GET
	@Path("/getPoiById")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Poi getPoiById(@QueryParam("poi_id") int id) {
		return MoveAndShotFacade.getPoi(id);
	}
	
	@POST
	@Path("/addShotArea")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addShotArea(@QueryParam("poi_id")int id, @QueryParam("area")String area){
		Poi poi = MoveAndShotFacade.getPoi(id);
		
		area = "POLYGON " + (area.replace(", ", " ")).replace("),(", ",");
		Polygon p = null;
		try {
			p = new Polygon(area);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		poi.setShotAreaPolygon(p);
		//System.out.println("Polygon: " + p.toString().replace("),(", ","));
		
		MoveAndShotFacade.update(poi);
	}
	
	@POST
	@Path("/sendImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response sendImage(@FormDataParam("file")InputStream uploadedInputStream,
			@FormDataParam("file")FormDataContentDisposition fileDetails,
			 @FormDataParam("poi_id") int poiId){
		/*System.out.println("POI: "+ poiId);
		System.out.println("File Details: "+fileDetails.toString());
		System.out.println("Uploaded Input Stream: "+uploadedInputStream.toString());*/
		try{
			Poi poi = MoveAndShotFacade.getPoi(poiId);
			
			String contextPath = wsc.getRealPath("");		
			
			File f = new File(contextPath+System.getProperty("file.separator")+REPOSITORY_PATH + System.getProperty("file.separator")+ poiId);
			
			if (!f.exists()) {
				f.mkdirs();
			}
			
			String uploadedFileLocation = contextPath+System.getProperty("file.separator")+REPOSITORY_PATH + System.getProperty("file.separator")+ poiId + System.getProperty("file.separator") + fileDetails.getFileName();
			String relativePath = REPOSITORY_PATH + System.getProperty("file.separator")+ poiId + System.getProperty("file.separator") + fileDetails.getFileName();
			poi.setImageAddress(relativePath);
			MoveAndShotFacade.update(poi);
	
			saveToFile(uploadedInputStream, uploadedFileLocation);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return Response.ok().build();
	}
	
	@GET
	@Path("/getArea")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<LatLng> getArea(@QueryParam("poi_id")int id){
		Poi poi = MoveAndShotFacade.getPoi(id);
		if(poi.getShotAreaPolygonSt()!=null)
			return getPontos(poi.getShotAreaPolygonSt());
		return null;
	}
	
	
	private ArrayList<LatLng> getPontos(String polygon) {
		polygon = polygon.substring(9, polygon.length() - 2);
		ArrayList<LatLng> points = new ArrayList<LatLng>();
		int i = 0;
		int pos = polygon.indexOf(',');
		while (pos > 0) {
			String nextPoints = polygon.substring(0, pos);
			LatLng iPoint = getPoint(nextPoints);
			points.add(iPoint);
			i++;
			polygon = polygon.substring(pos + 1, polygon.length());
			pos = polygon.indexOf(',');
		}
		String lastPoint = polygon;
		LatLng lastPointLatLong = getPoint(lastPoint);
		points.add(lastPointLatLong);
		return points;
	}

	private LatLng getPoint(String point) {
		int pos = point.indexOf(' ');
		Double x = Double.parseDouble(point.substring(0, pos));
		Double y = Double.parseDouble(point.substring(pos + 1));
		return new LatLng(x, y);
	}

	private void saveToFile(InputStream inputStream, String uploadedFileLocation) {
		try{
			OutputStream out = null;
			int read = 0;
			byte[] bytes = new byte[1024];
			
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = inputStream.read(bytes)) != -1){
				out.write(bytes, 0, read);
			}
			out.flush();
            out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public ServletContext getWsc() {
		return wsc;
	}

	public void setWsc(ServletContext wsc) {
		this.wsc = wsc;
	}
}
