import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_CCOMP;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class Bounds 
{
	private static IplImage image;
	public static void setImage(IplImage image) 
	{
		Bounds.image = image;
	}
	public static CvRect getBounds(IplImage binary,boolean visible,int offset)
	{
		CvMemStorage mem=CvMemStorage.create();
		CvSeq contours = new CvSeq();
		CvSeq ptr = new CvSeq();
		cvFindContours(binary, mem, contours, Loader.sizeof(CvContour.class) , CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, cvPoint(0,0));
		CvRect boundbox = null;
		CvRect big=null;
		if(!contours.isNull())
		{ 
			big = cvRect(0,0,1,1);
			for (ptr = contours; ptr != null; ptr = ptr.h_next()) 
			{
				
			    boundbox = cvBoundingRect(ptr, 0);
			    if(boundbox.height()*boundbox.width()>big.height()*big.width())
			    {
			    	big=boundbox;
			    }
			}
		if(visible)	
		cvRectangle( image, cvPoint( big.x()-offset, big.y()-offset ), cvPoint( big.x() + big.width()+offset, big.y() + big.height()+offset),CvScalar.BLUE, 0, 0, 0 );
		}
		return big;
	}
}
