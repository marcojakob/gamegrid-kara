package scenario14_put_leaf_track;


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
	
    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		if (!onLeaf()) {
			putLeaf();
		}

		if (!treeFront()) {
			move();
		} else {
			stop();
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
