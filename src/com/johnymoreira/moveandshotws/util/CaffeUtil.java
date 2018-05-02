/**
 * 
 */
package com.johnymoreira.moveandshotws.util;

import java.io.File;

import com.johnymoreira.moveandshotws.dao.PoiImageDAO;
import com.johnymoreira.moveandshotws.pojo.PoiImage;

/**
 * @author Thiago Leonel
 * 
 * Class for shot calls through the bash using python to run the pycaffe scripts.
 *
 */
public class CaffeUtil {
	
	public static void predict(PoiImage poiImage, String image_path) {
		ProcessBuilder pb = new ProcessBuilder("python", "/home/thiago/TCC/bin/HybridIdentifier.py", image_path).redirectError(new File("/home/thiago/tcc-output2.txt")).redirectOutput(new File("/home/thiago/tcc-output2.txt"));
	     try {
	    	 int retVal = pb.start().waitFor();
	    	 if(retVal == 0) {	
	    		poiImage.setStatus("done");
	    		PoiImageDAO.updatePoiImage(poiImage);
	    	 }else {
		    		poiImage.setStatus("failed");
		    	PoiImageDAO.updatePoiImage(poiImage); 
	    	 }
	    	 
	     }catch(Exception e)
	     {
	    	 e.printStackTrace(System.out);
	     }
	}

}
