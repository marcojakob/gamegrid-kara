package kara.gamegrid.sokoban;

import java.awt.Color;

import kara.gamegrid.WorldImages;


/**
 * The game complete screen state.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class GameCompleteState extends ScreenState {
	private Label trophyLabel;
	private Label congratulationsLabel;
	private Label youCompletetLabel;
	private Button backToMenuButton;

	public GameCompleteState(GameScreen gameScreen) {
		super(gameScreen);
	}

	/**
	 * Initializes the screen.
	 */
	public void initScreen() {
		gameScreen.createBlackBackground();

		trophyLabel = new Label(gameScreen, 256, 256, WorldImages.ICON_TROPHY);
		trophyLabel.setBackgroundColor(Color.BLACK);
		gameScreen.addObject(trophyLabel, GameScreen.WIDTH_IN_CELLS / 2, 6);

		congratulationsLabel = new Label(gameScreen, "Congratulations!", 500,
				70, GameScreen.FONT_XXL);
		congratulationsLabel.setTextColor(new Color(255, 205, 205));
		congratulationsLabel.setBackgroundColor(Color.BLACK);
		gameScreen.addObject(congratulationsLabel,
				GameScreen.WIDTH_IN_CELLS / 2, 12);

		youCompletetLabel = new Label(gameScreen, "You completed all Levels!",
				350, 50, GameScreen.FONT_L);
		youCompletetLabel.setTextColor(new Color(255, 205, 205));
		youCompletetLabel.setBackgroundColor(Color.BLACK);
		gameScreen.addObject(youCompletetLabel, GameScreen.WIDTH_IN_CELLS / 2,
				14);

		backToMenuButton = new Button(gameScreen, "Main Menu", 130, 30,
				GameScreen.FONT_M);
		backToMenuButton.setBorderColor(Color.RED);
		backToMenuButton.setIcon(WorldImages.ICON_HOME);
		backToMenuButton.setBackgroundColor(new Color(255, 205, 205));
		gameScreen.addObject(backToMenuButton, GameScreen.WIDTH_IN_CELLS / 2,
				16);
	}

	/**
	 * The act method is called by the GameScreen to perform the action in the
	 * ScreenState.
	 */
	public void act() {
		if (backToMenuButton.wasClicked()) {
			// go to start state
			gameScreen.setState(gameScreen.getStartState());
		}
	}
}
