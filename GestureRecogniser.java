package phase_two;

import java.awt.Point;
import java.util.ArrayList;
import framework.*;

public class GestureRecogniser {

	private final double RADIUS_GAP_FACTOR = 0.8, SIMILARITY_PER = 0.75;
	public int minX = 5000, maxX = 0, minY = 5000, maxY = 0, centerX, centerY,
			size;
	public ArrayList<Point> p;
	public Circle small, big;
	double minRadius, maxRadius, rectHeight, rectWidth;
	
	public GestureRecogniser() {
		
	}
	public GestureRecogniser(ArrayList<Point> mouseLocation) {
		 p = mouseLocation;
		 size = p.size();
	}

	private void findMinMax() {

		for (int i = 0; i < size; i++) {
			if (p.get(i).x < minX) {
				minX = p.get(i).x;

			}
			if (p.get(i).y < minY) {
				minY = p.get(i).y;

			}
			if (p.get(i).x > maxX) {
				maxX = p.get(i).x;

			}
			if (p.get(i).y > maxY) {
				maxY = p.get(i).y;

			}
		}
	}

	/**
	 * Before calling this method make sure that you have already called
	 * findMinMax method...
	 */
	private void setCenters() {
		centerX = minX + (maxX - minX) / 2;
		centerY = minY + (maxY - minY) / 2;
	}

	/*
	 * For circle gesture....
	 */
	public boolean isCircleGesture() {

		findMinMax();
		setCenters();

		System.out.println("CenterX " + centerX + " CenterY " + centerY);

		rectHeight = maxY - minY;
		rectWidth = maxX - minX;
		double greater = (rectHeight > rectWidth) ? rectHeight : rectWidth;
		double diff = Math.abs(rectHeight - rectWidth);

		if ((greater * 0.3) < diff) {
			System.out.println("Discarded . greater" + greater + " diff: "
					+ diff);
			return false;
		}

		minRadius = (rectHeight * RADIUS_GAP_FACTOR) / 2;
		maxRadius = (greater / 2) + 10;
		print();
		System.out.println("RectHeight: " + rectHeight + " minRadius: "
				+ minRadius + " maxRadius:" + maxRadius);
		small = new Circle(minRadius, centerX, centerY);
		big = new Circle(maxRadius, centerX, centerY);
		int counter = 0;
		for (int i = 0; i < size; i++) {
			if (!small.contains(p.get(i)) && big.contains(p.get(i))) {
				// System.out.println("X: "+p.get(i).x + " Y: "+p.get(i).y);
				counter++;
			}
		}
		System.out.println("size: " + size + " Counter : " + counter);
		double per = (((double) counter / size) * 100);
		if ((size * SIMILARITY_PER) < counter) {
			System.out.println("Match percentage : " + per + "%");
			return true;
		}

		System.out.println("Match percentage : " + per + "%");
		return false;
	}

	public void print() {
		System.out.println(" minX:" + minX + " minY: " + minY + " maxX: "
				+ maxX + " maxY: " + maxY);
	}

}
