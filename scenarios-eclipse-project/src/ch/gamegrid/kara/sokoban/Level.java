package ch.gamegrid.kara.sokoban;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch.aplu.jgamegrid.Actor;
import ch.gamegrid.kara.world.GameScreen;
import ch.gamegrid.kara.world.Scenario;

/**
 * The Level is a Scenario with the following structure:
 * <p>
 * Each level must start with the Word 'Level:' and the corresponding
 * level-number. This is followed by a line starting with 'Password:' and the
 * password. <br>
 * <p>
 * After the level number and the password, the actor positions are described
 * (see {@link Scenario}).
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class Level extends Scenario {
	public static final String TITLE_KEY = "Level:";
	public static final String PASSWORD_KEY = "Password:";
	private int levelNumber = -1;

	/**
	 * Constructor for a Level that copies the specified Scenario to create
	 * a new Level.
	 * 
	 * @param scenario copies the specified scenario.
	 * @param levelNumber
	 */
	public Level(Scenario scenario, int levelNumber) {
		super(scenario);
		this.levelNumber = levelNumber;
	}
	
	/**
	 * Returns the level number.
	 */
	public int getLevelNumber() {
		return levelNumber;
	}

	/**
	 * Returns the level password.
	 */
	public String getLevelPassword() {
		return getAttribute(PASSWORD_KEY);
	}

	/**
	 * Parses all the Levels from the specified Level File.
	 * 
	 * @return the levels as an array
	 */
	public static Level[] parseFromFile(String levelFile) {
		Scenario[] scenarios = Scenario.parseFromFile(levelFile, TITLE_KEY, 
				GameScreen.WIDTH_IN_CELLS, GameScreen.HEIGHT_IN_CELLS, PASSWORD_KEY);
		Level[] levels = new Level[scenarios.length];
		
		for (int i = 0; i < scenarios.length; i++) {
			levels[i] = new Level(scenarios[i], i+1);
		}

		return levels;
	}

	/**
	 * Creates a Level from all the actors in the list.
	 * 
	 * @return the level
	 */
	public static Level createFromActors(List<Actor> actors,
			int levelNumber, String password) {
		Map<String, String> attributes = new LinkedHashMap<>();
		attributes.put(PASSWORD_KEY, password);
		Scenario scenario = Scenario.createFromActors(actors,
				GameScreen.WIDTH_IN_CELLS, GameScreen.HEIGHT_IN_CELLS,
				TITLE_KEY, Integer.toString(levelNumber), attributes);
		
		return new Level(scenario, levelNumber);
	}
}
