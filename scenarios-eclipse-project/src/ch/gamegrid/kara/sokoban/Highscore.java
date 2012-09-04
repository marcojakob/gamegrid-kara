package ch.gamegrid.kara.sokoban;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class is used to manage (read and write) the highscore.
 * 
 * Every Highscore contains three highscore entries for one specific level.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class Highscore {
	public static final Entry EMPTY_ENTRY = new Entry("---", 0);

	private int levelNumber;

	/** A sorted list with at most three entries. */
	private List<Entry> entries;

	/**
	 * Constructor.
	 * 
	 * @param levelNumber
	 *            the level number that this Highscore is for
	 */
	public Highscore(int levelNumber) {
		this.levelNumber = levelNumber;
		this.entries = new ArrayList<Entry>();
	}

	/**
	 * Returns the level number of this Highscore.
	 */
	public int getLevelNumber() {
		return levelNumber;
	}

	/**
	 * Returns if the number of moves would be in the top 3 of the highscore
	 * list.
	 */
	public boolean isHighscoreTop3(int moves) {
		if (entries.size() < 3) {
			return true;
		} else {
			// compare with moves of third (last) entry
			return isBetterThan(moves, entries.get(2).getMoves());
		}
	}

	/**
	 * Adds the specified entry into the highscore. Only the top 3 entries are
	 * stored (i.e. the entries with the fewest moves). The new place in the
	 * highscore is returned (1, 2 or 3). If the highscore is not in the top 3
	 * it is ignored and -1 is returned.
	 * <p>
	 * Note: Every player can only be on one place in the highscore. If the same
	 * user is added with less moves, his place might be updated.
	 * 
	 * @param playerName
	 *            the name of the player
	 * @param moves
	 *            the number of moves
	 * @return 1, 2 or 3 for the new place of the entry in the highscore list.
	 *         If the highscore is not in the first three -1 is returned.
	 */
	public int addHighscoreEntry(String playerName, int moves) {
		// Test if better than third place
		if (entries.size() < 3
				|| isBetterThan(moves, entries.get(2).getMoves())) {
			// is in top 3 --> add
			entries.add(new Entry(playerName, moves));
		} else {
			// not in top 3
			return -1;
		}

		// sort
		Collections.sort(entries);

		// go through the first 3 entries and check for place
		// if the same user is now two times in top three, remove the lower one
		int place = -1;
		int duplicateIndexToRemove = -1;
		for (int i = 0; i < entries.size() && i < 3; i++) {
			if (playerName.equals(entries.get(i).getName())) {
				if (place != -1) {
					// user is alredy in a better place --> delete this one
					duplicateIndexToRemove = i;
				} else {
					place = i + 1;
				}
			}
		}

		if (duplicateIndexToRemove != -1) {
			entries.remove(duplicateIndexToRemove);
		}

		// only keep top 3 in the list
		while (entries.size() > 3) {
			entries.remove(3);
		}

		return place + 1;
	}

	/**
	 * Adds the specified entry into the highscore. Only the top 3 entries are
	 * stored (i.e. the entries with the fewest moves). The new place in the
	 * highscore is returned (1, 2 or 3). If the highscore is not in the top 3
	 * it is ignored and -1 is returned.
	 * <p>
	 * Note: Every player can only be on one place in the highscore. If the same
	 * user is added with less moves, his place might be updated.
	 * 
	 * @param entry
	 *            the entry.
	 * @return 1, 2 or 3 for the new place of the entry in the highscore list.
	 *         If the highscore is not in the first three -1 is returned.
	 */
	public int addHighscoreEntry(Entry entry) {
		return addHighscoreEntry(entry.getName(), entry.getMoves());
	}

	/**
	 * Returns the first Entry in the highscore list or a DUMMY if there is no
	 * such entry. The entry is a clone to prevent change to this highscore.
	 */
	public Entry getFirstEntry() {
		if (entries.size() > 0) {
			return entries.get(0).clone();
		}
		return EMPTY_ENTRY;
	}

	/**
	 * Returns the second Entry in the highscore list or a DUMMY if there is no
	 * such entry.
	 */
	public Entry getSecondEntry() {
		if (entries.size() > 1) {
			return entries.get(1).clone();
		}
		return EMPTY_ENTRY;
	}

	/**
	 * Returns the third Entry in the highscore list or a DUMMY if there is no
	 * such entry. The entry is a clone to prevent change to this highscore.
	 */
	public Entry getThirdEntry() {
		if (entries.size() > 2) {
			return entries.get(2).clone();
		}
		return EMPTY_ENTRY;
	}

	/**
	 * Returns the place of the player in the highscore or -1, if not in
	 * highscore.
	 * 
	 * @return 1, 2 or 3 for the place of the player in the highscore list. If
	 *         the player is not in the first three -1 is returned.
	 */
	public int getPlayerPlace(String playerName) {
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getName().equals(playerName)) {
				return i + 1;
			}
		}

		// not in top 3
		return -1;
	}

	public Highscore clone() {
		Highscore h = new Highscore(levelNumber);
		h.addHighscoreEntry(getFirstEntry());
		h.addHighscoreEntry(getSecondEntry());
		h.addHighscoreEntry(getThirdEntry());
		return h;
	}

	/**
	 * Returns true if movesA is better than movesB.
	 */
	public static boolean isBetterThan(int movesA, int movesB) {
		// less than 1 indicates that it is not a real entry --> they are always
		// considered as beeing worse
		if (movesA < 1 && movesB >= 1) {
			return false;
		} else if (movesA >= 1 && movesB < 1) {
			return true;
		} else {
			return movesA < movesB;
		}
	}

	/**
	 * Inner class for a highscore entry with name of the player and number of
	 * moves. The Highscore.Entry implements Comparable so that it can be sorted
	 * by number of moves.
	 */
	public static class Entry implements Comparable<Entry> {
		private String name;
		private int moves;

		public Entry(String name, int moves) {
			this.name = name;
			this.moves = moves;
		}

		public String getName() {
			return name;
		}

		public int getMoves() {
			return moves;
		}

		public String toString() {
			if (moves < 1) {
				return "---";
			} else {
				return name + " (" + moves + ")";
			}
		}

		public int compareTo(Entry other) {
			if (Highscore.isBetterThan(other.getMoves(), this.moves)) {
				return 1;
			} else {
				return -1;
			}
		}

		public Entry clone() {
			return new Entry(name, moves);
		}
	}
}
