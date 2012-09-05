package scenario26;


import kara.gamegrid.actor.KaraSokoban;
import kara.gamegrid.world.GameScreen;


/**
 * MyKara is a subclass of KaraSokoban. Therefore, it inherits all methods of KaraSokoban <p>
 * <i>MyKara ist eine Unterklasse von KaraSokoban. Sie erbt damit alle Methoden der Klasse KaraSokoban</i> <p>
 */
public class MyKaraSolution extends KaraSokoban {
	
    int counter = 0;

    /**
     * In the 'act()' method you can write your program for Kara <br>
     * <i>In der Methode 'act()' koennen die Befehle fuer Kara programmiert werden</i>
     */
	public void act() {
		String key = getKey();

		if (key.equals("right")) {
			setDirectionRight();
			tryToMove();
		}

		if (key.equals("down")) {
			setDirectionDown();
			tryToMove();
		}

		if (key.equals("left")) {
			setDirectionLeft();
			tryToMove();
		}

		if (key.equals("up")) {
			setDirectionUp();
			tryToMove();
		}
	}

    /**
     * Kara makes one step. This method first tests if Kara can move or if he has to move
     * a mushroom first. <p> 
     * 
     * Kara macht einen Schritt. Diese Methode schaut zuerst, ob sich Kara bewegen kann 
     * oder ob er zuerst noch einen Pilz schieben muss.
     */
	public void tryToMove() {
		if (!treeFront()) {
			if (mushroomFront()) {
				if (canPushMushroom()) {
					move();
					counter++;
					setNumberOfMoves(counter);
				}
			} else {
				move();
				counter++;
				setNumberOfMoves(counter);
			}

			if (testLevelComplete()) {
				saveHighscore();
				levelComplete();
			}
		}
	}

	/**
	 * Handles the saving of the highscore.
	 * <p>
	 * 
	 * Diese Methode behandelt das Speichern der Highscore.
	 */
	public void saveHighscore() {
		// Test if it is in the top 3
		if (isHighscoreTop3(counter)) {
			// Is in top 3 --> add it
			addHighscoreEntry(counter);
		}
	}
	
	
	
	
	/**
	 * The main-method is the start of the program where the Kara world is loaded <br>
	 * <i>Die main-Methode ist der Start des Programms, wo die Kara Welt geladen wird</i>
	 */
	public static void main(String[] args) {
		GameScreen game = new GameScreen("Sokoban_Levels_Boxxle_1.txt", MyKaraSolution.class);
		game.setDeveloperMode(false);
		game.setHighscoreEnabled(true);
		game.show();
	}
}
