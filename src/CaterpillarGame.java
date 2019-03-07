import uwcse.graphics.*;

import java.util.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

/**
 * A CaterpillarGame displays a garden that contains good and bad cabbages and a
 * constantly moving caterpillar. The player directs the moves of the
 * caterpillar. Every time the caterpillar eats a cabbage, the caterpillar
 * grows. The player wins when all of the good cabbages are eaten and the
 * caterpillar has left the garden. The player loses if the caterpillar eats a
 * bad cabbage or crawls over itself.
 */

public class CaterpillarGame extends GWindowEventAdapter implements
		CaterpillarGameConstants
// The class inherits from GWindowEventAdapter so that it can handle key events
// (in the method keyPressed), and timer events.
// All of the code to make this class able to handle key events and perform
// some animation is already written.
{
	// Game window
	private GWindow window;

	// The caterpillar
	private Caterpillar cp;

	// Direction of motion given by the player
	private int dirFromKeyboard;

	// Do we have a keyboard event
	private boolean isKeyboardEventNew = false;

	// The list of all the cabbages
	private ArrayList<Cabbage> cabbages;
	
	// The list of all the fences
	private ArrayList<Rectangle> fences;

	// is the current game over?
	private boolean gameOver;
	
	// Fences
	private String messageGameOver;

	private Rectangle upperFence;

	private Rectangle lowerFence;

	private Rectangle rightFence;

	private Rectangle firstLeftFence;

	private Rectangle secondLeftFence;
	
	// Timer for the multiple color
	private int colorTimer = 150;

	/**
	 * Constructs a CaterpillarGame
	 */
	public CaterpillarGame() {
		// Create the graphics window
		window = new GWindow("Caterpillar game", WINDOW_WIDTH, WINDOW_HEIGHT);

		// Any key or timer event while the window is active is sent to this
		// CaterpillarGame
		window.addEventHandler(this);

		// Set up the game (fence, cabbages, caterpillar)
		// Display the game rules
		int gameRulesOption = JOptionPane
				.showConfirmDialog(
						null,
						"Caterpillar Game"
								+ "\n--Instructions--"
								+ "\n1. Direct the caterpillar to eat good cabbages."
								+ "\n2. Eating psychedelic cabbages allow the caterpillar to change its color randomly."
								+ "\n3. Don't eat the bad cabbages or else the caterpillar will die."
								+ "\n4. Don't crash over the caterpillar body itself."
								+ "\n5. Don't hit the caterpillar to the fence, or else will game over."
								+ "\n6. Get out of the garden after all good cabbages have been eaten."
								+ "\n" + "\nHow to control the caterpillar: "
								+ "\nMove right: Press(a)."
								+ "\nMove left: Press(d)."
								+ "\nMove up: Press(w)."
								+ "\nMove down: Press(s)."
								+ "\nDirect exit: Press(q).",
						"Playing Instructions", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
		if (gameRulesOption == JOptionPane.OK_OPTION) {
			initializeGame();
		}
	}

	/**
	 * Initializes the game (draw the garden, garden fence, cabbages,
	 * caterpillar)
	 */
	private void initializeGame() {
		// Clear the window
		window.erase();

		// New game
		gameOver = false;

		// No keyboard event yet
		isKeyboardEventNew = false;

		// Background (the garden)
		window.add(new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT,
				Color.green, true));

		// Creates the fence around the garden
		drawFences();

		// Draws the cabbages
		drawCabbage();

		// Create the caterpillar
		cp = new Caterpillar(window);

		// start timer events (to do the animation)
		this.window.startTimerEvents(ANIMATION_PERIOD);
	}

	private void drawCabbage() {
		// Initialize cabbages ArrayList
		cabbages = new ArrayList<Cabbage>(N_GOOD_CABBAGES + N_BAD_CABBAGES
				+ N_PSY_CABBAGES);

		// Initialize the elements of cabbage ArrayList
		Random rand = new Random();
		for (int i = 0; i < N_GOOD_CABBAGES + N_BAD_CABBAGES + N_PSY_CABBAGES; i++) {
			boolean overlap;
			int x, y;
			do {
				x = 140 + rand.nextInt(330);
				y = 20 + rand.nextInt(450);
				overlap = false;

				for (Cabbage c : cabbages) {
					double distance = Point2D.distance(c.center.x, c.center.y,
							x, y);
					// So they do not overlap
					if (distance < 3 * CABBAGE_RADIUS) {
						overlap = true;
						break;
					}
				}
			} while (overlap);

			// Draw the cabbages
			if (i < N_GOOD_CABBAGES) {
				cabbages.add(new GoodCabbage(window, new Point(x, y)));
			} else if (i < N_GOOD_CABBAGES + N_BAD_CABBAGES) {
				cabbages.add(new BadCabbage(window, new Point(x, y)));
			} else {
				cabbages.add(new PsychedelicCabbage(window, new Point(x, y)));
			}
		}
		
	}

	private void drawFences() {
		fences = new ArrayList<Rectangle>();
		// Create the fence around the garden
		upperFence = new Rectangle(120, 0, 380, 10, Color.BLACK, true);
		lowerFence = new Rectangle(120, 490, 380, 10, Color.BLACK, true);
		rightFence = new Rectangle(490, 0, 10, 500, Color.BLACK, true);
		firstLeftFence = new Rectangle(120, 0, 10, 200, Color.BLACK, true);
		secondLeftFence = new Rectangle(120, 300, 10, 200, Color.BLACK, true);
		fences.add(upperFence);
		fences.add(lowerFence);
		fences.add(rightFence);
		fences.add(firstLeftFence);
		fences.add(secondLeftFence);
		// Adds the fences to window
		for (int i = 0; i < fences.size(); i++) {
			window.add(fences.get(i));
		}
	}

	/**
	 * Moves the caterpillar within the graphics window every ANIMATION_PERIOD
	 * milliseconds.
	 * 
	 * @param e
	 *            the timer event
	 */
	public void timerExpired(GWindowEvent e) {
		// Did we get a new direction from the user?
		// Use isKeyboardEventNew to take the event into account
		// only once
		if (isKeyboardEventNew) {
			isKeyboardEventNew = false;
			cp.move(dirFromKeyboard);
		} else
			cp.move();

		// Is the caterpillar eating a cabbage?
		isCabbagesEaten();
		multiColorCatepillar();
		// Checks if the game is over
		gameOver();

		// Is the game over?
		if (gameOver) endTheGame();
	}
	
	// Generate random colors when eating psychedelic cabbage
	private void multiColorCatepillar() {
		if (cp.multiColor) {
			colorTimer--;
			window.startTimerEvents(ANIMATION_PERIOD);
			if (colorTimer == 0) {
				cp.multiColor = false;
				colorTimer = 150;
			}
		}
		
	}
	
	// Checks if cabbage is eaten
	private void isCabbagesEaten() {
		// Loops through the cabbage ArrayList to check 
		// whether the cabbages are eaten
		for (int i = 0; i < cabbages.size(); i++) {
			Cabbage getCabbage = cabbages.get(i);
			if (getCabbage.isPointInCabbage(cp.getHead())) {
				getCabbage.isEatenBy(cp);
				cabbages.remove(i);
			}
		}
	}

	/**
	 * Moves the caterpillar according to the selection of the user i: NORTH, j:
	 * WEST, k: EAST, m: SOUTH
	 * 
	 * @param e
	 *            the keyboard event
	 */
	public void keyPressed(GWindowEvent e) {
		switch (Character.toLowerCase(e.getKey())) {
		case 'w':
			if (cp.getDirection() != SOUTH) {
				dirFromKeyboard = NORTH;
			}

			break;
		case 'a':
			if (cp.getDirection() != EAST) {
				dirFromKeyboard = WEST;
			}

			break;
		case 'd':
			if (cp.getDirection() != WEST) {
				dirFromKeyboard = EAST;
			}

			break;
		case 's':
			if (cp.getDirection() != NORTH) {
				dirFromKeyboard = SOUTH;
			}

			break;
		case 'q': // quit
			System.exit(0); // abruptly ends the application
			break;
		default:
			return;
		}

		// new keyboard event
		isKeyboardEventNew = true;
	}

	/**
	 * The game is over. Starts a new game or ends the application
	 */
	private void endTheGame() {
		window.stopTimerEvents();
		// messageGameOver is an instance String that
		// describes the outcome of the game that just ended
		// (e.g. congratulations! you win)
		boolean again = anotherGame(messageGameOver);
		if (again) {
			initializeGame();
		} else {
			System.exit(0);
		}
	}

	/**
	 * Does the player want to play again?
	 */
	private boolean anotherGame(String s) {
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Game over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new CaterpillarGame();
	}
	
	private void gameOver() {
		if (cp.isCrawlingOverItself()) {
			messageGameOver = "Don't crawl over yourself!"
					+ "\nGAME OVER!";
			gameOver = true;
		}
		else if (cp.die) {
			messageGameOver = "Don't eat a contaminated cabbage, dumbass!"
					+ "\nYou're dead!";
			gameOver = true;

		}
		else if (cp.cabbageCounter == N_GOOD_CABBAGES
				&& cp.isOutsideGarden()) {
			messageGameOver = "You have won the game!" + "\nYou called game!";
			gameOver = true;
		}
		else if (touchFence()) {
			messageGameOver = "Don't to a fence!"
					+ "\nKilled!";
			gameOver = true;
		}
	}

	private boolean touchFence() {
		Point head = cp.getHead();
		// Loops through the fence ArrayList
		for (int i = 0; i < fences.size(); i++) {
			// Gets the fences inside the list
			Rectangle touchFence = fences.get(i);

			if ((head.x >= touchFence.getX())
					&& (head.x <= touchFence.getX()
							+ touchFence.getWidth())
					&& (head.y >= touchFence.getY())
					&& (head.y <= touchFence.getY()
							+ touchFence.getHeight())) {
				return true;
			}
		}
		return false;
	}
}
