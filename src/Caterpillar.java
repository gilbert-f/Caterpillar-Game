import uwcse.graphics.*;

import java.awt.Point;
import java.util.*;
import java.awt.Color;

/**
 * A Caterpillar is the representation and the display of a caterpillar
 */

public class Caterpillar implements CaterpillarGameConstants {
	// The body of a caterpillar is made of Points stored
	// in an ArrayList
	private ArrayList<Point> body = new ArrayList<Point>();

	// Store the graphical elements of the caterpillar body
	// Useful to erase the body of the caterpillar on the screen
	private ArrayList<Rectangle> bodyUnits = new ArrayList<Rectangle>();

	// The window the caterpillar belongs to
	private GWindow window;

	// Direction of motion of the caterpillar (NORTH initially)
	private int dir = NORTH;
	
	public boolean die = false;
	
	public boolean multiColor = false;
	
	public int cabbageCounter = 0;

	/**
	 * Constructs a caterpillar
	 * 
	 * @param window
	 *            the graphics where to draw the caterpillar.
	 */
	public Caterpillar(GWindow window) {
		// Initialize the graphics window for this Caterpillar
		this.window = window;

		// Create the caterpillar (10 points initially)
		// First point
		Point p = new Point();
		p.x = 5;
		p.y = WINDOW_HEIGHT / 2;
		body.add(p);

		// Other points
		for (int i = 0; i < 9; i++) {
			Point q = new Point(p);
			q.translate(STEP, 0);
			body.add(q);
			p = q;
		}

		// Display the caterpillar
		draw();
	}
	
	// Method to display the caterpillar
	private void draw() {
		Point p = (Point) body.get(0);
		
		// Adds points between points p and q
		for (int i = 1; i < body.size(); i++) {
			Point q = (Point) body.get(i);
			addBody(p, q, bodyUnits.size());
			p = q;
		}

		window.doRepaint();
	}
	
	// Method to add rectangles from point p to point q
	private void addBody(Point p, Point q, int index) {
		// Initialize  the color of snake
		Color c;
		
		// Defines the corner of the rectangle where it starts
		int x = Math.min(q.x, p.x) - CATERPILLAR_WIDTH / 2;
		int y = Math.min(q.y, p.y) - CATERPILLAR_WIDTH / 2;

		// Defines the width and height of the rectangle
		int width = ((q.y == p.y) ? (STEP + CATERPILLAR_WIDTH)
				: CATERPILLAR_WIDTH);
		int height = ((q.x == p.x) ? (STEP + CATERPILLAR_WIDTH)
				: CATERPILLAR_WIDTH);

		// If multiColor is true, the caterpillar will be colorful
		if (multiColor == true) {
			c = new Color((int) (Math.random() * 256),
					(int) (Math.random() * 256), 
					(int) (Math.random() * 256));
		} else {
			c = Color.RED;
		}

		Rectangle r = new Rectangle(x, y, width, height, c, true);
		window.add(r);
		bodyUnits.add(index, r);
	}

	/**
	 * Moves the caterpillar in the current direction (complete)
	 */
	public void move() {
		move(dir);
	}

	/**
	 * Move the caterpillar in the direction newDir. <br>
	 * If the new direction is illegal, select randomly a legal direction of
	 * motion and move in that direction.<br>
	 * 
	 * @param newDir
	 *            the new direction.
	 */
	public void move(int newDir) {
		boolean isMoveIllegal;

		boolean isFirstMoveLegal = true;

		// Moves the caterpillar in direction newDir
		do {
			Point head = new Point(body.get(body.size() - 1));
			switch (newDir) {
			case NORTH:
				head.y -= STEP;
				break;
			case SOUTH:
				head.y += STEP;
				break;
			case EAST:
				head.x += STEP;
				break;
			case WEST:
				head.x -= STEP;
				break;
			}
			// Checks if the new position is still in the window
			if (isPointInTheWindow(head)) {
				isMoveIllegal = false;
				body.remove(0);
				body.add(head);
			} else {
				isMoveIllegal = true;
				// Select another direction
				if (newDir != dir && isFirstMoveLegal) {
					newDir = dir;
					isFirstMoveLegal = false;
				} else {
					// Assigned direction
					if (dir == NORTH) {
						newDir = EAST;
					} else if (dir == EAST) {
						newDir = SOUTH;
					} else if (dir == SOUTH) {
						newDir = WEST;
					} else {
						newDir = NORTH;
					}
				}

			}
		} while (isMoveIllegal);

		dir = newDir;

		// Move the caterpillar
		moveCaterpillar();
	}

