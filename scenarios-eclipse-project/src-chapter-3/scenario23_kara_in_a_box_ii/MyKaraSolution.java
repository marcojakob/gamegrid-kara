package scenario23_kara_in_a_box_ii;


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
	
    boolean goingRight = true;
    int step = 0;

    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		putLeafIfEvenStep();
		
		if (treeFront()) {
			if (goingRight) {
				// we are at the right border
				turnAroundRight();
			} else {
				// we are at the left border
				turnAroundLeft();
			}
		} else {
			move();
			step = step + 1;
		}
	}
	
	public void turnAroundRight() {
		if (treeRight()) {
			// we are in the bottom right corner
			stop();
		} else {
			turnRight();
			move();
			turnRight();
			goingRight = false;
			step = step + 1;
		}
	}
	
	public void turnAroundLeft() {
		if (treeLeft()) {
			// we are in the bottom left corner
			stop();
		} else {
			turnLeft();
			move();
			turnLeft();
			goingRight = true;
			step = step + 1;
		}
	}

	public void putLeafIfEvenStep() {
		if (step % 2 == 0) {
			// even step number --> put a leaf
			putLeaf();
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
