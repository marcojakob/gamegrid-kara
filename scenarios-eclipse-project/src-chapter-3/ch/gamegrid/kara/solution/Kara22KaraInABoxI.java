package ch.gamegrid.kara.solution;


import ch.gamegrid.kara.actors.Kara;
import ch.gamegrid.kara.world.KaraWorld;


/**
 * MyKara is a subclass of Kara. Therefore, it inherits all methods of Kara: <p>
 * 
 * <i>MyKara ist eine Unterklasse von Kara. Sie erbt damit alle Methoden der Klasse Kara:</i> <p>
 * 
 * Actions:     move(), turnLeft(), turnRight(), putLeaf(), removeLeaf() <b>
 * Sensors:     onLeaf(), treeFront(), treeLeft(), treeRight(), mushroomFront()
 */
public class Kara22KaraInABoxI extends Kara {
	
    boolean goingRight = true;
    boolean finished = false;
	
    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		// process the first line
		processLine();

		while (!finished) {
			if (goingRight) {
				if (!treeRight()) {
					turnRight();
					move();
					turnRight();
					// we have turned and now go left
					goingRight = false;

					processLine();
				} else {
					// we are in the bottom right corner
					finished = true;
				}
			} else {
				if (!treeLeft()) {
					turnLeft();
					move();
					turnLeft();
					// we have turned and now go right
					goingRight = true;

					processLine();
				} else {
					// we are in the bottom left corner
					finished = true;
				}
			}
		}
	}

	public void processLine() {
		while (!treeFront()) {
			invertField();
			move();
		}

		// invert the last field in the corner
		invertField();
	}

	public void invertField() {
		if (onLeaf()) {
			removeLeaf();
		} else {
			putLeaf();
		}
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara scenario is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo das Kara Szenario geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("scenarios/Kara22.txt", Kara22KaraInABoxI.class);
		world.show();
	}
}
