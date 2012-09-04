package ch.gamegrid.kara.sokoban;

import ch.gamegrid.kara.world.GameScreen;

/**
 * The ScreenState is an abstract class for the screens in the game.
 * <p>
 * The class structure is made according to the state design pattern.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public abstract class ScreenState {
	protected GameScreen gameScreen;

	/**
	 * Create a ScreenState.
	 * 
	 * @param gameScreen
	 *            the game screen that uses this state.
	 */
	public ScreenState(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	/**
	 * Initializes the screen.
	 */
	public abstract void initScreen();

	/**
	 * The act method is called by the GameScreen to perform the action in the
	 * ScreenState.
	 */
	public abstract void act();
}
