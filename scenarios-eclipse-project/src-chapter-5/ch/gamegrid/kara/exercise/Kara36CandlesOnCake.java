package ch.gamegrid.kara.exercise;


import ch.gamegrid.kara.actors.KaraIO;
import ch.gamegrid.kara.world.KaraWorld;


/**
 * MyKara is a subclass of KaraIO. Therefore, it inherits all methods of KaraIO: <p>
 * <i>MyKara ist eine Unterklasse von KaraIO. Sie erbt damit alle Methoden der Klasse KaraIO:</i> <p>
 * 
 * Actions:      move(), turnLeft(), turnRight(), putLeaf(), removeLeaf() <b>
 * Sensors:      onLeaf(), treeFront(), treeLeft(), treeRight(), mushroomFront()
 * Input/Output: displayMessage(...), intInput(...), doubleInput(...)
 */
public class Kara36CandlesOnCake extends KaraIO {
	
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
	 * The main-method is the start of the program where the Kara scenario is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo das Kara Szenario geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("scenarios/Kara36.txt", Kara36CandlesOnCake.class);
		world.show();
	}
}
