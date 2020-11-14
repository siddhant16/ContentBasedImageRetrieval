import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_32F;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvMinMaxLoc;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_core.cvZero;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_TM_CCORR_NORMED;
import static org.bytedeco.javacpp.opencv_imgproc.cvMatchTemplate;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;


public class textureBased {
	static String matched;
	public static List<String> processImage(String imagePath, String deployPath) {
		
		
		Gabor g = new Gabor();
		
		try
		{
		 File dir = new File(deployPath+"/textureData");
		 File[] files = dir.listFiles();
		 List<String> imglist = new ArrayList<String>();

		 double[] val = new double[files.length];
		 
		 for (int i = 0; i < files.length; i++) {

			//IplImage dbImg = cvLoadImage(files[i].getAbsolutePath(), 0);
			//IplImage tmp = cvLoadImage("C:/Users/Acer/BE/Project/sift1/SIFT/src/superman.png", 0);
			IplImage src = cvLoadImage(imagePath, 0);
			//System.out.println("1->(SERVER?)"+files[i].getAbsolutePath());
			Mat myImg1 = g.preprocessImage(files[i].getAbsolutePath());
			double [] features1 = new double[10];
			features1 = g.enhanceImg(myImg1);
		
		    double[] features = g.input_image(imagePath);

		    double diff = g.calDist(features, features1);
		    
		    val[i] = diff;
		}

		HashMap<String, Double> h_hmap = new HashMap<String, Double>();
		//System.out.println("1->(chk equal)"+files[i].getName());
		for(int i=0;i<files.length;i++){
			h_hmap.put(files[i].getName(), val[i]);
			//System.out.println("IMGNAME"+files[i].getName()+" : "+ val[i]);
		}
			
			
		Map<Integer, String> map = g.sortByValues(h_hmap);
		
		Set set = map.entrySet();
	    Iterator iterator = set.iterator();
	    while(iterator.hasNext()) {
	    	MatchRecord record = new MatchRecord();
	    	Map.Entry me = (Map.Entry)iterator.next();
	    	//System.out.println(me.getKey().toString());
	    	record.setName(me.getKey().toString());
	    	//System.out.println(me.getValue().toString());
			imglist.add(record.getName());
			record.setImage(cvLoadImage(me.getValue().toString(), 0));
	    	//imglist.add(me.getKey().toString());
	        
	    }
		
		//imglist.add(files[i].getName());
		
		
		return imglist;
		}
		catch(Exception e)
		{
			System.out.println("not working");
			e.printStackTrace();
			//System.exit(0);
		}
		return null;
	}

}
