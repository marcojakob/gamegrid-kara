package scenario37;


import kara.gamegrid.actor.KaraIO;
import kara.gamegrid.world.KaraWorld;


/**
 * MyKara is a subclass of KaraIO. Therefore, it inherits all methods of KaraIO: <p>
 * <i>MyKara ist eine Unterklasse von KaraIO. Sie erbt damit alle Methoden der Klasse KaraIO:</i> <p>
 * 
 * Actions:      move(), turnLeft(), turnRight(), putLeaf(), removeLeaf() <b>
 * Sensors:      onLeaf(), treeFront(), treeLeft(), treeRight(), mushroomFront()
 * Input/Output: displayMessage(...), intInput(...), doubleInput(...)
 */
public class MyKaraSolution extends KaraIO {
	
	/**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		drawRectangle(21, 4);

		int age = intInput("How old is your grandmother?");
		drawCandles(age / 10);
	}

	public void drawCandles(int count) {
		int i = 0;
		while (i < count) {
			move();
			turnLeft();
			putLeafs(3);
			turnAround();
			multiMove(3);
			turnLeft();
			move();
			i = i + 1;
		}
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
	 * The main-method is the start of the program where the Kara world is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo die Kara Welt geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("World*.txt", MyKaraSolution.class);
		world.show();
	}
}
