//package sample_color_based;

import java.util.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;

class color_FeaturesExtraction{
	
	public double[][] colorFeatures(String img_path){
	//public CvScalar[] colorFeatures(String img_path){
		//final color features of an image.
		double colorFeatures[][] = new double[3][3];
		
		//loading input image.
		IplImage IMG = cvLoadImage(img_path);
		
		IplImage Resize_img = cvCreateImage ( cvSize(300 , 300 ), IMG.depth(), IMG.nChannels() );
		
		//resizing image
		cvResize(IMG, Resize_img);
		//cvShowImage("Resized", Resize_img);
		//waitKey();
		
		//Step 1 : Converting RGB to HSV.
		IplImage IMG_HSV = cvCreateImage(cvGetSize(IMG), IPL_DEPTH_8U, 3);
		
		//cvShowImage("ORIGINAL", IMG); 	//display original RGB input image.
		
		cvCvtColor(IMG, IMG_HSV, CV_BGR2HSV);	//convert RGB to HSV.
		
		//Step 2 : Convert HSV to 3 components 'H', 'S', 'V'
		//Creating 3 channels viz. H, S, V.
		IplImage IMG_H, IMG_S, IMG_V;
	    IMG_H = cvCreateImage(cvGetSize(IMG), IPL_DEPTH_8U, 1);
	    IMG_S = cvCreateImage(cvGetSize(IMG), IPL_DEPTH_8U, 1);
	    IMG_V = cvCreateImage(cvGetSize(IMG), IPL_DEPTH_8U, 1);
	    
	    //Splitting HSV to H-S-V.
	    cvSplit(IMG_HSV, IMG_H, IMG_S, IMG_V, null);
	    
	    //Step 3 : Equalizing 3 channels.
	    cvEqualizeHist(IMG_H, IMG_H);
	    cvEqualizeHist(IMG_S, IMG_S);
	    cvEqualizeHist(IMG_V, IMG_V);
		
	    //Display HSV image.
	    //cvShowImage("HSV", IMG_HSV);	
	    
		//Calculating 3 moments viz. mean, SD and skewness of H		
	
		double h_mean, h_sd, h_skew, h_skew1;
		
		CvScalar MeanScalar=new CvScalar();
		CvScalar StandardDeviationScalar=new CvScalar();
				
	    cvAvgSdv(IMG_H,MeanScalar,StandardDeviationScalar,null);
	    
	    CvScalar [] h = new CvScalar[6];
	    
	   // h[0] = MeanScalar;
	    //h[1] = StandardDeviationScalar;
	    
	    h_mean=Math.floor(MeanScalar.getVal(0)*1e5)/1e5;
	    h_sd=Math.floor(StandardDeviationScalar.getVal(0)*1e5)/1e5;
				
		double H_skewness = 0.0;
		
		CvScalar s1;
	    for (int x = 0; x<IMG_H.width(); x++) {
	    	
	    	for (int y = 0; y<IMG_H.height(); y++) {
	    		s1=cvGet2D(IMG_H,y,x);
	    		H_skewness = H_skewness + Math.pow(((s1.val(0)-MeanScalar.getVal(0))/StandardDeviationScalar.getVal(0)), 3);
		    }
		}
	    int total1=IMG_H.width()*IMG_H.height();
	    h_skew1=(H_skewness/total1);
	    
	    h_skew = Math.floor(h_skew1*1e5)/1e5;
		
		colorFeatures[0][0] = h_mean;
		colorFeatures[0][1] = h_sd;
		colorFeatures[0][2] = h_skew;
		
		
		//Calculating 3 moments viz. mean, SD and skewness of S
		double s_mean, s_sd, s_skew,s_skew1;
		
		cvAvgSdv(IMG_S,MeanScalar,StandardDeviationScalar,null);
		
		h[2] = MeanScalar;
		h[3] = StandardDeviationScalar;
		
		s_mean = Math.floor(MeanScalar.getVal(0)*1e5)/1e5;
		s_sd = Math.floor(StandardDeviationScalar.getVal(0)*1e5)/1e5;
				
        double S_skewness = 0.0;
		
		CvScalar s2;
	    for (int x = 0; x<IMG_S.width(); x++) {
	       	for (int y = 0; y<IMG_S.height(); y++) {
	    		s2=cvGet2D(IMG_S,y,x);
	    		S_skewness = S_skewness + Math.pow(((s2.val(0)-MeanScalar.getVal(0))/StandardDeviationScalar.getVal(0)), 3);
		    }
		}
	    int total2=IMG_S.width()*IMG_S.height();
	    s_skew1=(S_skewness/total2);
	    s_skew = Math.floor(s_skew1*1e5)/1e5;
		
		colorFeatures[1][0] = s_mean;
		colorFeatures[1][1] = s_sd;
		colorFeatures[1][2] = s_skew;
		
		//Calculating 3 moments viz. mean, SD and skewness of V
		
		double v_mean, v_sd, v_skew,v_skew1;
		cvAvgSdv(IMG_V,MeanScalar,StandardDeviationScalar,null);
		
		h[4] = MeanScalar;
		h[5] = StandardDeviationScalar;
		
		v_mean = Math.floor(MeanScalar.getVal(0)*1e5)/1e5;
		v_sd = Math.floor(StandardDeviationScalar.getVal(0)*1e5)/1e5;
				
        double V_skewness = 0.0;
		
		CvScalar s3;
	    for (int x = 0; x<IMG_V.width(); x++) {
	       	for (int y = 0; y<IMG_V.height(); y++) {
	    		s3=cvGet2D(IMG_V,y,x);
	    		V_skewness = V_skewness + Math.pow(((s3.val(0)-MeanScalar.getVal(0))/StandardDeviationScalar.getVal(0)), 3);
		    }
		}
	    int total3=IMG_V.width()*IMG_V.height();
	    v_skew1=(V_skewness/total3);
	    v_skew = Math.floor(v_skew1*1e5)/1e5;
		
		colorFeatures[2][0] = v_mean;
		colorFeatures[2][1] = v_sd;
		colorFeatures[2][2] = v_skew;

		
		
		//Display features
		/*for(int i=0;i<9;i++){
			System.out.println(colorFeatures[i]);
		}*/
		return colorFeatures;
		//return h;
	}
	
