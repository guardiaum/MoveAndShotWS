package com.johnymoreira.moveandshotws.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.johnymoreira.moveandshotws.dao.PoiDAO;
import com.johnymoreira.moveandshotws.pojo.Poi;
import com.johnymoreira.moveandshotws.util.Constants;

/**
 * Fachada para comunicação entre as camadas Services e Dao
 * 
 * @author Johny Moreira
 *
 */
public class MoveAndShotFacade implements ServletContextListener{
	
	static ClassLoader classLoader = MoveAndShotFacade.class.getClassLoader();
	private static String contextPath;
	private static Properties properties = null;
	
	// Obtem o contexto da aplicação
	public static String getContextPath(){
		return contextPath;
	}
	
	/**
	 * Carrega o setup de propriedades para conexão com o banco de dados
	 * @return
	 */
	public static Properties loadSetup(){
		if (properties==null) {
			properties = new Properties();
		
			String pathname = contextPath+System.getProperty("file.separator")+Constants.DATABASE_INI_FILE;
			if (contextPath == null){
				pathname = Constants.DATABASE_INI_FILE;
			}
			
			File f = new File(pathname);
			if (f.exists()) {
				try {
					FileInputStream inStream = new FileInputStream(pathname);
					properties.load(inStream);
					inStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
	//Destroi contexto da aplicação
	@Override
	public void contextDestroyed(ServletContextEvent ctx) {}

	//captura contexto até diretório WEB-INF
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		contextPath = contextEvent.getServletContext().getRealPath("")+System.getProperty("file.separator")+"WEB-INF";
	}

	/**
	 * Armazena ponto de interesse
	 * @param poi {@link Poi}
	 * @return	id do ponto caso cadastro tenha sido concluído. 0 caso negativo.
	 */
	public static int storePoi(Poi poi) {
		return PoiDAO.storePoi(poi);
	}
	
	/**
	 * Remove ponto de interesse
	 * @param id String identificador do ponto de interesse
	 */
	public static void removePoi(String id) {
		PoiDAO.removePoi(id);
	}

	/**
	 * Obtem a lista de pontos de interesse cadastrados
	 * 
	 * @return List<Poi> a lista de pontos
	 */
	public static List<Poi> getPois() {
		return PoiDAO.getPois();
	}

	/**
	 * Obtem um ponto de interesse
	 * 
	 * @param id int identificador do ponto
	 * @return {@link Poi}
	 */
	public static Poi getPoi(int id) {
		return PoiDAO.getPoi(id);
	}
	
	/**
	 * Atualiza informações do ponto de interesse
	 * 
	 * @param poi {@link Poi} ponto de interesse com dados a serem atualizados
	 */
	public static void update(Poi poi){
		PoiDAO.update(poi);
	}
}
