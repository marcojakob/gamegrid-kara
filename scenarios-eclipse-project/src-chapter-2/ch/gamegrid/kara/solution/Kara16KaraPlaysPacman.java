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
public class Kara16KaraPlaysPacman extends Kara {
	
    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		if (!treeFront()) {
			removeLeaf();
			findNextLeaf();
		}
	}

	public void findNextLeaf() {
		// look for leaf in front
		// (erst mal vorne schauen)
		move();
		if (!onLeaf()) {
			// no leaf in front, go back and look left
			// (kein Blatt vorne, also zurueck und links schauen)
			turnAndGoBack();
			turnRight();
			move();
			if (!onLeaf()) {
				// no leaf left; leaf must be on right side
				// (links ist auch kein Blatt; dann muss es rechts liegen)
				turnAndGoBack();
				move();
			}
		}
	}

	public void turnAndGoBack() {
		turnLeft();
		turnLeft();
		move();
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara world is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo die Kara Welt geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("worlds/Kara16.txt", Kara16KaraPlaysPacman.class);
		world.show();
	}
}
