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
import com.johnymoreira.moveandshotws.pojo.Tag;

/**
 * Data Access Object
 * 
 * @author Thiago Leonel
 *
 */
public class TagDAO {

	/**
	 * Armazena tag da foto
	 * @param tag {@link Tag}
	 * @return	id da tag caso cadastro tenha sido concluído. 0 caso negativo.
	 */
	public static int storeTag(Tag tag) {
		int id = 0;
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();

			try {

				Statement s = c.createStatement();

				try {
					String sql = "INSERT INTO "
							+ "tags (poi_image_id, word, position) "
							+ "VALUES ('"+tag.getPoiImageId()+ "', '"+ tag.getWord()
							+ "','"+tag.getPosition()+ "') returning id;";

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
	 * Remove tag da foto
	 * @param id String identificador do tag da foto
	 */
	public static void removeTag(String id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					s.executeUpdate("DELETE FROM tags WHERE id=" + id);
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
	 * @return List<Tag> a lista de pontos
	 */
	public static List<Tag> getTags() {
		List<Tag> tags = new ArrayList<Tag>();
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();

				try {
					String sql = "SELECT id, poi_image_id, word, position FROM tags";
					
					ResultSet rs = s.executeQuery(sql);

					while (rs.next()) {
						Tag p = new Tag();
						p.setId(rs.getInt("id"));
						p.setPoiImageId(rs.getInt("poi_image_id"));
						p.setWord(rs.getString("word"));
						p.setPosition(rs.getInt("position"));
						tags.add(p);
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
		return tags;
	}

	/**
	 * Obtem uma tag da foto
	 * 
	 * @param id int identificador do ponto
	 * @return {@link Tag}
	 */
	public static Tag getTag(int id) {
		try {
			Class.forName("org.postgresql.Driver");

			Connection c = ConnectionUtil.createConnection();
			try {
				Statement s = c.createStatement();
				try {
					ResultSet rs = s
							.executeQuery("SELECT id,poi_image_id, word, position"
									+ " FROM tags"
									+ " WHERE id="+ id);

					rs.next();
					Tag p = new Tag();	
					p.setId(rs.getInt("id"));
					p.setPoiImageId(rs.getInt("poi_image_id"));
					p.setWord(rs.getString("word"));
					p.setPosition(rs.getInt("position"));
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
	 * Atualiza informações da tag da foto
	 * 
	 * @param tag {@link Tag} tag da foto com dados a serem atualizados
	 */
	public static void updateTag(Tag tag) {
		try {
			Class.forName("org.postgresql.Driver");
            Connection c = ConnectionUtil.createConnection();
            try{
            	Statement s = c.createStatement();
            	try{
		            s.clearBatch();
		            	String sql = " UPDATE tags "
		            			+ "SET poi_image_id = VALOR, word = VALOR, position = VALOR "
		            			+ " WHERE id = VALOR";
		            	sql = sql.replaceFirst("VALOR", String.valueOf(tag.getPoiImageId()));
		            	sql = sql.replaceFirst("VALOR", "'"+tag.getWord()+"'");
		            	sql = sql.replaceFirst("VALOR", String.valueOf(tag.getPosition()));
		            	sql = sql.replaceFirst("VALOR", String.valueOf(tag.getId()));
		            	
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
