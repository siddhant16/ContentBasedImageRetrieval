
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bytedeco.javacpp.opencv_core.IplImage;
 class Gabor {
	 
	 double[] calValue(Mat img1, Mat img2, Mat img3, Mat img4){
		 //mean and sd calculation
		 double [] textureVector = new double[8];
		 IplImage i1 = new IplImage(img1);
		 IplImage i2 = new IplImage(img2);
		 IplImage i3 = new IplImage(img3);
		 IplImage i4 = new IplImage(img4);

		 CvScalar mean = new CvScalar();
		 CvScalar sd = new CvScalar();
		 

		 cvAvgSdv(i1, mean, sd, null);
		 textureVector[0] = Math.floor(mean.getVal(0)*1e5)/1e5;
		 textureVector[1] = Math.floor(sd.getVal(0)*1e5)/1e5;
		 
		 cvAvgSdv(i2, mean, sd, null);
		 textureVector[2] = Math.floor(mean.getVal(0)*1e5)/1e5;
		 textureVector[3] = Math.floor(sd.getVal(0)*1e5)/1e5;
		 
		 cvAvgSdv(i3, mean, sd, null);
		 textureVector[4] = Math.floor(mean.getVal(0)*1e5)/1e5;
		 textureVector[5] = Math.floor(sd.getVal(0)*1e5)/1e5;
		 
		 cvAvgSdv(i4, mean, sd, null);
		 textureVector[6] = Math.floor(mean.getVal(0)*1e5)/1e5;
		 textureVector[7] = Math.floor(sd.getVal(0)*1e5)/1e5;
	 
		 return textureVector;
	 }
	 
	 //public Mat enhanceImg(Mat myImg){
	 double[] enhanceImg(Mat myImg){
		 
	    // prepare the output matrix for filters.
	    Mat gabor1 = new Mat (myImg.rows(),myImg.cols(), CV_8UC1 );
	    Mat gabor2 = new Mat (myImg.rows(),myImg.cols(), CV_8UC1 );
	    Mat gabor3 = new Mat (myImg.rows(),myImg.cols(), CV_8UC1);
	    Mat gabor4 = new Mat (myImg.rows(),myImg.cols(), CV_8UC1);

	    //predefine parameters for Gabor kernel 
	    Size kSize = new Size(31,31);

	    //Orientations of the normal to the parallel stripes of a Gabor function.
	    double theta1 = 0;
	    double theta2 = 45;
	    double theta3 = 90;
	    double theta4 = 135;

	    double lambda = 16/3	; 	//Wavelength of the sinusoidal factor
	    double sigma =  (5.09030 * 8.0) / (3.0 * CV_PI); 	//Standard deviation of the gaussian envelope
	    double gamma = 0.5890;   	//Spatial aspect ratio
	    double psi =  CV_PI/2;		//Phase offset.

	    // the filters kernel
	    Mat kernel1 = getGaborKernel(kSize, sigma, theta1, lambda, gamma , psi , CV_32F);
	    Mat kernel2 = getGaborKernel(kSize, sigma, theta2, lambda, gamma , psi , CV_32F);
	    Mat kernel3 = getGaborKernel(kSize, sigma, theta3, lambda, gamma , psi , CV_32F);
	    Mat kernel4 = getGaborKernel(kSize, sigma, theta4, lambda, gamma , psi , CV_32F);
	    
//	    imshow("ker", kernel2);
//	    cvWaitKey();
	    filter2D(myImg, gabor1, -1, kernel1);
	    filter2D(myImg, gabor2, -1, kernel2);
	    filter2D(myImg, gabor3, -1, kernel3);
	    filter2D(myImg, gabor4, -1, kernel4);
	
//	    imshow("Gabor1", kernel1);
//	    imshow("Gabor1", kernel2);
//	    imshow("Gabor1", kernel3);
//	    imshow("Gabor1", kernel4);
	   // waitKey(0);
	    double [] features = calValue(gabor1, gabor2, gabor3, gabor4);
	  
	    return features;	
	    
	}

	 Mat preprocessImage(String path){
		Size size = new Size(400,400);
		
		Mat myImg = imread(path,1); 
		
		resize(myImg, myImg, size);
		
		cvtColor(myImg, myImg,CV_BGR2GRAY);
		
		return myImg;
	}
	
	 double calDist(double[] vec1, double[] vec2){
		
		 for(int i=0;i<8;i++)
			 vec1[i] = Math.pow(vec1[i] - vec2[i], 2); 
		 
		 double sum = 0.0;

		 for(int i=0;i<8;i++)
			 sum += vec1[i];
		 
		 double vec3 = Math.sqrt(sum);
		 return vec3;
	 }
	 
	 
	 public double[] input_image(String ip_path)
	 {
		 
		 
		 Mat myImg = preprocessImage(ip_path);
		
		 double [] features = enhanceImg(myImg);
		 return features;
	 }
	 
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 public HashMap sortByValues(HashMap map) { 
		 List list = new LinkedList(map.entrySet());
	     // Defined Custom Comparator here
	     Collections.sort(list, new Comparator() {
	    	 public int compare(Object o1, Object o2) {
	    		 return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
		     }
		 });

		 // Here I am copying the sorted list in HashMap
		 // using LinkedHashMap to preserve the insertion order
		 HashMap sortedHashMap = new LinkedHashMap();
		 for (Iterator it = list.iterator(); it.hasNext();) {
			 Map.Entry entry = (Map.Entry) it.next();
		     sortedHashMap.put(entry.getKey(), entry.getValue());
		 }
		 
		 return sortedHashMap;
	 }
	 
 }
