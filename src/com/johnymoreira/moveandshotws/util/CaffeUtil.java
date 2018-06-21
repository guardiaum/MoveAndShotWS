/**
 * 
 */
package com.johnymoreira.moveandshotws.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;

import com.johnymoreira.moveandshotws.dao.PoiImageDAO;
import com.johnymoreira.moveandshotws.dao.PredictionDAO;
import com.johnymoreira.moveandshotws.dao.PredictionResultDAO;
import com.johnymoreira.moveandshotws.pojo.PoiImage;
import com.johnymoreira.moveandshotws.pojo.Prediction;
import com.johnymoreira.moveandshotws.pojo.PredictionResult;

/**
 * @author Thiago Leonel
 * 
 * Class for shot calls through the bash using python to run the pycaffe scripts.
 *
 */
public class CaffeUtil {
	
	public static void predict(PoiImage poiImage, String image_path) {
		 String path = createTmpTxt(poiImage);
		ProcessBuilder pb = new ProcessBuilder("python", "/home/thiago/TCC/bin/HybridIdentifier.py", image_path).redirectError(new File("/home/thiago/tcc-output2.txt")).redirectOutput(new File(path));
	     try {
	    	 int retVal = pb.start().waitFor();
	    	 if(retVal == 0) {	
	    		poiImage.setStatus("done");
	    		PoiImageDAO.updatePoiImage(poiImage);
	    		Prediction prediction = new Prediction();
	    		prediction.setPoiImageId(poiImage.getId());
	    		prediction.setTimestamp(new Timestamp(System.currentTimeMillis()));
	    		saveResults(path, prediction);
	    	 }else {
		    		poiImage.setStatus("failed");
		    	PoiImageDAO.updatePoiImage(poiImage); 
	    	 }
	    	 
	     }catch(Exception e)
	     {
	    	 e.printStackTrace(System.out);
	     }
	}
	
	private static String createTmpTxt(PoiImage poiImage) {
		String basePath = "/home/thiago/TCC/tmp/";
		String folder = poiImage.getId()+System.currentTimeMillis()+"";
		String path = basePath+folder+"/tmp.txt";
		File directory = new File(basePath+folder);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		
		File file = new File(path);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return path;
	}
	
	private static void saveResults(String tmpFilePath,  Prediction prediction) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(tmpFilePath));
			String results = reader.readLine();
			String executionTime = reader.readLine();
			prediction.setExecution_time(Double.parseDouble(executionTime));
			int id = PredictionDAO.storePrediction(prediction);
			for(String result : results.split(",")) {
				PredictionResult predictionResult = new PredictionResult();
				predictionResult.setPrediction_id(id);
				predictionResult.setResult(result);
				predictionResult.setStatus("rejected");
				PredictionResultDAO.storePredictionResult(predictionResult);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
