package phase_two;

import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_core.cvSub;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GAUSSIAN;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_MEDIAN;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_CCOMP;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvSmooth;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

@SuppressWarnings("serial")
public class SnapThread extends Frame implements Runnable
{
	
	private IplImage image;
	static int state;
	public static Graphics g;
	Frame f;
	public MouseMover mover;
	
	OpenCVFrameGrabber grabber;
	public SnapThread()
	{
		grabber = new OpenCVFrameGrabber(0);

		grabber.setImageHeight(788);
		grabber.setImageWidth(1024);
		try {
			grabber.start();
			
			image = grabber.grab();
		} catch (com.googlecode.javacv.FrameGrabber.Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setSize(image.width(), image.height());
//		 addWindowListener(this);
		setSize(image.width(),image.height());
		 setVisible(true);
		 g=getGraphics();
		 setLocation(0,0);
		 addWindowListener(
				 new WindowAdapter() {
					
					 public void windowClosing(WindowEvent e)
					 {
						 System.exit(0);
					 }
				}
				 );
		try{	
			mover=new MouseMover();
		}catch(Exception e){}
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
		
		//grabber.setImageHeight(786);
		//grabber.setImageWidth(1366);
		try
		{	
			while(true)
			{
				image = grabber.grab();
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
//						double scalex=(float)cursor.x()/1366;
//						double scaley=(float)cursor.y()/768;
//						double factorx=scalex*cursor.x();
//						double factory=scaley*cursor.y();
						int x=cursor.x();//-20 + (int)factorx*3;
						int y=cursor.y();//-20 + (int)factory*3;
//						robot.mouseMove(x, y);
						mover.mouseMove(x, y);
//						if(y<2)robot.mouseWheel(-2);
//						if(y>766)robot.mouseWheel(2);
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
//							robot.mousePress(InputEvent.BUTTON1_MASK);
						}
						//else
//							System.out.println("Release");
//							robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
//---------------------------------------------------------------------------------------------------------------		
					//cvSaveImage("a.jpg", bin_blue);
//					if(state==ItemEvent.SELECTED)
//				    	g.drawImage(image.getBufferedImage(), 0, 0, 1024, 788, null);
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
