package ch.gamegrid.kara.exercise;


import ch.gamegrid.kara.actors.KaraSokoban;
import ch.gamegrid.kara.world.GameScreen;


/**
 * MyKara is a subclass of KaraSokoban. Therefore, it inherits all methods of KaraSokoban <p>
 * <i>MyKara ist eine Unterklasse von KaraSokoban. Sie erbt damit alle Methoden der Klasse KaraSokoban</i> <p>
 */
public class Kara26Sokoban extends KaraSokoban {
	
    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara scenario is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo das Kara Szenario geladen wird</i>
	 */
	public static void main(String[] args) {
		GameScreen game = new GameScreen("scenarios/Sokoban_Levels.txt", Kara26Sokoban.class);
		game.setDeveloperMode(true);
		game.setHighscoreEnabled(false);
		game.show();
	}
}