	private void moveCaterpillar() {
		// Erase the body unit at the getPart
		window.remove(bodyUnits.get(0));
		bodyUnits.remove(0);

		// Add a new body unit at the head
		Point q = body.get(body.size() - 1);
		Point p = body.get(body.size() - 2);
		addBody(p, q, bodyUnits.size());

		// Repaint window
		window.doRepaint();
	}

	private boolean isPointInTheWindow(Point p) {
		return (p.x >= 0 && p.x <= WINDOW_WIDTH && p.y > 0 && p.y <= WINDOW_HEIGHT);
	}

	/**
	 * Is the caterpillar crawling over itself?
	 * 
	 * @return true if the caterpillar is crawling over itself and false
	 *         otherwise.
	 */
	public boolean isCrawlingOverItself() {
		Point head = getHead();
		
		// Is the head point equal to any other point of the caterpillar?
		for (int i = 0; i < body.size() - 1; i++) {
			Point p = body.get(i);
			if (head.x == p.x && head.y == p.y){ 
				return true;
			}
		}

		return false;
	}

	/**
	 * Are all of the points of the caterpillar outside the garden
	 * 
	 * @return true if the caterpillar is outside the garden and false
	 *         otherwise.
	 */
	public boolean isOutsideGarden() {
		if (body.get(0).x >= 0 && body.get(0).x < 115 && body.get(0).y >= 0
				&& body.get(0).y < 500 && body.get(body.size() / 2).x >= 0
				&& body.get(body.size() / 2).x < 115
				&& body.get(body.size() / 2).y >= 0
				&& body.get(body.size() / 2).y < 500
				&& body.get(body.size() - 1).x >= 0
				&& body.get(body.size() - 1).x < 115
				&& body.get(body.size() - 1).y >= 0
				&& body.get(body.size() - 1).y < 500) return true;
		
		else return false;
	}

	/**
	 * Return the location of the head of the caterpillar (complete)
	 * 
	 * @return the location of the head of the caterpillar.
	 */
	public Point getHead() {
		return new Point((Point) body.get(body.size() - 1));
	}

	/**
	 * Increase the length of the caterpillar (by GROWTH_SPURT elements) Add the
	 * elements at the tail of the caterpillar.
	 */
	public void grow() {
		// Gets the body point of the tail
		Point getPart = body.get(0);
		// Gets the body point before the tail
		Point beforegetPart = body.get(1);
		// Makes new point of the growth
		Point growth;

		// Where to add the growth when it eats the GoodCabbage
		if ((getPart.x <= beforegetPart.x) && (getPart.y == beforegetPart.y)) {
			growth = new Point(getPart.x - GROWTH_SPURT, getPart.y);
		} else if ((getPart.x >= beforegetPart.x)
				&& (getPart.y == beforegetPart.y)) {
			growth = new Point(getPart.x + GROWTH_SPURT, getPart.y);
		} else if ((getPart.x == beforegetPart.x)
				&& (getPart.y >= beforegetPart.y)) {
			growth = new Point(getPart.x, getPart.y + GROWTH_SPURT);
		} else {
			growth = new Point(getPart.x, getPart.y - GROWTH_SPURT);
		}

		// Adds the part of the body to the body ArrayList
		body.add(0, growth);
		// Adds back the body unit to the caterpillar
		// to the first part of body
		addBody(growth, getPart, 0);
	}
	
	// Method to get the direction of the caterpillar
	public int getDirection() {
		return dir;
	}
}
