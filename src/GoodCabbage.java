import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;

public class GoodCabbage extends Cabbage implements CaterpillarGameConstants {
	private Oval goodCabbage;
	
	// Constructor
	public GoodCabbage(GWindow window, Point center) {
		super(window, center);
		// Display the GoodCabbage
		draw();	
	}

	@Override
	// Method to draw the GoodCabbage
	protected void draw() {
		goodCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS,
				2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, Color.WHITE, true);
		window.add(goodCabbage);	
	}

	@Override
	// Method to check whether the GoodCabbage has been eaten or not
	public void isEatenBy(Caterpillar cp) {
		// Caterpillar grows
		cp.grow();
		// Count the number of GoodCabbage eaten
		cp.cabbageCounter++;
		window.remove(goodCabbage);	
	}
}
