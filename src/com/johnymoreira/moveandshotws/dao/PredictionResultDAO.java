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
import com.johnymoreira.moveandshotws.pojo.PredictionResult;

/**
 * Data Access Object
 * 
 * @author Johny Moreira
 *
 */
public class PredictionResultDAO {

	/**
	 * Armazena ponto de interesse
	 * @param predictionResult {@link PredictionResult}
	 * @return	id do ponto caso cadastro tenha sido concluído. 0 caso negativo.
	 */
	public static int storePredictionResult(PredictionResult predictionResult) {
		int id = 0;
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();

			try {

				Statement s = c.createStatement();

				try {
					String sql = "INSERT INTO "
							+ "prediction_result (prediction_id, result, status) "
							+ "VALUES ('"+predictionResult.getPrediction_id()+ "', '"+ predictionResult.getResult()
							+ "', '"+predictionResult.getStatus()+ "') returning id;";

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
	public static void removePredictionResult(String id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					s.executeUpdate("DELETE FROM prediction_result WHERE id=" + id);
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
	 * @return List<PredictionResult> a lista de pontos
	 */
	public static List<PredictionResult> getPredictionResults() {
		List<PredictionResult> predictionResults = new ArrayList<PredictionResult>();
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					String sql = "SELECT * FROM prediction_result";
					
					ResultSet rs = s.executeQuery(sql);

					while (rs.next()) {
						PredictionResult p = new PredictionResult();	
						p.setId(rs.getInt("id"));
						p.setPrediction_id(rs.getInt("prediction_id"));
						p.setResult(rs.getString("result"));
						p.setStatus(rs.getString("status"));
						predictionResults.add(p);
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
		return predictionResults;
	}

	/**
	 * Obtem um ponto de interesse
	 * 
	 * @param id int identificador do ponto
	 * @return {@link PredictionResult}
	 */
	public static PredictionResult getPredictionResult(int id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();
				try {
					ResultSet rs = s
							.executeQuery("SELECT * FROM prediction_result WHERE id="+ id);

					rs.next();
					PredictionResult p = new PredictionResult();	
					p.setId(rs.getInt("id"));
					p.setPrediction_id(rs.getInt("prediction_id"));
					p.setResult(rs.getString("result"));
					p.setStatus(rs.getString("status"));
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
	 * @param predictionResult {@link PredictionResult} ponto de interesse com dados a serem atualizados
	 */
	public static void update(PredictionResult predictionResult) {
		try {
			Class.forName("org.postgresql.Driver");
            Connection c = ConnectionUtil.createConnection();
            try{
            	Statement s = c.createStatement();
            	try{
		            s.clearBatch();
		            	String sql = " UPDATE predictionResult SET prediction_id = VALOR, result = VALOR, status = VALOR"
		            			+ " WHERE id = VALOR";
		            	sql = sql.replaceFirst("VALOR", "'"+predictionResult.getPrediction_id()+"'");
		            	sql = sql.replaceFirst("VALOR", "'"+predictionResult.getResult()+"'");
		            	sql = sql.replaceFirst("VALOR", "'"+predictionResult.getResult()+"'");
		            	sql = sql.replaceFirst("VALOR", String.valueOf(predictionResult.getId()));
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
