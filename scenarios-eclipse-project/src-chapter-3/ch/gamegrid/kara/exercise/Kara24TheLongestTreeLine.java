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
public class Kara24TheLongestTreeLine extends Kara {
	
    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		move();
		turnRight();
		move();
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara scenario is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo das Kara Szenario geladen wird</i>
	 */
	public static void main(String[] args) {
		KaraWorld world = new KaraWorld("scenarios/Kara24.txt", Kara24TheLongestTreeLine.class);
		world.show();
	}
}
