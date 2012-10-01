package kara.gamegrid.sokoban;

import java.awt.Color;
import java.awt.image.BufferedImage;

import kara.gamegrid.KaraWorld;
import kara.gamegrid.WorldImages;

import ch.aplu.jgamegrid.GGImage;

/**
 * The highscore screen state.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class HighscoreState extends ScreenState {
	private Button arrowRightButton;
	private Button arrowLeftButton;

	private int currentHighscoreLevel = 1;

	private Button backToMenuButton;

	public HighscoreState(GameScreen gameScreen) {
		super(gameScreen);
	}

	/**
	 * Initializes the screen.
	 */
	public void initScreen() {
		gameScreen.createBlackBackground();

		Label highscoreTitleLabel = new Label(gameScreen, "Highscore Level "
				+ currentHighscoreLevel, 500, 70, GameScreen.FONT_XL);
		highscoreTitleLabel.setTextColor(new Color(255, 205, 205));
		highscoreTitleLabel.setBackgroundColor(Color.BLACK);
		gameScreen.addObject(highscoreTitleLabel,
				GameScreen.WIDTH_IN_CELLS / 2, 1);

		arrowRightButton = new Button(gameScreen, 26, 27,
				WorldImages.ICON_ARROW_RIGHT);
		arrowRightButton.setBackgroundColor(Color.BLACK);
		arrowRightButton.setBorderColor(Color.RED);
		arrowRightButton.setInset(0);
		arrowRightButton.setAlign(Label.ALIGN_LEFT);
		arrowRightButton.setBackgroundColor(new Color(255, 205, 205));
		gameScreen.addObject(arrowRightButton,
				GameScreen.WIDTH_IN_CELLS / 2 + 6, 1);

		arrowLeftButton = new Button(gameScreen, 26, 27,
				WorldImages.ICON_ARROW_LEFT);
		arrowLeftButton.setBackgroundColor(Color.BLACK);
		arrowLeftButton.setBorderColor(Color.RED);
		arrowLeftButton.setInset(0);
		arrowLeftButton.setAlign(Label.ALIGN_LEFT);
		arrowLeftButton.setBackgroundColor(new Color(255, 205, 205));
		gameScreen.addObject(arrowLeftButton,
				GameScreen.WIDTH_IN_CELLS / 2 - 6, 1);
		
		backToMenuButton = new Button(gameScreen, "Main Menu", 130, 30,
				GameScreen.FONT_M);
		backToMenuButton.setBorderColor(Color.RED);
		backToMenuButton.setIcon(WorldImages.ICON_HOME);
		backToMenuButton.setBackgroundColor(new Color(255, 205, 205));
		gameScreen.addObject(backToMenuButton, GameScreen.WIDTH_IN_CELLS / 2,
				16);

		BufferedImage levelImg = gameScreen.getLevel(currentHighscoreLevel)
				.toImage(KaraWorld.CELL_SIZE);
		int width = 420;
		int height = 360;
		levelImg = GGImage.scale(levelImg, width, height);
		Label highscoreLevelLabel = new Label(gameScreen, width, height,
				levelImg);
		highscoreLevelLabel.setBorderColor(null);
		highscoreLevelLabel.setBackgroundColor(Color.BLACK);
		gameScreen.addObject(highscoreLevelLabel,
				GameScreen.WIDTH_IN_CELLS / 2, GameScreen.HEIGHT_IN_CELLS / 2);
		
		if (gameScreen.isHighscoreAvailable()) {
			// Get the highscore entries
			Highscore highscore = gameScreen
					.getHighscoreForLevel(currentHighscoreLevel);
			String first = highscore.getFirstEntry().toString();
			String second = highscore.getSecondEntry().toString();
			String third = highscore.getThirdEntry().toString();

			Label highscoreGoldLabel = new Label(gameScreen, first, 400, 40,
					GameScreen.FONT_L);
			highscoreGoldLabel.setTextColor(new Color(255, 205, 205));
			highscoreGoldLabel.setBackgroundColor(Color.BLACK);
			highscoreGoldLabel.setBackgroundTransparency(150);
			highscoreGoldLabel.setBorderColor(null);
			highscoreGoldLabel.setIcon(WorldImages.ICON_GOLD);
			gameScreen.addObject(highscoreGoldLabel,
					GameScreen.WIDTH_IN_CELLS / 2, 10);

			Label highscoreSilverLabel = new Label(gameScreen, second, 400, 40,
					GameScreen.FONT_L);
			highscoreSilverLabel.setTextColor(new Color(255, 205, 205));
			highscoreSilverLabel.setBackgroundColor(Color.BLACK);
			highscoreSilverLabel.setBackgroundTransparency(150);
			highscoreSilverLabel.setBorderColor(null);
			highscoreSilverLabel.setIcon(WorldImages.ICON_SILVER);
			gameScreen.addObject(highscoreSilverLabel,
					GameScreen.WIDTH_IN_CELLS / 2, 12);

			Label highscoreBronzeLabel = new Label(gameScreen, third, 400, 40,
					GameScreen.FONT_L);
			highscoreBronzeLabel.setTextColor(new Color(255, 205, 205));
			highscoreBronzeLabel.setBackgroundColor(Color.BLACK);
			highscoreBronzeLabel.setBackgroundTransparency(150);
			highscoreBronzeLabel.setBorderColor(null);
			highscoreBronzeLabel.setIcon(WorldImages.ICON_BRONZE);
			gameScreen.addObject(highscoreBronzeLabel,
					GameScreen.WIDTH_IN_CELLS / 2, 14);
		} else {
			// Highscore is not available --> give a hint to the user
			Label highscoreNa1 = new Label(gameScreen,
					"Highscore is not available!", 400, 40, GameScreen.FONT_XL);
			highscoreNa1.setTextColor(new Color(255, 205, 205));
			highscoreNa1.setBackgroundColor(Color.BLACK);
			highscoreNa1.setBackgroundTransparency(150);
			highscoreNa1.setBorderColor(null);
			gameScreen.addObject(highscoreNa1, GameScreen.WIDTH_IN_CELLS / 2,
					13);
		}
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

		String key = gameScreen.getKey();

		if (arrowLeftButton.wasClicked() || (key != null && key.equals("left"))) {
			currentHighscoreLevel--;
			if (currentHighscoreLevel < 1) {
				currentHighscoreLevel = gameScreen.getNumberOfLevels();
			}
			// reload this state
			gameScreen.setState(gameScreen.getHighscoreState());
		}

		if (arrowRightButton.wasClicked()
				|| (key != null && key.equals("right"))) {
			currentHighscoreLevel++;
			if (currentHighscoreLevel > gameScreen.getNumberOfLevels()) {
				currentHighscoreLevel = 1;
			}
			// reload this state
			gameScreen.setState(gameScreen.getHighscoreState());
		}
	}
}
