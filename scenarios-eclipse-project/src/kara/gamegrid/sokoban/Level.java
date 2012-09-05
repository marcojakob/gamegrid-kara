package kara.gamegrid.sokoban;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kara.gamegrid.world.GameScreen;
import kara.gamegrid.world.WorldSetup;

import ch.aplu.jgamegrid.Actor;

/**
 * The Level is a {@link WorldSetup} with the following structure:
 * <p>
 * Each level must start with the Word 'Level:' and the corresponding
 * level-number. This is followed by a line starting with 'Password:' and the
 * password. <br>
 * <p>
 * After the level number and the password, the actor positions are described
 * (see {@link WorldSetup}).
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class Level extends WorldSetup {
	public static final String TITLE_KEY = "Level:";
	public static final String PASSWORD_KEY = "Password:";
	private int levelNumber = -1;

	/**
	 * Constructor for a Level that copies the specified {@link WorldSetup} to
	 * create a new Level.
	 * 
	 * @param worldSetup
	 *            copies the specified world setup.
	 * @param levelNumber
	 *            the level number
	 */
	public Level(WorldSetup worldSetup, int levelNumber) {
		super(worldSetup);
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
	 * @param levelFile
	 *            The filename of level file, possibly relative to the clazz.
	 * @param clazz
	 *            The class used to get the relative path to the file or
	 *            <code>null</code> if the file should be retrieved relative to
	 *            the jar root or project root.
	 * @return the levels as an array
	 */
	public static Level[] parseFromFile(String levelFile, Class<?> clazz) {
		WorldSetup[] worldSetups = WorldSetup.parseFromFile(levelFile, clazz, TITLE_KEY, 
				GameScreen.WIDTH_IN_CELLS, GameScreen.HEIGHT_IN_CELLS, PASSWORD_KEY);
		Level[] levels = new Level[worldSetups.length];
		
		for (int i = 0; i < worldSetups.length; i++) {
			levels[i] = new Level(worldSetups[i], i+1);
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
		WorldSetup worldSetup = WorldSetup.createFromActors(actors,
				GameScreen.WIDTH_IN_CELLS, GameScreen.HEIGHT_IN_CELLS,
				TITLE_KEY, Integer.toString(levelNumber), attributes);
		
		return new Level(worldSetup, levelNumber);
	}
}
