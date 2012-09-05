package ch.gamegrid.kara.exercise;


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
public class Kara18AroundTreeIII extends Kara {
	
    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		if (treeFront()) {
			goAroundTree();
		} else {
			move();
		}
	}

	public void goAroundTree() {
		turnLeft();
		move();
		turnRight();
		move();
		move();
		turnRight();
		move();
		turnLeft();
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara world is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo die Kara Welt geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("worlds/Kara18.txt", Kara18AroundTreeIII.class);
		world.show();
	}
}
