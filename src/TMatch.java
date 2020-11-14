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
import java.util.Iterator;
import java.util.List;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;


public class TMatch {
	static String matched;
	public static List<String> processImage(String imagePath, String deployPath) {
		try
		{
		 List<MatchRecord> records = new ArrayList<MatchRecord>();
		
		 File dir = new File(deployPath+"INC_images");
		 int threshold = 20;
		 File[] files = dir.listFiles();
		 List<String> imglist = new ArrayList<String>();
		 for (int i = 0; i < files.length; i++) {
System.out.println(files[i].getAbsolutePath());
			IplImage src = cvLoadImage(files[i].getAbsolutePath(), 0);
			//IplImage tmp = cvLoadImage("C:/Users/Acer/BE/Project/sift1/SIFT/src/superman.png", 0);
			IplImage tmp = cvLoadImage(imagePath, 0);
                
			IplImage result = cvCreateImage(cvSize(src.width() - tmp.width() + 1, src.height() - tmp.height() + 1), IPL_DEPTH_32F, 1);
			//
			
			
			cvZero(result);
			
			// Match Template Function from OpenCV
			
			cvMatchTemplate(src, tmp, result, CV_TM_CCORR_NORMED);
           // cvShowImage("result",result);
			// double[] min_val = new double[2];
			// double[] max_val = new double[2];

			DoublePointer min_val = new DoublePointer();
			DoublePointer max_val = new DoublePointer();

			CvPoint minLoc = new CvPoint();
			CvPoint maxLoc = new CvPoint();

			// Get the Max or Min Correlation Value
			cvMinMaxLoc(result, min_val, max_val, minLoc, maxLoc, null);
			if (minLoc.get() < threshold) {
				MatchRecord record = new MatchRecord();
				record.setCount(maxLoc.get());
				record.setName(files[i].getAbsolutePath());
				imglist.add(files[i].getName());
				record.setImage(src);
				records.add(record);
			}

			CvPoint point = new CvPoint();
			point.x(maxLoc.x() + tmp.width());
			point.y(maxLoc.y() + tmp.height());

			cvRectangle(src, maxLoc, point, CvScalar.WHITE, 2, 8, 0);// Draw a
																		// Rectangle
																		// for
																		// Matched
																		// Region

		}

		/*String filepath[];
		 filepath=new  String[10];
		 int i=0;
		for (Iterator iterator = records.iterator(); iterator.hasNext();) {
			MatchRecord matchRecord = (MatchRecord) iterator.next();
			//cvShowImage("Matched_Image", matchRecord.image);
			filepath[i]=matchRecord.getName();
			i++;
			System.out.println(filepath[i]);
			
		}*/
		
		
			cvWaitKey(0);
		
			//cvReleaseImage(src);
			//cvReleaseImage(tmp);
			//cvReleaseImage(result);

		
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
