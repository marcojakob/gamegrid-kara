package scenario25_push_mushroom_trough_tunnel;


import kara.gamegrid.Kara;
import kara.gamegrid.KaraWorld;


/**
 * MyKara is a subclass of Kara. Therefore, it inherits all methods of Kara: <p>
 * 
 * <i>MyKara ist eine Unterklasse von Kara. Sie erbt damit alle Methoden der Klasse Kara:</i> <p>
 * 
 * Actions:     move(), turnLeft(), turnRight(), putLeaf(), removeLeaf() <b>
 * Sensors:     onLeaf(), treeFront(), treeLeft(), treeRight(), mushroomFront()
 */
public class MyKaraSolution extends Kara {
	
	int chamberWidth = 1;
    int chamberHeight = 1;
    int tunnelLength = 0;

    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		// reset the variables
		chamberWidth = 1;
		chamberHeight = 1;
		tunnelLength = 0;

		// PHASE 1: Measuring
		// Note: Every method of PHASE 1 will start and end facing right!
		measureChamber();
		goToEndOfTunnel();
		goToTopRightOfRightChamber();

		turnLeft();
		turnLeft();

		// PHASE 2: Pushing Mushroom
		// Note: Every method of PHASE 2 will start and end facing left!
		pushMushroomToTunnelEntrance();
		pushMushroomTroughTunnel();
		pushMushroomOnLeaf();
		
		stop();
	}

	/**
	 * Measures the chamber. The start position must be the top left corner of
	 * the chamber. The end position will be the bottom right corner of the
	 * chamber.
	 */
	public void measureChamber() {
		// measure width
		while (!treeFront()) {
			chamberWidth++;
			move();
		}

		turnRight();

		while (!treeFront()) {
			chamberHeight++;
			move();
		}

		turnLeft();
	}

	/**
	 * Starts from the bottom right corner and goes to the end of the tunnel.
	 * The chamber must already have been measured.
	 */
	public void goToEndOfTunnel() {
		turnLeft();

		multiMove(chamberHeight / 2);

		turnRight();
		move();
		// now we are in the tunnel

		while (treeRight()) {
			// measure the tunnel length
			tunnelLength++;
			move();
		}
	}

	/**
	 * Starts at the end of the tunnel. Ends in the top right corner.
	 */
	public void goToTopRightOfRightChamber() {
		while (!treeFront()) {
			// must be careful not to push the mushroom to the right wall
			if (mushroomFront()) {
				// go around the mushroom
				turnLeft();
				move();
				turnRight();
				move();
				move();
				turnRight();
				move();
				turnLeft();
			}

			if (!treeFront()) {
				move();
			}
		}

		turnLeft();

		while (!treeFront()) {
			move();
		}

		turnRight();
	}

	/**
	 * Starts in the top right corner and pushes left on every line (except top
	 * most and bottom most) Ends at the start of the tunnel while mushroom is
	 * already in.
	 */
	public void pushMushroomToTunnelEntrance() {
		// start at second line
		turnLeft();
		move();
		turnRight();

		for (int i = 0; i < chamberHeight - 2; i++) {
			// push
			multiMove(chamberWidth - 2);

			// go back
			turnLeft();
			turnLeft();
			multiMove(chamberWidth - 2);

			// one step down
			turnRight();
			move();
			turnRight();
		}

		// Mushroom is now in the left column, we need to push it up or down to
		// the middle
		multiMove(chamberWidth - 1);
		turnRight();

		// possibly pushing mushroom up
		multiMove((chamberHeight / 2) - 1);

		turnRight();
		move();
		turnLeft();

		while (!treeFront()) {
			move();
		}

		turnLeft();
		move();
		turnLeft();

		// possibly pushing mushroom down
		multiMove((chamberHeight / 2) - 1);

		// push mushroom in the tunnel one step
		turnLeft();
		move();
		turnRight();
		move();
		turnRight();
		move();
	}

	/**
	 * Starts at the tunnel entrance. Ends at the end of the tunnel, while the
	 * mushroom is out of the tunnel but Kara is still one step in the tunnel.
	 */
	public void pushMushroomTroughTunnel() {
		multiMove(tunnelLength);
	}

	/**
	 * Starts at the end of the tunnel, while the mushroom is out of the tunnel
	 * but Kara is still one step in the tunnel. Ends at our goal!
	 */
	public void pushMushroomOnLeaf() {
		multiMove(chamberWidth - 1);

		turnRight();
		move();
		turnLeft();
		move();
		turnLeft();

		multiMove(chamberHeight / 2);

		// Go back home...
		turnLeft();
		turnLeft();
		while (!treeFront()) {
			move();
		}
		turnRight();
	}

	/**
	 * Moves the specified number of steps.
	 */
	public void multiMove(int steps) {
		int i = 0;
		while (i < steps) {
			move();
			i = i + 1;
		}
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara world is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo die Kara Welt geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("WorldSetup.txt", MyKaraSolution.class);
		world.show();
	}
}
