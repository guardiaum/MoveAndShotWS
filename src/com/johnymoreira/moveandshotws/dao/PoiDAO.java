package com.johnymoreira.moveandshotws.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.postgis.Geometry;
import org.postgis.MultiPolygon;
import org.postgis.Polygon;

import com.johnymoreira.moveandshotws.pojo.Poi;

public class PoiDAO {

	public static int storePoi(Poi poi) {
		int id = 0;
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();

			try {

				Statement s = c.createStatement();

				try {
					String sql = "INSERT INTO "
							+ "poi (poi_name, poi_type, latitude, longitude) "
							+ "VALUES ('"+poi.getName()+ "', '"+ poi.getType()
							+ "',"+poi.getLatitude()+", "+poi.getLongitude()+ ") returning id;";
					/*
					 * ,'"+ poi.getShotAreaPolygon().toString().replace("),(", ",") + ")
					 * */
					//System.out.println(sql);

					ResultSet rs = s.executeQuery(sql);
					rs.next();
					id = rs.getInt("id");
					rs.close();
				} finally {
					s.close();
				}
			} finally {
				c.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return id;
	}

	public static void removePoi(String id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					s.executeUpdate("DELETE FROM poi WHERE id=" + id);
				} finally {
					s.close();
				}
			} finally {
				c.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static List<Poi> getPois() {
		List<Poi> pois = new ArrayList<Poi>();
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					String sql = "SELECT id, poi_name, poi_type, "
							+ "ST_AsText(poi_shot_area_polygon) as"
							+ " poi_shot_area_polygon, latitude, "
							+ "longitude, image FROM poi";
					
					ResultSet rs = s.executeQuery(sql);

					while (rs.next()) {
						Poi p = new Poi();
						p.setId(rs.getInt("id"));
						p.setName(rs.getString("poi_name"));
						p.setType(rs.getString("poi_type"));
						String theGeomSt = rs.getString("poi_shot_area_polygon");
						if (theGeomSt != null) {
							try {
								Geometry theGeom = null;
								if (theGeomSt.toUpperCase().contains(
										"MULTIPOLYGON")) {
									theGeom = new MultiPolygon(theGeomSt);
								} else if (theGeomSt.toUpperCase().contains(
										"POLYGON")) {
									theGeom = new Polygon(theGeomSt);
								}
								p.setShotImagesArea(theGeom);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						//System.out.println(p.getShotImagesArea());
						p.setLatitude(rs.getDouble("latitude"));
						p.setLongitude(rs.getDouble("longitude"));
						p.setImageAddress(rs.getString("image"));
						pois.add(p);
					}
				} finally {
					s.close();
				}
			} finally {
				c.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pois;
	}

	public static Poi getPoi(int id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();
				try {
					ResultSet rs = s
							.executeQuery("SELECT id, poi_name, poi_type, "
									+ "ST_AsText(poi_shot_area_polygon) "
									+ "as poi_shot_area_polygon, "
									+ "latitude, longitude, image "
									+ "FROM poi WHERE id="+ id);

					rs.next();
					Poi p = new Poi();
					p.setId(rs.getInt("id"));
					p.setName(rs.getString("poi_name"));
					p.setType(rs.getString("poi_type"));
					if(rs.getString("poi_shot_area_polygon")!=null)
						p.setShotAreaPolygon(new Polygon(rs.getString("poi_shot_area_polygon")));
					p.setLatitude(rs.getDouble("latitude"));
					p.setLongitude(rs.getDouble("longitude"));
					p.setImageAddress(rs.getString("image"));
					return p;
				} finally {
					s.close();
				}
			} finally {
				c.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void update(Poi poi) {
		try {
			Class.forName("org.postgresql.Driver");
            Connection c = ConnectionUtil.createConnection();
            try{
            	Statement s = c.createStatement();
            	try{
		            s.clearBatch();
		            	String sql = " UPDATE poi SET poi_name = VALOR, poi_type = VALOR, latitude = VALOR,"
		            			+ "  longitude = VALOR, poi_shot_area_polygon = VALOR, image = VALOR"
		            			+ " WHERE id = VALOR";
		            	sql = sql.replaceFirst("VALOR", "'"+poi.getName()+"'");
		            	sql = sql.replaceFirst("VALOR", "'"+poi.getType()+"'");
		            	sql = sql.replaceFirst("VALOR", String.valueOf(poi.getLatitude()));
		            	sql = sql.replaceFirst("VALOR", String.valueOf(poi.getLongitude()));
		            	if(poi.getShotAreaPolygon()!=null)
		            		sql = sql.replaceFirst("VALOR","'"+poi.getShotAreaPolygon().toString().replace("),(", ",")+"'");
		            	else
		            		sql = sql.replaceFirst("VALOR", "null");
		            	sql = sql.replaceFirst("VALOR", "'"+poi.getImageAddress()+"'");
		            	sql = sql.replaceFirst("VALOR", String.valueOf(poi.getId()));
		            	s.addBatch(sql);
		            	
		            s.executeBatch();
            	
            	}finally{
            		s.close();
            	}
            }finally{
            	c.close();
            }
        } catch (ClassNotFoundException e) {
        	System.out.println(e.getLocalizedMessage());
        } catch (SQLException e) {
        	System.out.println(e.getLocalizedMessage());
        } 
	}

}
