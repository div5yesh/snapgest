package phase_two;

import java.awt.Color;

import javax.swing.JOptionPane;

public class Recogniser_Thread extends Thread {

	private final int SLEEP_TIME = 2;
	GestureRecogniser rec;

	public void run() {
		while (true) {
			try {
				Thread.sleep(SLEEP_TIME * 1000);
				// 
				showMessage();
				resetMovementData();
				clearScreen();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void showMessage() {
		rec = new GestureRecogniser(MouseMover.getMovementData());
		if (rec.isCircleGesture()) {
			JOptionPane.showMessageDialog(null, "Accepted");
		} else {
			JOptionPane.showMessageDialog(null, "Rejected");
		}

	}

	private void resetMovementData() {
		MouseMover.getMovementData().clear();
	}

	private void clearScreen() {
		SnapThread.g.setColor(Color.WHITE);
		SnapThread.g.fillRect(0, 0, 1300, 788);
	}
}
