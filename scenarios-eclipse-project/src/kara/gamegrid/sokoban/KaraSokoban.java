package kara.gamegrid.sokoban;

import java.util.List;

import kara.gamegrid.Kara;
import kara.gamegrid.Leaf;
import kara.gamegrid.Mushroom;
import kara.gamegrid.Tree;
import ch.aplu.jgamegrid.Actor;

/**
 * KaraSokoban extends the functionality of Kara by adding methods that are used
 * for the Sokoban game
 * 
 * <i>KaraSokoban erweitert die Funktionalitaet von Kara mit Methoden fuer das
 * Sokoban Spiel.</i>
 * 
 * @author Marco Jakob (http://edu.makery.ch)
 */
public abstract class KaraSokoban extends Kara {
	private static final int DIRECTION_RIGHT = 0;
	private static final int DIRECTION_DOWN = 90;
	private static final int DIRECTION_LEFT = 180;
	private static final int DIRECTION_UP = 270;

	/**
	 * Gets the most recently pressed key <br>
	 * <i>Ermittelt die zuletzt gedrueckte Taste</i>.
	 * <p>
	 * 
	 * <ul>
	 * <li>"a", "b", .., "z" (alphabetical keys), "0".."9" (digits), most
	 * punctuation marks. Also returns uppercase characters when appropriate.</li>
	 * <li>"up", "down", "left", "right" (the cursor keys)</li>
	 * <li>"enter", "space", "tab", "escape", "backspace", "shift", "control"</li>
	 * <li>"F1", "F2", .., "F12" (the function keys)</li>
	 * </ul>
	 * 
	 * @return the most recently pressed key as String or an empty String if no
	 *         key was pressed.
	 */
	public String getKey() {
		if (!getGameScreen().isLevelComplete()) {
			return getGameScreen().getKey();
		} else {
			return "";
		}
	}

	/**
	 * Kara turns so that he looks to the right <br>
	 * <i>Kara dreht sich so, dass er nach rechts schaut</i>.
	 */
	public void setDirectionRight() {
		actorDelegate.setDirection(DIRECTION_RIGHT);
	}

	/**
	 * Kara turns so that he looks down <br>
	 * <i>Kara dreht sich so, dass er nach unten schaut</i>.
	 */
	public void setDirectionDown() {
		actorDelegate.setDirection(DIRECTION_DOWN);
	}

	/**
	 * Kara turns so that he looks to the left <br>
	 * <i>Kara dreht sich so, dass er nach links schaut</i>.
	 */
	public void setDirectionLeft() {
		actorDelegate.setDirection(DIRECTION_LEFT);
	}

	/**
	 * Kara turns so that he looks up <br>
	 * <i>Kara dreht sich so, dass er nach oben schaut</i>.
	 */
	public void setDirectionUp() {
		actorDelegate.setDirection(DIRECTION_UP);
	}

	/**
	 * Kara checks if he can push a mushroom <br>
	 * <i>Kara schaut nach, ob er einen Pilz stossen kann</i>.
	 * 
	 * @return true if Kara can push a mushroom, false otherwise
	 */
	public boolean canPushMushroom() {
		// first check if there actually is a mushroom in front of Kara
		if (mushroomFront()) {
			// check if the mushroom can be pushed
			if (getObjectInFront(actorDelegate.getIntDirection(), 2, Tree.class) == null
					&& getObjectInFront(actorDelegate.getIntDirection(), 2, Mushroom.class) == null) {
				return true;
			}
		} else {
			showWarning(
					"Should check if Kara can push a mushroom but there is no mushroom in front of Kara!",
					"Soll testen, ob Kara einen Pilz stossen kann. Kara steht aber gar nicht vor einem Pilz!");
		}

		return false;
	}

	/**
	 * Sets the number of moves the player has made <br>
	 * <i>Setzt die Anzahl Bewegungen, die der Spieler gemacht hat</i>.
	 */
	public void setNumberOfMoves(int moves) {
		getGameScreen().setNumberOfMoves(moves);
	}

	/**
	 * Checks if the level is complete (all mushrooms on a leaf) <br>
	 * <i>Prueft, ob der Level fertig ist (alle Pilze stehen auf einem
	 * Blatt)</i>.
	 * 
	 * @return true if the level is complete, false otherwise
	 */
	public boolean testLevelComplete() {
		List<Actor> actors = getWorld().getActors(Mushroom.class);

		// Test all mushrooms if they are on a leaf
		for (Actor actor : actors) {
			// Must be a mushroom:
			Mushroom m = (Mushroom) actor;
			if (getWorld().getActorsAt(m.getLocation(), Leaf.class)
					.size() == 0) {
				// Mushroom is not on a leaf
				return false;
			}
		}

		// All mushrooms are on a leaf
		return true;
	}

	/**
	 * Marks the current level as complete <br>
	 * <i>Markiert den aktuellen Level als fertig</i>.
	 */
	public void levelComplete() {
		getGameScreen().setLevelComplete(true);
	}

	/**
	 * Adds the entry to the highscore list <br>
	 * <i>Fuegt einen Eintrag in die Highscoreliste ein</i>.
	 */
	public void addHighscoreEntry(int moves) {
		if (!getGameScreen().isHighscoreReadOnly()) {
			Highscore h = getGameScreen().getHighscoreForCurrentLevel();
			if (h == null) {
				showWarning("Highscore is not available!",
						"Die Highscore ist nicht verfuegbar!");
			}

			int place = h.addHighscoreEntry(getGameScreen().getPlayerName(),
					moves);
			if (place == -1) {
				// entry is not in the highscore
				showWarning(
						"The highscore entry is not in the top 3. You can't add it!",
						"Der Eintrag in die Highscore ist nicht in den Top 3. Er kann nicht hinzugefuegt werden!");
				return;
			} else {
				getGameScreen().setHighscore(h);
			}
		}
	}

	/**
	 * Returns if the number of moves would be in the top 3 of the highscore
	 * list <br>
	 * <i>Ermittelt, ob die Anzahl Bewegungen fuer die Top 3 in der Highscore
	 * reichen wuerden</i>.
	 */
	public boolean isHighscoreTop3(int moves) {
		Highscore h = getGameScreen().getHighscoreForCurrentLevel();
		if (h == null) {
			return false;
		}
		return h.isHighscoreTop3(moves);
	}

	/*-------------------- privat methods -----------------------*/

	/**
	 * Returns the game screen.
	 */
	private GameScreen getGameScreen() {
		return (GameScreen) getWorld();
	}
}
