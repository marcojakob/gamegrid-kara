package scenario37_candles_for_age;


import kara.gamegrid.KaraWorld;
import kara.gamegrid.io.KaraIO;


/**
 * MyKara is a subclass of KaraIO. Therefore, it inherits all methods of KaraIO: <p>
 * <i>MyKara ist eine Unterklasse von KaraIO. Sie erbt damit alle Methoden der Klasse KaraIO:</i> <p>
 * 
 * Actions:      move(), turnLeft(), turnRight(), putLeaf(), removeLeaf() <b>
 * Sensors:      onLeaf(), treeFront(), treeLeft(), treeRight(), mushroomFront()
 * Input/Output: displayMessage(...), intInput(...), doubleInput(...)
 */
public class MyKaraIO extends KaraIO {
	
    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {

	}

	public void putLeafs(int count) {
		// write code here
	}

	public void multiMove(int steps) {
		// write code here
	}

	public void turnAround() {
		// write code here
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara world is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo die Kara Welt geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("WorldSetup.txt", MyKaraIO.class);
		world.show();
	}
}
