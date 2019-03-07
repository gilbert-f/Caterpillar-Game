import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;

public class PsychedelicCabbage extends Cabbage implements CaterpillarGameConstants {
	private Oval psychedelicCabbage1, psychedelicCabbage2, psychedelicCabbage3, psychedelicCabbage4;
	
	// Constructor
	public PsychedelicCabbage(GWindow window, Point center) {
		super(window, center);
		// Display the PsychedelicCabbage
		draw();
	}

	@Override
	// Method to draw the PsychedelicCabbage
	protected void draw() {
		psychedelicCabbage1 = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS, 
				2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, Color.ORANGE, true);
		psychedelicCabbage2 = new Oval(center.x - 4 * CABBAGE_RADIUS / 5, 
				center.y - 4 * CABBAGE_RADIUS / 5, 8 * CABBAGE_RADIUS / 5, 
				8 * CABBAGE_RADIUS / 5, Color.GRAY, true);
		psychedelicCabbage3 = new Oval(center.x - 3 * CABBAGE_RADIUS / 5, 
				center.y - 3 * CABBAGE_RADIUS / 5, 6 * CABBAGE_RADIUS / 5, 
				6 * CABBAGE_RADIUS / 5, Color.BLACK, true);
		psychedelicCabbage4 = new Oval(center.x - 2 * CABBAGE_RADIUS / 5, 
				center.y - 2 * CABBAGE_RADIUS / 5, 4 * CABBAGE_RADIUS / 5, 
				4 * CABBAGE_RADIUS / 5, Color.YELLOW, true);
		
		window.add(psychedelicCabbage1);
		window.add(psychedelicCabbage2);
		window.add(psychedelicCabbage3);
		window.add(psychedelicCabbage4);
		
	}

	@Override
	// Method to check whether the PsychedelicCabbage has been eaten or not
	public void isEatenBy(Caterpillar cp) {
		// Turns to multiple colors when eaten
		cp.multiColor = true;
		
		window.remove(psychedelicCabbage1);
		window.remove(psychedelicCabbage2);
		window.remove(psychedelicCabbage3);
		window.remove(psychedelicCabbage4);
		
	}

}
