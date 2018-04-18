package phase_two;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.util.ArrayList;

public class MouseMover {
	public Point current = null;
	public Point prev = null;
	public static ArrayList<Point> mouseLocation;

	public MouseMover() {
		mouseLocation = new ArrayList<Point>();

	}

	public void mouseMove(int i, int j) {

		try {
			Robot rob = new Robot();
			if (current != null) {
				prev = current;
			}
			int parts = 20;
			current = new Point(i, j);

			try {
				SnapThread.g.setColor(Color.RED);
				SnapThread.g.drawLine((int) current.x, (int) current.y,
						(int) prev.x, (int) prev.y);

				mouseLocation.add(current);

				double constx = (current.x - prev.x) / parts;
				double consty = (current.y - prev.y) / parts;

				for (int k = 0; k < parts; k++) {

					rob.mouseMove((int) (prev.x + constx),
							(int) (prev.y + consty));
					prev.x += constx;
					prev.y += consty;

				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			// System.out.println("Moving to x: " + i + " y: " + j);
		} catch (AWTException e) {
			
//			e.printStackTrace();
		}

	}

	public static ArrayList<Point> getMovementData() {
		return mouseLocation;
	}

}
