package kara.gamegrid.sokoban;

import java.awt.Color;

import kara.gamegrid.WorldImages;


/**
 * The enter name screen state.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class EnterNameState extends ScreenState  {
	private Button startGameButton;
	private InputLabel nameInputLabel;
	private boolean validName = false;

	public EnterNameState(GameScreen gameScreen) {
		super(gameScreen);
	}

	/**
	 * Initializes the screen.
	 */
	public void initScreen() {
		gameScreen.createBlackBackground();

		Label enterNameLabel = new Label(gameScreen, "Enter your Name:", 230, 21,
				GameScreen.FONT_S);
		enterNameLabel.setBackgroundColor(Color.BLACK);
		enterNameLabel.setTextColor(new Color(255, 205, 205));
		gameScreen.addObject(enterNameLabel, GameScreen.WIDTH_IN_CELLS / 2, 9);

		nameInputLabel = new InputLabel(gameScreen, gameScreen.getPlayerName(), 200, 21,
				GameScreen.FONT_M);
		nameInputLabel.setBorderColor(null);
		nameInputLabel.setBackgroundColor(new Color(255, 205, 205));
		nameInputLabel.setMaxLength(15);
		gameScreen.addObject(nameInputLabel, GameScreen.WIDTH_IN_CELLS / 2, 10);

		startGameButton = new Button(gameScreen, "OK", 130, 30, GameScreen.FONT_M);
		startGameButton.setBorderColor(Color.RED);
		startGameButton.setIcon(WorldImages.ICON_START);
		startGameButton.setBackgroundColor(new Color(255, 205, 205));
		gameScreen.addObject(startGameButton, GameScreen.WIDTH_IN_CELLS / 2, 12);

		// Check if valid input
		checkValidName(nameInputLabel.getText());
	}

	/**
	 * The act method is called by the GameScreen to perform the action in the
	 * ScreenState.
	 */
	public void act() {
		// handle key events
		String key = gameScreen.getKey();
		if (!key.isEmpty()) {
			handleKeyPress(key);
		}

		// handle mouse events
		if (startGameButton.wasClicked()) {
			handleStartGame();
		}
	}

	/**
	 * Handles the key press event.
	 */
	private void handleKeyPress(String key) {
		if (key.equals("enter") && validName) {
			handleStartGame();
		} else {
			nameInputLabel.handleKeyPress(key);
			if (nameInputLabel.wasTextChanged()) {
				checkValidName(nameInputLabel.getText());
			}
		}
	}

	private void checkValidName(String name) {
		if (name.trim().isEmpty()) {
			// not valid
			validName = false;
		} else {
			validName = true;
		}
		startGameButton.setVisible(validName);
	}

	/**
	 * Handles the start game event.
	 */
	private void handleStartGame() {
		String name = nameInputLabel.getText();
		if (name != null) {
			gameScreen.setPlayerName(name.replaceAll(";", " ").trim());
		} else {
			gameScreen.setPlayerName("");
		}

		// Set the new screen state
		gameScreen.setState(gameScreen.getLevelSplashState());
	}
}
