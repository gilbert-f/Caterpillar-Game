import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.*;

public class BadCabbage extends Cabbage implements CaterpillarGameConstants {
	private Oval badCabbage;
	
	// Constructor
	public BadCabbage(GWindow window, Point center) {
		super(window, center);
		// Display the BadCabbage
		draw();
	}

	@Override
	// Method to draw the BadCabbage
	protected void draw() {
		badCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS,
				2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, Color.BLACK, true);
		window.add(badCabbage);	
	}

	@Override
	// Method to check whether the BadCabbage has been eaten or not
	public void isEatenBy(Caterpillar cp) {
		window.remove(badCabbage);
		// Caterpillar die when BadCabbage is eaten
		cp.die = true;
	}
}