	public double[][] matDiff(double[][] img_1, double[][] img_2){
		double [][] resDiff = new double[3][3];
		
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				resDiff[i][j] = Math.abs(img_1[i][j] - img_2[i][j]);
		
		return resDiff;
	}
	
	public double[][] matMul(double[][] img_1, double[][] img_2){
		double [][] resDiff = new double[3][3];
		System.out.println("inside mat mul - wt_1");
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				System.out.println(img_1[i][j]);
		
		System.out.println("inside mat mul - res");
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				System.out.println(img_2[i][j]);
		double sum=0.0;
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++){
				for(int k=0;k<3;k++){
					sum += img_1[i][k]*img_2[k][j];
				}
				resDiff[i][j] = sum;
				sum=0;
			}		
		return resDiff;
	}
	
	public double final_hsv_mom(double[][] res, double[][] ip_features){
		//double res[][] = new double[3][3];
		res = matDiff(res,ip_features);
		double hsv_mom; 
		
		System.out.println("DIFFERENCE:");
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				System.out.println(res[i][j]);
		
		hsv_mom = res[0][0]+res[0][1]+res[0][2]+res[1][0]+res[1][1]+res[1][2]+res[2][0]+res[2][1]+res[2][2];
		//hsv_mom = res[0][0]+res[0][1]+res[1][0]+res[1][1]+res[2][0]+res[2][1];	
		System.out.println("\nhsv_mom : "+hsv_mom);
		System.out.println("---------------------------------------");
		return hsv_mom;
	}
	
//	public CvArr final_hsv_mom_1(CvArr i1, CvArr i2){
//		cvAbsDiff(i1, i2, i1);
//		return i1;
//	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap sortByValues(HashMap map) { 
       List list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
    	   public int compare(Object o1, Object o2) {
    		   return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
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