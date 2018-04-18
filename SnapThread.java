import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.TrayIcon;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.opencv.core.CvType;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class SnapThread extends Thread
{
	final private int red=1;
	final private int green=2;
	final private int blue=3;
	private IplImage image;
	private Robot robot;
	private int x,y;
	static int state;
	static Graphics g;
	Frame f;
	
	public SnapThread()
	{
		try{	robot=new Robot();	}catch(Exception e){}
	}
	public void run()
	{
//		f=new Frame();
//        f.setSize(1, 1);
//        f.setUndecorated(true);
//        f.setVisible(true);
//        f.setAlwaysOnTop(true);
//        f.setAutoRequestFocus(isDaemon());
//        f.addKeyListener(new KeyAdapter()
//        {
//        	public void keyPressed(KeyEvent e)
//        	{
//        		if(e.isAltDown()&&e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_F3)
//        		{
//        			SnapStart.trayIcon.displayMessage("SnapGest","Y", TrayIcon.MessageType.INFO);
//        		}
//        	}
//        });
		CvCapture capture = cvCreateCameraCapture(0);
		//grabber.setImageHeight(786);
		//grabber.setImageWidth(1366);
		try
		{	
			while(true)
			{
				image = cvRetrieveFrame(capture);
				if(image!=null)
				{
					cvFlip(image, image, 1);	
					cvSmooth(image, image, CV_GAUSSIAN, 5);
//---------------------------------------------------------------------------------------------------------------
					IplImage blue = IplImage.create(image.cvSize(), image.depth(), 1);
					cvSplit(image,null,null,blue,null);
					IplImage gray = IplImage.create(image.cvSize(), image.depth(), 1);	
					cvCvtColor(image,gray,CV_RGB2GRAY);
					IplImage diff_blue = IplImage.create(image.cvSize(), image.depth(), 1);
					CvMat diff_blue_mat=diff_blue.asCvMat();
					cvSub(gray ,blue , diff_blue_mat, null);
					cvSmooth(diff_blue_mat, diff_blue_mat, CV_MEDIAN, 75);
					IplImage bin_blue = cvCreateImage(image.cvSize(), image.depth(), 1);
					cvThreshold(diff_blue_mat,bin_blue,20,200,CV_THRESH_BINARY);
					cvSmooth(bin_blue, bin_blue, CV_GAUSSIAN, 5);
//----------------------------------------------------------------------------------------------------------------								

//					IplImage r = IplImage.create(image.cvSize(), image.depth(), 1);
//					IplImage gr = IplImage.create(image.cvSize(), image.depth(), 1);
//					//IplImage b = IplImage.create(image.cvSize(), image.depth(), 1);
//					cvSplit(image,null,gr,r,null);
//				//	cvAdd(b, gr, gr, null);
//					cvSub(r, gr, r, null);
//					cvThreshold(r, r, 20, 255, CV_THRESH_BINARY);
//		
					
					
					
					//IplImage gray = IplImage.create(image.cvSize(), image.depth(), 1);	
					//cvCvtColor(image,gray,CV_RGB2GRAY);
					//IplImage diff_blue = IplImage.create(image.cvSize(), image.depth(), 1);
					//CvMat diff_blue_mat=diff_blue.asCvMat();
					//cvSub(gray.asCvMat(),blue.asCvMat(),diff_blue_mat,null);
					//cvSmooth(diff_blue_mat, diff_blue_mat, CV_MEDIAN, 75);
					//IplImage bin_blue = cvCreateImage(image.cvSize(), image.depth(), 1);
					//cvThreshold(diff_blue_mat,bin_blue,20,200,CV_THRESH_BINARY);
					//cvSmooth(bin_blue, bin_blue, CV_GAUSSIAN, 5);
					
					IplImage imgHSV = cvCreateImage(image.cvSize(), image.depth(), 3); 
				    cvCvtColor(image, imgHSV, CV_BGR2HSV);
				    IplImage bin_red = cvCreateImage(image.cvSize(), image.depth(), 1);
				    CvScalar rmin = cvScalar(160, 0, 0, 0);		
					CvScalar rmax = cvScalar(180, 255, 255, 0);
			        cvInRangeS(imgHSV, rmin,rmax, bin_red);
			        cvSmooth(bin_red, bin_red, CV_MEDIAN, 25);
			        cvSmooth(bin_red, bin_red, CV_GAUSSIAN, 5);
//-----------------------------------------------------------------------------------------------------------------					
					
//			            IplImage imgHSVg = cvCreateImage(image.cvSize(), image.depth(), 3); 
//						IplImage bin_green = cvCreateImage(image.cvSize(), image.depth(), 1);
//				            cvCvtColor(image, imgHSVg, CV_BGR2HSV);
//				            CvScalar min2 = cvScalar(190, 0, 0, 0);		
//						    CvScalar max2 = cvScalar(210, 255, 255, 0);
//				            cvInRangeS(imgHSVg, min2,max2, bin_green);
//				            cvSmooth(bin_red, bin_red, CV_MEDIAN, 35);
//				            cvSmooth(bin_red, bin_red, CV_GAUSSIAN, 5);
//											
//					IplImage bin_red = cvCreateImage(cvGetSize(image), 8, 1);						

//-----------------------------------------------------------------------------------------------------------------					
					CvRect cursor=getBounds(bin_blue, true, 10);
					if(cursor!=null)
					{
						//SnapStart.trayIcon.displayMessage("SnapGest","Blue detected.", TrayIcon.MessageType.INFO);
						double scalex=(float)cursor.x()/1366;
						double scaley=(float)cursor.y()/768;
						double factorx=scalex*cursor.x();
						double factory=scaley*cursor.y();
						int x=cursor.x()-20 + (int)factorx*3;
						int y=cursor.y()-20 + (int)factory*3;
						robot.mouseMove(x, y);
						if(y<2)robot.mouseWheel(-2);
						if(y>766)robot.mouseWheel(2);
						//cvRectangle( image, cvPoint( cursor.x()+(int)factor*3, cursor.y() ), cvPoint( cursor.x()+(int)factor + 10, cursor.y() + 10),CvScalar.BLUE, 0, 0, 0 );
					}
//------------------------------------------------------------------------------------------------------------------						
					CvRect lclick=getBounds(bin_red, true, 10);
					if(lclick!=null&&cursor!=null)
					{
						Rectangle r1=new Rectangle(cursor.x()-20,cursor.y()-20,cursor.width()+20,cursor.height()+20);
						Rectangle r2=new Rectangle(lclick.x()-20,lclick.y()-20,lclick.width()+20,lclick.height()+20);
						
						if(r1.intersects(r2))
						{
							System.out.println("click");
//							robot.mouseWheel(10);
							robot.mousePress(InputEvent.BUTTON1_MASK);
						}
						else
							
							robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
//---------------------------------------------------------------------------------------------------------------		
					//cvSaveImage("a.jpg", bin_blue);
					if(state==ItemEvent.SELECTED)
				    	g.drawImage(image.getBufferedImage(), 0, 0, 1024, 768, null);
//---------------------------------------------------------------------------------------------------------------------------					
				    blue.release();
				    gray.release();
				    diff_blue.release();
					diff_blue_mat.release();
					opencv_core.cvReleaseImage(bin_blue);
					opencv_core.cvReleaseImage(imgHSV);
					opencv_core.cvReleaseImage(bin_red);
				}
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
	private CvRect getBounds(IplImage binary,boolean visible,int offset)
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
