

import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_core.cvSub;
import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateCameraCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvRetrieveFrame;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GAUSSIAN;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_MEDIAN;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvSmooth;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;

import java.awt.Graphics;
import java.awt.event.ItemEvent;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.*;

public class SnapThread extends Thread
{
	private IplImage image;
	private ActionHandler action;
	static int state;
	static Graphics g;
	public SnapThread()
	{
		action=new ActionHandler();
	}
	public void run()
	{
		CvCapture capture = cvCreateCameraCapture(0);
		try
		{	
			while(true)
			{
				image = cvRetrieveFrame(capture);
				if(image!=null)
				{
					cvFlip(image, image, 1);
					cvSmooth(image, image, CV_GAUSSIAN, 5);
					Bounds.setImage(image);
//---------------------------------------------------------------------------------------------------------------
					IplImage blue = IplImage.create(image.cvSize(), image.depth(), 1);
					Memory.add(blue);
					cvSplit(image,null,null,blue,null);
					IplImage gray = IplImage.create(image.cvSize(), image.depth(), 1);
					Memory.add(gray);
					cvCvtColor(image,gray,CV_RGB2GRAY);
					IplImage diff_blue = IplImage.create(image.cvSize(), image.depth(), 1);
					Memory.add(diff_blue);
					CvMat diff_blue_mat=diff_blue.asCvMat();
					Memory.add(diff_blue_mat);
					cvSub(gray ,blue , diff_blue_mat, null);
					cvSmooth(diff_blue_mat, diff_blue_mat, CV_MEDIAN, 75);
					IplImage bin_blue = cvCreateImage(image.cvSize(), image.depth(), 1);
					cvThreshold(diff_blue_mat,bin_blue,20,200,CV_THRESH_BINARY);
					cvSmooth(bin_blue, bin_blue, CV_GAUSSIAN, 5);
//----------------------------------------------------------------------------------------------------------------								
	
					IplImage imgHSV = cvCreateImage(image.cvSize(), image.depth(), 3); 
				    cvCvtColor(image, imgHSV, CV_BGR2HSV);
				    IplImage bin_red = cvCreateImage(image.cvSize(), image.depth(), 1);
				    CvScalar rmin = cvScalar(162, 59, 0, 0);		
					CvScalar rmax = cvScalar(245, 245, 245, 0);
//					CvScalar rmin = cvScalar(hmin, smin, vmin, 0);		
//					CvScalar rmax = cvScalar(hmax, smax, vmax, 0);
					cvInRangeS(imgHSV, rmin,rmax, bin_red);
			        cvSmooth(bin_red, bin_red, CV_MEDIAN, 25);
			        cvSmooth(bin_red, bin_red, CV_GAUSSIAN, 5);					
//-----------------------------------------------------------------------------------------------------------------					
					CvRect cursor=Bounds.getBounds(bin_blue, true, 10);
					action.click_scroll(cursor);
//------------------------------------------------------------------------------------------------------------------						
					CvRect lclick=Bounds.getBounds(bin_red, true, 10);
					action.left_click(cursor, lclick);
//---------------------------------------------------------------------------------------------------------------		
					if(state==ItemEvent.SELECTED)
						g.drawImage(image.getBufferedImage(), 0, 0, 1024, 768, null);
//---------------------------------------------------------------------------------------------------------------------------		
					Memory.release();
				    opencv_core.cvReleaseImage(bin_blue);
					opencv_core.cvReleaseImage(imgHSV);
					opencv_core.cvReleaseImage(bin_red);
				}
			}
		}
		catch(Exception e){e.printStackTrace();}
	}	
}
