package ch.gamegrid.kara.solution;


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
public class Kara35BakingACake extends KaraIO {
	
	/**
	 * In the 'act()' method you can write your program for Kara <br>
	 * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert
	 * werden</i>
	 */
	public void act() {
		drawRectangle(21, 4);
	}

	public void drawRectangle(int width, int height) {
		int i = 0;
		while (i < height) {
			putLeafs(width);
			turnAround();
			multiMove(width);

			// go to next line
			turnRight();
			move();
			turnRight();

			i = i + 1;
		}
	}

	public void putLeafs(int count) {
		int i = 0;
		while (i < count) {
			putLeaf();
			move();
			i = i + 1;
		}
	}

	public void multiMove(int steps) {
		int i = 0;
		while (i < steps) {
			move();
			i = i + 1;
		}
	}

	public void turnAround() {
		turnLeft();
		turnLeft();
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara scenario is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo das Kara Szenario geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("scenarios/Kara35.txt", Kara35BakingACake.class);
		world.show();
	}
}
