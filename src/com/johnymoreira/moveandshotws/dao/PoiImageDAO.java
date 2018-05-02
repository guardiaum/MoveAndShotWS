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

import com.johnymoreira.moveandshotws.pojo.LatLng;
import com.johnymoreira.moveandshotws.pojo.PoiImage;

/**
 * Data Access Object
 * 
 * @author Johny Moreira
 *
 */
public class PoiImageDAO {

	/**
	 * Armazena foto do ponto de interesse
	 * @param poiImage {@link PoiImage}
	 * @return	id da foto caso cadastro tenha sido concluído. 0 caso negativo.
	 */
	public static int storePoiImage(PoiImage poiImage) {
		int id = 0;
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();

			try {

				Statement s = c.createStatement();

				try {
					String sql = "INSERT INTO "
							+ "poi_image_upload (poi_id, status, image_path) "
							+ "VALUES ('"+poiImage.getPoiId()+ "', '"+ poiImage.getStatus()
							+ "','"+poiImage.getImagePath()+ "') returning id;";

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

	/**
	 * Remove foto do ponto de interesse
	 * @param id String identificador do foto do ponto de interesse
	 */
	public static void removePoiImage(String id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					s.executeUpdate("DELETE FROM poi_image_upload WHERE id=" + id);
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

	/**
	 * Obtem a lista de pontos de interesse cadastrados
	 * 
	 * @return List<PoiImage> a lista de pontos
	 */
	public static List<PoiImage> getPoiImages() {
		List<PoiImage> poisImages = new ArrayList<PoiImage>();
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					String sql = "SELECT id, poi_id, status, image_path FROM poi_image_upload";
					
					ResultSet rs = s.executeQuery(sql);

					while (rs.next()) {
						PoiImage p = new PoiImage();
						p.setId(rs.getInt("id"));
						p.setPoiId(rs.getInt("poi_id"));
						p.setStatus(rs.getString("status"));
						p.setImagePath(rs.getString("image_path"));
						poisImages.add(p);
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
		return poisImages;
	}

	/**
	 * Obtem uma foto do ponto de interesse
	 * 
	 * @param id int identificador do ponto
	 * @return {@link PoiImage}
	 */
	public static PoiImage getPoiImage(int id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();
				try {
					ResultSet rs = s
							.executeQuery("SELECT id, poi_id, status, image_path"
									+ " FROM poi_image_upload"
									+ " WHERE id="+ id);

					rs.next();
					PoiImage p = new PoiImage();	
					p.setId(rs.getInt("id"));
					p.setPoiId(rs.getInt("poi_id"));
					p.setStatus(rs.getString("status"));
					p.setImagePath(rs.getString("image_path"));
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
	
	/**
	 * Atualiza informações da foto do ponto de interesse
	 * 
	 * @param poiImage {@link PoiImage} foto do ponto de interesse com dados a serem atualizados
	 */
	public static void updatePoiImage(PoiImage poiImage) {
		try {
			Class.forName("org.postgresql.Driver");
            Connection c = ConnectionUtil.createConnection();
            try{
            	Statement s = c.createStatement();
            	try{
		            s.clearBatch();
		            	String sql = " UPDATE poi_image_upload "
		            			+ "SET poi_id = VALOR, status = VALOR, image_path = VALOR "
		            			+ " WHERE id = VALOR";
		            	sql = sql.replaceFirst("VALOR", String.valueOf(poiImage.getPoiId()));
		            	sql = sql.replaceFirst("VALOR", "'"+poiImage.getStatus()+"'");
		            	sql = sql.replaceFirst("VALOR", "'"+poiImage.getImagePath()+"'");
		            	sql = sql.replaceFirst("VALOR", String.valueOf(poiImage.getId()));
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
        	System.out.println(e.getNextException());
        } 
	}

}
