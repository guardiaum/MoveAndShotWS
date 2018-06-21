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
import com.johnymoreira.moveandshotws.pojo.Prediction;

/**
 * Data Access Object
 * 
 * @author Johny Moreira
 *
 */
public class PredictionDAO {

	/**
	 * Armazena ponto de interesse
	 * @param prediction {@link Prediction}
	 * @return	id do ponto caso cadastro tenha sido concluído. 0 caso negativo.
	 */
	public static int storePrediction(Prediction prediction) {
		int id = 0;
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();

			try {

				Statement s = c.createStatement();

				try {
					String sql = "INSERT INTO "
							+ "prediction (poi_image_id, timestamp, execution_time) "
							+ "VALUES ('"+prediction.getPoiImageId()+ "', '"+ prediction.getTimestamp()
							+ "', '"+prediction.getExecution_time()+ "') returning id;";

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
	 * Remove ponto de interesse
	 * @param id String identificador do ponto de interesse
	 */
	public static void removePrediction(String id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					s.executeUpdate("DELETE FROM prediction WHERE id=" + id);
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
	 * @return List<Prediction> a lista de pontos
	 */
	public static List<Prediction> getPredictions() {
		List<Prediction> predictions = new ArrayList<Prediction>();
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					String sql = "SELECT id, poi_image_id, timestamp, execution_time FROM prediction";
					
					ResultSet rs = s.executeQuery(sql);

					while (rs.next()) {
						Prediction p = new Prediction();
						p.setId(rs.getInt("id"));
						p.setPoiImageId(rs.getInt("poi_image_id"));
						p.setTimestamp(rs.getTimestamp("timestamp"));
						p.setExecution_time(rs.getDouble("execution_time"));
						predictions.add(p);
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
		return predictions;
	}

	/**
	 * Obtem um ponto de interesse
	 * 
	 * @param id int identificador do ponto
	 * @return {@link Prediction}
	 */
	public static Prediction getPrediction(int id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();
				try {
					ResultSet rs = s
							.executeQuery("SELECT id, poi_image_id, timestamp, execution_time FROM prediction WHERE id="+ id);

					rs.next();
					Prediction p = new Prediction();	
					p.setId(rs.getInt("id"));
					p.setPoiImageId(rs.getInt("poi_image_id"));
					p.setTimestamp(rs.getTimestamp("timestamp"));
					p.setExecution_time(rs.getDouble("execution_time"));
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
	 * Atualiza informações do ponto de interesse
	 * 
	 * @param prediction {@link Prediction} ponto de interesse com dados a serem atualizados
	 */
	public static void update(Prediction prediction) {
		try {
			Class.forName("org.postgresql.Driver");
            Connection c = ConnectionUtil.createConnection();
            try{
            	Statement s = c.createStatement();
            	try{
		            s.clearBatch();
		            	String sql = " UPDATE prediction SET poi_image_id = VALOR, timestamp = VALOR, execution_time = VALOR"
		            			+ " WHERE id = VALOR";
		            	sql = sql.replaceFirst("VALOR", "'"+prediction.getPoiImageId()+"'");
		            	sql = sql.replaceFirst("VALOR", "'"+prediction.getTimestamp()+"'");
		            	sql = sql.replaceFirst("VALOR", String.valueOf(prediction.getExecution_time()));
		            	sql = sql.replaceFirst("VALOR", String.valueOf(prediction.getId()));
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
