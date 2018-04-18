package phase_two;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvAbsDiff;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_CCOMP;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

@SuppressWarnings("serial")
public class DetectorThread extends Frame implements Runnable, WindowListener {

	private final int THRESHOLD_VALUE = 100;
	OpenCVFrameGrabber grabber;
	IplImage frame ;
	IplImage image ;
	IplImage prevImage ;
	IplImage diff ;
	MouseMover m;
	
//	CanvasFrame canvasFrame;
	public static Graphics g;
	


	public DetectorThread() {
//		 super("Motion Snap");
		try {

			grabber = new OpenCVFrameGrabber(0);

			grabber.setImageHeight(788);
			grabber.setImageWidth(1024);
			grabber.start();
			frame = grabber.grab();
			image = IplImage.create(frame.width(), frame.height(),
					IPL_DEPTH_8U, 1);
			prevImage=IplImage.create(frame.width(), frame.height(),
					IPL_DEPTH_8U, 1);
			diff = IplImage.create(frame.width(), frame.height(),
					IPL_DEPTH_8U, 1);
			m=new MouseMover();
//			canvasFrame = new CanvasFrame("Motion Detector");
//
//			canvasFrame.setCanvasSize(frame.width(), frame.height());
//			canvasFrame.addWindowListener(this);
//			canvasFrame.setVisible(true);
//			canvasFrame.setLocation(0, 0);
			 setSize(frame.width(), frame.height());
			 addWindowListener(this);
			 setVisible(true);
			 g=getGraphics();
			 setLocation(0,0);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		while (true) {
			try {
				frame = grabber.grab();
				cvFlip(frame, frame, 1);
				if (image == null) {
					image = IplImage.create(frame.width(), frame.height(),
							IPL_DEPTH_8U, 1);

					cvCvtColor(frame, image, CV_RGB2GRAY);
//					 canvasFrame.showImage(image);
				} else {
					// prevImage = IplImage.create(frame.width(),
					// frame.height(),
					// IPL_DEPTH_8U, 1);
					// prevImage.release();
					prevImage = image;
					image = IplImage.create(frame.width(), frame.height(),
							IPL_DEPTH_8U, 1);
					cvCvtColor(frame, image, CV_RGB2GRAY);
//					 canvasFrame.showImage(image);
				}
				if (diff == null) {
					diff = IplImage.create(frame.width(), frame.height(),
							IPL_DEPTH_8U, 1);
				}
				if (prevImage != null) {
					// perform ABS difference
					cvAbsDiff(image, prevImage, diff);

					// do some threshold for wipe away useless details
					cvThreshold(diff, diff, this.THRESHOLD_VALUE, 255,
							CV_THRESH_BINARY);

					findAndDrawContours();
					// mouseMove();
//					canvasFrame.showImage(diff);

					System.gc();
					// diff.release();
					// image.release();
					// prevImage.release();
					
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * moves mouse according to diff image ....
	 * 
	 * If you are changing diff image any time then make sure to change this
	 * method accordingly....
	 */

	/**
	 * finds contors from image diff...
	 */
	public void findAndDrawContours() {

		try {
			CvMemStorage mem = CvMemStorage.create();
			CvSeq contours = new CvSeq();
			CvSeq ptr = new CvSeq();
			cvFindContours(diff, mem, contours, Loader.sizeof(CvContour.class),
					CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));

			CvRect boundbox = cvRect(0,0,1,1);
			CvRect big = cvRect(0, 0, 1, 1);
			// CvRect small = cvRect(0, 0, 1, 1);

			big = cvBoundingRect(contours, 0);

			for (ptr = contours; ptr.isNull(); ptr = ptr.h_next()) {

				boundbox = cvBoundingRect(ptr, 0);
				Rectangle bb = new Rectangle(boundbox.x(), boundbox.y(),
						boundbox.width(), boundbox.height());
				Rectangle bigr = new Rectangle(big.x(), big.y(), big.width(),
						big.height());
				bigr.add(bb);
				big = cvRect(bigr.x, bigr.y, bigr.width, bigr.height);
				// if (boundbox.height() * boundbox.width() > big.height()
				// * big.width()) {
				// big = boundbox;
				// }

			}
			// for (ptr = contours; ptr != null; ptr = ptr.h_next()) {
			//
			// boundbox = cvBoundingRect(ptr, 0);
			// if (small.height() * small.width() < boundbox.height()
			// * boundbox.width()
			// && boundbox.width() * boundbox.height() < big
			// .height() * big.width()) {
			// small = boundbox;
			// }
			// }

			cvRectangle(diff, cvPoint(big.x(), big.y()),
					cvPoint(big.x() + big.width(), big.y() + big.height()),
					CvScalar.BLUE, 0, 0, 0);
			// p=new Point(big.x(), big.y());
			// Robot r=new Robot();
			// r.mouseMove(big.x()+big.width() /2, big.y()+big.height()/2);
			int x = big.x() + big.width() / 2, y = big.y() + big.height() / 2;
//			MouseMover.mouseMove(x, y);
			
			m.mouseMove(x, y);
			// g=canvasFrame.getGraphics();

			// cvRectangle(
			// diff,
			// cvPoint(small.x(), small.y()),
			// cvPoint(small.x() + small.width(), small.y()
			// + small.height()), CvScalar.BLUE, 0, 0, 0);
		} catch (java.lang.Exception e) {
			// return null;
		}

		// CvRect cursor = big;
		// if (small.y() < big.y()) {
		// cursor = small;
		// } else {
		// cursor = big;
		// }

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			grabber.stop();
		
//			 canvasFrame.dispose();
			System.exit(0);
		} catch (Exception e1) {

			e1.printStackTrace();
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
