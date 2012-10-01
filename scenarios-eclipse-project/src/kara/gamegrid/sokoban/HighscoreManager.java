package kara.gamegrid.sokoban;

/**
 * The HighscoreManager is responsible for loading and storing Highscore.
 * 
 * @author Marco Jakob (http://edu.makery.ch)
 */
public abstract class HighscoreManager {

	/**
	 * Constructor.
	 */
	public HighscoreManager() {
	}

	/**
	 * Initially loads the highscore.
	 */
	public abstract void initHighscores();

	/**
	 * Returns it the manager is read only.
	 */
	public abstract boolean isReadOnly();

	/**
	 * Returns the name of the current player.
	 */
	public abstract String getCurrentPlayerName();

	/**
	 * Set the name of the current player.
	 */
	public abstract void setCurrentPlayerName(String currentPlayerName);

	/**
	 * Returns the Highscore for the specified level. The returned Highscore is
	 * a clone. To store a change in the highscore, the method setHighscore(...)
	 * must be called.
	 */
	public abstract Highscore getHighscoreForLevel(int levelNumber);

	/**
	 * Sets the specified Highscore and stores it.
	 */
	public abstract void setHighscore(Highscore highscore);
}
