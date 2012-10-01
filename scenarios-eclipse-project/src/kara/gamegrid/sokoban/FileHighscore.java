package kara.gamegrid.sokoban;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Saves the highscores to a local file.
 * 
 * @author Marco Jakob (http://edu.makery.ch)
 */
public class FileHighscore extends HighscoreManager {
	// The local file containing the highscores
	private static final String HIGHSCORE_FILE = "HIGHSCORES.txt";

	private String currentPlayerName = "";

	/**
	 * The highscore map with the level number as a key and the Highscore as a
	 * value.
	 */
	private Map<Integer, Highscore> highscores;

	/**
	 * Constructor for objects of class FileHighscore
	 */
	public FileHighscore() {
	}

	/**
	 * Returns true if the FileHighscore storage is available in this
	 * environment.
	 */
	public static boolean isAvailable() {
		// Try writing a test file
		try {
			File file = new File("temp");

			file.createNewFile();
			file.delete();

			// Successfully created and deleted a file
			return true;
		} catch (Exception e) {
			// Could not write and delete a file
			return false;
		}
	}

	/**
	 * Initially loads the highscore from the file.
	 */
	public void initHighscores() {
		highscores = parseHighscoresFromFile();
	}

	/**
	 * Returns it the manager is read only.
	 */
	public boolean isReadOnly() {
		// never read only
		return false;
	}

	/**
	 * Returns the name of the current player.
	 */
	public String getCurrentPlayerName() {
		return currentPlayerName;
	}

	/**
	 * Set the name of the player.
	 */
	public void setCurrentPlayerName(String currentPlayerName) {
		this.currentPlayerName = currentPlayerName;
	}

	/**
	 * Returns the Highscore for the specified level. The returned Highscore is
	 * a clone. To store a change in the highscore, the method setHighscore(...)
	 * must be called.
	 */
	public Highscore getHighscoreForLevel(int levelNumber) {
		if (highscores == null) {
			return null;
		}

		Highscore h = highscores.get(levelNumber);
		if (h == null) {
			return new Highscore(levelNumber);
		}
		return h.clone();
	}

	/**
	 * Sets the specified Highscore and stores it to the file.
	 */
	public void setHighscore(Highscore highscore) {
		highscores.put(highscore.getLevelNumber(), highscore);
		writeHighscoresToFile(highscores);
	}

	/**
	 * Parses all the Highscores from the specified Level File.
	 * 
	 * @return the highscores as a HashMap with the level number as a key.
	 */
	private Map<Integer, Highscore> parseHighscoresFromFile() {
		Map<Integer, Highscore> result = new HashMap<Integer, Highscore>();

		try {
			File f = new File(HIGHSCORE_FILE);
			if (!f.exists()) {
				f.createNewFile();
			}

			FileReader fr = new FileReader(f);
			BufferedReader input = new BufferedReader(fr);

			String line;

			try {
				while ((line = input.readLine()) != null) {
					if (line.startsWith("Level:")) {
						String[] strs = line.split(";");
						if (strs.length == 7) {
							int levelNumber = Integer.parseInt(strs[0]
									.substring("Level:".length()).trim());
							Highscore highscore = new Highscore(levelNumber);
							highscore.addHighscoreEntry(strs[1],
									Integer.parseInt(strs[2]));
							highscore.addHighscoreEntry(strs[3],
									Integer.parseInt(strs[4]));
							highscore.addHighscoreEntry(strs[5],
									Integer.parseInt(strs[6]));
							result.put(levelNumber, highscore);
						}
					}
				}
			} finally {
				input.close();
			}
		} catch (Exception ex) {
			System.out.println("ERROR: Could not load highscore from file: "
					+ HIGHSCORE_FILE);
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * Writes the highscores to the specified file.
	 */
	public void writeHighscoresToFile(Map<Integer, Highscore> highscores) {
		try {
			FileWriter fstream = new FileWriter(HIGHSCORE_FILE);
			BufferedWriter out = new BufferedWriter(fstream);

			Set<Integer> keys = highscores.keySet();
			for (int key : keys) {
				out.write(toHighscoreFileString(highscores.get(key)));
				out.newLine();
			}

			out.flush();
			// Close the output stream
			out.close();
		} catch (Exception ex) {
			System.out.println("ERROR: Could not save highscore to file: "
					+ HIGHSCORE_FILE);
			ex.printStackTrace();
		}
	}

	/**
	 * Returns a String representation of the highscore. This String can be used
	 * to save into a text file.
	 */
	private String toHighscoreFileString(Highscore highscore) {
		StringBuffer buf = new StringBuffer();
		buf.append("Level:" + highscore.getLevelNumber());
		buf.append(";" + highscore.getFirstEntry().getName() + ";"
				+ highscore.getFirstEntry().getMoves());
		buf.append(";" + highscore.getSecondEntry().getName() + ";"
				+ highscore.getSecondEntry().getMoves());
		buf.append(";" + highscore.getThirdEntry().getName() + ";"
				+ highscore.getThirdEntry().getMoves());
		return buf.toString();
	}
}
