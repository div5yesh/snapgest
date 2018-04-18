package phase_two;

import java.io.File;



public class MotionDetector {

	public static void main(String[] args) {
		
		Thread detector_thread = new Thread(new SnapThread());
		Thread recogniser_thread = new Recogniser_Thread();
		detector_thread.start();
		recogniser_thread.start();	
		
		
	}

}
