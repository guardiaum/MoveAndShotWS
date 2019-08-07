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
 *         Class for shot calls through the bash using python to run the pycaffe
 *         scripts.
 *
 */

public class CaffeUtil {

	String PYTHON_SCRIPT_PATH = "/home/thiago/TCC/bin/HybridIdentifier.py" //Insert here the path for the CNN script
	String OUTPUT_LOG = "/home/thiago/tcc-output2.txt" //Insert here the path for the log file
	String TMP_FOLDER = "/home/thiago/TCC/tmp/" // Insert here a path for a tmp folder where results will be written

	public static String predict(PoiImage poiImage, String image_path) {
		String path = createTmpTxt(poiImage);
		ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT_PATH, image_path)
				.redirectError(new File(OUTPUT_LOG)).redirectOutput(new File(path));
		try {
			int retVal = pb.start().waitFor();
			if (retVal == 0) {
				poiImage.setStatus("done");
				PoiImageDAO.updatePoiImage(poiImage);
				Prediction prediction = new Prediction();
				prediction.setPoiImageId(poiImage.getId());
				prediction.setTimestamp(new Timestamp(System.currentTimeMillis()));
				return saveResults(path, prediction);
			} else {
				poiImage.setStatus("failed");
				PoiImageDAO.updatePoiImage(poiImage);
				return "failed";
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);

		}
		return "failed";
	}

	private static String createTmpTxt(PoiImage poiImage) {
		String basePath = TMP_FOLDER;
		String folder = poiImage.getId() + System.currentTimeMillis() + "";
		String path = basePath + folder + "/tmp.txt";
		File directory = new File(basePath + folder);
		if (!directory.exists()) {
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

	private static String saveResults(String tmpFilePath, Prediction prediction) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(tmpFilePath));
			String results = reader.readLine();
			String executionTime = reader.readLine();
			prediction.setExecution_time(Double.parseDouble(executionTime));
			int id = PredictionDAO.storePrediction(prediction);

			for (String result : results.split(",")) {
				PredictionResult predictionResult = new PredictionResult();
				predictionResult.setPrediction_id(id);
				predictionResult.setResult(result);
				predictionResult.setStatus("rejected");
				PredictionResultDAO.storePredictionResult(predictionResult);
			}
			return results;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			reader.close();
		}

		return "failed";
	}

}
