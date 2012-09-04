package ch.gamegrid.kara.sokoban;

import java.awt.Color;

import ch.gamegrid.kara.world.GameScreen;
import ch.gamegrid.kara.world.WorldImages;

/**
 * The start screen state.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class StartState extends ScreenState {
	private Button startGameButton;
	private Button highscoreButton;
	private InputLabel passwordInputLabel;
	private Label passwordOkLabel;
	private int passwordLevelNumber = -1;

	public StartState(GameScreen gameScreen) {
		super(gameScreen);
	}

	/**
	 * Initializes the screen.
	 */
	public void initScreen() {
		gameScreen.setCurrentLevelNumber(1); // reset level

		gameScreen.createBlackBackground();
		gameScreen.setBgImagePath(WorldImages.ICON_START_SCREEN_PATH);

		if (gameScreen.getCurrentLevel() != null) {
			startGameButton = new Button(gameScreen, "Start Game", 130, 30,
					GameScreen.FONT_M);
			startGameButton.setBorderColor(Color.RED);
			startGameButton.setIcon(WorldImages.ICON_START);
			startGameButton.setBackgroundColor(new Color(255, 205, 205));
			gameScreen.addObject(startGameButton, GameScreen.WIDTH_IN_CELLS / 2, 10);
			
			Label enterPasswordLabel = new Label(gameScreen,
					"Enter Level Password With Keyboard:", 230, 21,
					GameScreen.FONT_S);
			enterPasswordLabel.setBackgroundColor(Color.BLACK);
			enterPasswordLabel.setTextColor(new Color(255, 205, 205));
			gameScreen.addObject(enterPasswordLabel, GameScreen.WIDTH_IN_CELLS / 2,
					14);
			
			passwordInputLabel = new InputLabel(gameScreen, "", 180, 21,
					GameScreen.FONT_M);
			passwordInputLabel.setBorderColor(null);
			passwordInputLabel.setBackgroundColor(new Color(255, 205, 205));
			// passwordInputLabel.setMaxLength(10);
			gameScreen.addObject(passwordInputLabel, GameScreen.WIDTH_IN_CELLS / 2,
					15);
			
			passwordOkLabel = new Label(gameScreen, 95, 24, WorldImages.ICON_LOCKED);
			passwordOkLabel.setBackgroundTransparency(0);
			passwordOkLabel.setTextColor(new Color(255, 205, 205));
			passwordOkLabel.setFont(GameScreen.FONT_S);
			passwordOkLabel.setIconVisible(true);
			passwordOkLabel.setAlign(Label.ALIGN_LEFT);
			gameScreen.addObject(passwordOkLabel,
					GameScreen.WIDTH_IN_CELLS / 2 + 5, 15);
			
			if (gameScreen.isHighscoreEnabled()) {
				highscoreButton = new Button(gameScreen, "Highscore", 130, 30,
						GameScreen.FONT_M);
				highscoreButton.setBorderColor(Color.RED);
				highscoreButton.setIcon(WorldImages.ICON_HIGHSCORE);
				highscoreButton.setBackgroundColor(new Color(255, 205, 205));
				gameScreen.addObject(highscoreButton,
						GameScreen.WIDTH_IN_CELLS / 2, 12);
			}
			
		} else {
			// levels could not be loaded...
			Label noLevelsLabel1 = new Label(gameScreen,
					"No Levels!", 230, 21,
					GameScreen.FONT_L);
			noLevelsLabel1.setBackgroundColor(Color.BLACK);
			noLevelsLabel1.setTextColor(new Color(255, 205, 205));
			gameScreen.addObject(noLevelsLabel1, GameScreen.WIDTH_IN_CELLS / 2, 10);
			
			Label noLevelsLabel2 = new Label(gameScreen,
					"check your levels file or start in development mode", 330, 21,
					GameScreen.FONT_S);
			noLevelsLabel2.setBackgroundColor(Color.BLACK);
			noLevelsLabel2.setTextColor(new Color(255, 205, 205));
			gameScreen.addObject(noLevelsLabel2, GameScreen.WIDTH_IN_CELLS / 2, 11);
		}
		
	}

	/**
	 * The act method is called by the GameScreen to perform the action in the
	 * ScreenState.
	 */
	public void act() {
		if (passwordInputLabel != null) {
			// handle key events
			String key = gameScreen.getKey();
			if (!key.isEmpty()) {
				handleKeyPress(key);
			}
		}

		// handle mouse events
		if (startGameButton != null && startGameButton.wasClicked()) {
			handleStartGame();
		}

		if (highscoreButton != null && highscoreButton.wasClicked()) {
			// Set the new screen state
			gameScreen.setState(gameScreen.getHighscoreState());
		}
	}
	
	/**
	 * Handles the key press event.
	 */
	private void handleKeyPress(String key) {
		if (key.equals("enter")) {
			handleStartGame();
		} else {
			passwordInputLabel.handleKeyPress(key);
			if (passwordInputLabel.wasTextChanged()) {
				String password = passwordInputLabel.getText();
				if (password.length() > 0) {
					checkPassword(password);
				}
			}
		}
	}

	/**
	 * Tests whether the specified password matches to a level password. If a
	 * match is found, the according level number is saved.
	 */
	private void checkPassword(String password) {
		passwordLevelNumber = -1;
		for (Level l : gameScreen.getAllLevels()) {
			if (l.getLevelPassword().equals(password)) {
				passwordOkLabel.setIcon(WorldImages.ICON_OK);
				passwordOkLabel.setText("Level " + l.getLevelNumber());
				passwordLevelNumber = l.getLevelNumber();
				break;
			}
		}

		if (passwordLevelNumber == -1) {
			passwordOkLabel.setIcon(WorldImages.ICON_LOCKED);
			passwordOkLabel.setText("");
		}
	}

	/**
	 * Handles the start game event.
	 */
	private void handleStartGame() {
		if (passwordLevelNumber != -1) {
			gameScreen.setCurrentLevelNumber(passwordLevelNumber);
			passwordLevelNumber = -1; // reset
		}

		// Set the new screen state
		if (gameScreen.isHighscoreAvailable() && gameScreen.canSetPlayerName()) {
			gameScreen.setState(gameScreen.getEnterNameState());
		} else {
			gameScreen.setState(gameScreen.getLevelSplashState());
		}
	}
}
