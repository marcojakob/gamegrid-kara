package ch.gamegrid.kara.sokoban;

import java.awt.Color;

import ch.gamegrid.kara.world.GameScreen;
import ch.gamegrid.kara.world.WorldImages;

/**
 * The start screen state.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class LevelSplashState extends ScreenState {
	private Label levelAnimLabel;
	private Label levelPasswordAnimLabel;
	private Button startLevelButton;
	private Button backToMenuButton;

	public LevelSplashState(GameScreen gameScreen) {
		super(gameScreen);
	}

	/**
	 * Initializes the screen.
	 */
	public void initScreen() {
		gameScreen.createBlackBackground();

		levelAnimLabel = new Label(gameScreen, "Level "
				+ gameScreen.getCurrentLevelNumber(), 350, 50,
				GameScreen.FONT_XXL);
		levelAnimLabel.setTextColor(new Color(255, 205, 205));
		levelAnimLabel.setBackgroundColor(Color.BLACK);
		gameScreen.addObject(levelAnimLabel, GameScreen.WIDTH_IN_CELLS / 2, 6);

		levelPasswordAnimLabel = new Label(gameScreen, "Password: "
				+ getLevelPassword(), 350, 25,
				GameScreen.FONT_L);
		levelPasswordAnimLabel.setTextColor(new Color(255, 205, 205));
		levelPasswordAnimLabel.setBackgroundColor(Color.BLACK);
		gameScreen.addObject(levelPasswordAnimLabel,
				GameScreen.WIDTH_IN_CELLS / 2, 9);

		startLevelButton = new Button(gameScreen, "Start Level", 130, 30,
				GameScreen.FONT_M);
		startLevelButton.setBorderColor(Color.RED);
		startLevelButton.setIcon(WorldImages.ICON_START);
		startLevelButton.setBackgroundColor(new Color(255, 205, 205));
		gameScreen.addObject(startLevelButton, GameScreen.WIDTH_IN_CELLS / 2,
				12);

		backToMenuButton = new Button(gameScreen, "Main Menu", 130, 30,
				GameScreen.FONT_M);
		backToMenuButton.setBorderColor(Color.RED);
		backToMenuButton.setIcon(WorldImages.ICON_HOME);
		backToMenuButton.setBackgroundColor(new Color(255, 205, 205));
		gameScreen.addObject(backToMenuButton, GameScreen.WIDTH_IN_CELLS / 2,
				14);
	}
	
	/**
	 * Returns the level password of the current level. If there is no level or
	 * no password, an empty String is returned.
	 * @return
	 */
	private String getLevelPassword() {
		if (gameScreen.getCurrentLevel() != null) {
			String password = gameScreen.getCurrentLevel().getLevelPassword();
			if (password != null) {
				return password;
			}
		}
		return "";
	}

	/**
	 * The act method is called by the GameScreen to perform the action in the
	 * ScreenState.
	 */
	public void act() {
		String key = gameScreen.getKey();
		if (key.equals("enter")) {
			// enter clicked
			gameScreen.setState(gameScreen.getGameState());
		}

		if (startLevelButton.wasClicked()) {
			gameScreen.setState(gameScreen.getGameState());
		} else if (backToMenuButton.wasClicked()) {
			// go to start state
			gameScreen.setState(gameScreen.getStartState());
		}
	}
}
