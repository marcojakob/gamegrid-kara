package kara.gamegrid;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import kara.gamegrid.Kara.KaraDelegate;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBitmap;

/**
 * A WorldSetup contains information about the positions of the actors in the
 * world. A WorldSetup is immutable, i.e. after initialization it cannot be
 * changed. An immutable WorldSetup is easier to work with because we can be sure
 * that there will not be any side-effects if we make changes to any actors. And
 * we can always restore the initial WorldSetup.
 * <p>
 * Static helper methods are provided to create WorldSetups from a file and to
 * save them to a file. A text file can contain information for one or more
 * WorldSetup. A WorldSetup text file must have the following structure:
 * <p>
 * [titleKey]: World Setup Title<br>
 * [optionalAttribute1]: attribute<br>
 * [optionalAttribute2]: attribute<br>
 * [optionalAttributeN]: attribute<br>
 * <p>
 * After the titleKey and the optional attributes, the actor positions are
 * described with the following signs:
 * <ul>
 * <li>Kara: @
 * <li>Tree: #
 * <li>Leaf: .
 * <li>Mushroom: $
 * <li>Mushroom on Leaf: *
 * <li>Kara on Leaf: +
 * <li>Empty Field: Space
 * </ul>
 * <p>
 * A line starting with a Semicolon (;) is a comment and is ignored by the
 * parser.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class WorldSetup {
	public static final char UNDEFINED = '?';
	public static final char EMPTY = ' ';
	public static final char KARA = '@';
	public static final char TREE = '#';
	public static final char LEAF = '.';
	public static final char MUSHROOM = '$';
	public static final char MUSHROOM_LEAF = '*'; // Mushroom on a leaf
	public static final char KARA_LEAF = '+'; // Kara on a leaf
	
	public static final String WIDTH_KEY = "X:";
	public static final String HEIGHT_KEY = "Y:";
	
	/**
	 * The world width
	 */
	private final int width;
	
	/**
	 * The world height
	 */
	private final int height;
	
	/**
	 * The key to recognize the start of the WorldSetup inside the file, e.g.
	 * "World:". The characters following the key will be used as title.
	 */
	private final String titleKey;
	
	private final String title;
	
	private final String fileName;
	
	private final Map<String, String> attributes = new LinkedHashMap<String, String>();
	
	/**
	 * Actor positions with the outer List as line (y-position) and
	 * the inner List as column (x-position).
	 */
	private final List<List<Character>> actorPositions = new ArrayList<List<Character>>();

	/**
	 * Constructor to be used by the Builder.
	 */
	private WorldSetup(Builder builder) {
		this.width = builder.width;
		this.height = builder.height;
		this.titleKey = builder.titleKey;
		this.title = builder.title;
		this.fileName = builder.fileName;
		
		// copy values to make shure we have an immutable WorldSetup
		for (Entry<String, String> entry : builder.attributes.entrySet()) {
			this.attributes.put(entry.getKey(), entry.getValue());
		}
		for (List<Character> line : builder.actorPositions) {
			this.actorPositions.add(new ArrayList<Character>(line));
		}
	}
	
	/**
	 * This is a "copy constructor".
	 * 
	 * @param worldSetup
	 *            the WorldSetup to copy into the new WorldSetup.
	 */
	public WorldSetup(WorldSetup worldSetup) {
		this(new Builder(worldSetup));
	}
	
	/**
	 * Returns the number of horizontal cells.
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the number of vertical cells.
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the title of the world setup.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the title key that is used to identify the title inside the world
	 * setup text file.
	 * 
	 * @return
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * Returns the filename of the file this WorldSetup was loaded from. May be
	 * <code>null</code> if the WorldSetup was not loaded from a file.
	 * 
	 * @return The filename or <code>null</code>.
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Returns the attribute for the specified key.
	 * 
	 * @param attributeKey
	 * @return
	 */
	public String getAttribute(String attributeKey) {
		return attributes.get(attributeKey);
	}

	/**
	 * Returns a copy of the attributes map.
	 * 
	 * @return
	 */
	public Map<String, String> getAttributes() {
		// must be a copy of the real attributes map because otherwise
		// it would not be immutable.
		return new LinkedHashMap<String, String>(attributes);
	}

	/**
	 * Returns the actor type as character (see class description).
	 * 
	 * @param x
	 *            the x-position
	 * @param y
	 *            the y-position
	 * @return the actor char or {@link #UNDEFINED} if the position is not
	 *         defined.
	 */
	public char getActorTypeAt(int x, int y) {
		if (y >= 0 && y < actorPositions.size()) {
			List<Character> line = actorPositions.get(y);
			if (x >= 0 && x < line.size()) {
				return line.get(x);
			}
		}
		return UNDEFINED;
	}

	/**
	 * Returns an image representation of this WorldSetup with all the actors.
	 * 
	 * @param cellSize
	 *            The size of each cell of the grid.
	 * @return
	 */
	public BufferedImage toImage(int cellSize) {
		GGBitmap img = new GGBitmap(width * cellSize, height * cellSize);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				switch (this.getActorTypeAt(x, y)) {
				case WorldSetup.KARA:
					img.drawImage(WorldImages.ICON_MY_KARA, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.TREE:
					img.drawImage(WorldImages.ICON_TREE, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.LEAF:
					img.drawImage(WorldImages.ICON_LEAF, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.MUSHROOM:
					img.drawImage(WorldImages.ICON_MUSHROOM, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.MUSHROOM_LEAF:
					img.drawImage(WorldImages.ICON_LEAF, 
							x * cellSize + 10, y * cellSize);
					img.drawImage(WorldImages.ICON_MUSHROOM_ON_TARGET, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.KARA_LEAF:
					img.drawImage(WorldImages.ICON_LEAF, 
							x * cellSize + 10, y * cellSize);
					img.drawImage(WorldImages.ICON_MY_KARA, 
							x * cellSize + 10, y * cellSize);
					break;
				}
			}
		}

		return img.getBufferedImage();
	}

	/**
	 * Returns a ASCII String representation of this WorldSetup.
	 * 
	 * @param printWidthAndHeight
	 *            if true, the width and height attributes are also added.
	 * @return
	 */
	public String toASCIIText(boolean printWidthAndHeight) {
		StringBuffer buf = new StringBuffer();
		buf.append(titleKey).append(' ').append(title).append('\n');

		if (printWidthAndHeight) {
			buf.append(WIDTH_KEY).append(' ').append(width).append('\n');
			buf.append(HEIGHT_KEY).append(' ').append(height).append('\n');
		}

		for (Entry<String, String> entry : attributes.entrySet()) {
			buf.append(entry.getKey()).append(' ').append(entry.getValue())
					.append('\n');
		}

		for (List<Character> line : actorPositions) {
			for (Character character : line) {
				buf.append(character);
			}
			buf.append('\n');
		}

		return buf.toString();
	}

	@Override
	public String toString() {
		// Just return the title. This is used for drop down box when user needs
		// to choose between mutliple world setups.
		if (getFileName() != null) {
			return "<html>" + getTitle() + " <i>---> File: " + getFileName() + "</i></html>";
		}

		return getTitle();
	}

	/**
	 * Parses (one or many) world setups from the specified file. The default
	 * width and height attribute keys are used to get width and height from the
	 * file. If those attributes are not defined in the text file, width and
	 * height are determined from the actor positions.
	 * 
	 * @param fileName
	 *            The filename of the world setup file, relative to the package
	 *            root or relative to the project root. Wildcards (? or *) may
	 *            be used.
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:". The characters following the key will be
	 *            used as title.
	 * @param attributeKeys
	 *            Keys for optional attributes, e.g. "Password:".
	 * @return the world setups as an array
	 */
	public static WorldSetup[] parseFromFile(String fileName, String titleKey,
			String... attributeKeys) {
		return parseFromFile(fileName, null, titleKey, -1, -1, attributeKeys);
	}

	/**
	 * Parses (one or many) world setups from the specified file. The default
	 * width and height attribute keys are used to get width and height from the
	 * file. If those attributes are not defined in the text file, width and
	 * height are determined from the actor positions.
	 * 
	 * @param fileName
	 *            The filename of the world setup file, relative to the class,
	 *            relative to the package root or relative to the project root.
	 *            Wildcards (? or *) may be used.
	 * @param clazz
	 *            The class used to get the relative path to the file or
	 *            <code>null</code> if the file should be retrieved relative to
	 *            the jar root or project root.
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:". The characters following the key will be
	 *            used as title.
	 * @param attributeKeys
	 *            Keys for optional attributes, e.g. "Password:".
	 * @return the world setups as an array
	 */
	public static WorldSetup[] parseFromFile(String fileName, Class<?> clazz,
			String titleKey, String... attributeKeys) {
		return parseFromFile(fileName, clazz, titleKey, -1, -1, attributeKeys);
	}

	/**
	 * Parses (one or many) world setups from the specified file.
	 * 
	 * @param fileName
	 *            The filename of the world setup file, relative to the class,
	 *            relative to the package root or relative to the project root.
	 *            Wildcards (? or *) may be used.
	 * @param clazz
	 *            The class used to get the relative path to the file or
	 *            <code>null</code> if the file should be retrieved relative to
	 *            the jar root or project root.
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:". The characters following the key will be
	 *            used as title.
	 * @param worldWidth
	 *            the width or -1 if it should be specified through width
	 *            attribute or from length of actor lines in the file.
	 * @param worldHeight
	 *            the height or -1 if it should be specified through height
	 *            attribute or from height of actor lines in the file.
	 * @param attributeKeys
	 *            Keys for optional attributes, e.g. "Password:".
	 * @return the world setups as an array
	 */
	public static WorldSetup[] parseFromFile(String fileName, Class<?> clazz,
			String titleKey, int worldWidth, int worldHeight,
			String... attributeKeys) {
		List<WorldSetup> result = new ArrayList<WorldSetup>();

		try {
			List<File> matchingFiles = findMatchingFiles(fileName, clazz);
			
			for (File matchingFile : matchingFiles) {
				List<String> lines = FileUtils.readAllLines(matchingFile);
				
				Builder currentBuilder = null;
				
				// Go trough all lines of the file
				for (String line : lines) {
					if (!line.startsWith(";")) {
						if (line.startsWith(titleKey)) {
							if (currentBuilder != null) {
								result.add(currentBuilder.build());
							}
							currentBuilder = new Builder(titleKey);
							currentBuilder.setTitle(line.substring(titleKey.length()).trim());
							currentBuilder.setFileName(matchingFile.getName());
							
							if (worldWidth > -1 && worldHeight > -1) {
								currentBuilder.setWidth(worldWidth);
								currentBuilder.setHeight(worldHeight);
							}
							continue;
						} else if (currentBuilder == null) {
							continue; // continue until we have the first valid
										// world setup title key
						}

						if (line.startsWith(WIDTH_KEY)) {
							try {
								currentBuilder.setWidth(Integer.parseInt(line
										.substring(WIDTH_KEY.length()).trim()));
								continue;
							} catch (NumberFormatException e) {
								// do nothing
							}
						}

						if (line.startsWith(HEIGHT_KEY)) {
							try {
								currentBuilder
										.setHeight(Integer.parseInt(line
												.substring(HEIGHT_KEY.length())
												.trim()));
								continue;
							} catch (NumberFormatException e) {
								// do nothing
							}
						}

						boolean foundAttribute = false;
						for (String attributeKey : attributeKeys) {
							if (line.startsWith(attributeKey)) {
								currentBuilder.addAttribute(attributeKey, line
										.substring(attributeKey.length())
										.trim());
								foundAttribute = true;
								break;
							}
						}
						if (foundAttribute) {
							continue;
						}

						if (line.matches("[@#.$\\s*+]*")) {
							currentBuilder.addActorLine(line);
						}
					}
				}
				// add the last world setup
				if (currentBuilder != null) {
					result.add(currentBuilder.build());
				}
			}
			
		} catch (Exception ex) {
			// Just in case something goes wrong we catch it and print it out
			ex.printStackTrace();
		}

		return result.toArray(new WorldSetup[result.size()]);
	}

	/**
	 * Tries to load the specified file (or files) either relative to the class,
	 * relative to the package root or relative to the project root.
	 * 
	 * @param fileName
	 *            The filename of the world setup file, possibly relative to the
	 *            clazz. Wildcards (? or *) may be used.
	 * @param clazz
	 *            The class used to get the relative path to the file or
	 *            <code>null</code> if the file should be retrieved relative to
	 *            the jar root or project root.
	 * @return the files or an empty list if none could be found.
	 */
	private static List<File> findMatchingFiles(String fileName, Class<?> clazz) {
		try {
			// Option 1: try to get file relative to class
			if (clazz != null) {
				File dir = new File(clazz.getResource(".").toURI());
				List<File> files = FileUtils.scan(dir, fileName);
				if (!files.isEmpty()) {
					return files;
				}
			}
			
			// Option 2: try to get file relative to package root (may be inside jar)
			File dir2 = new File(Thread.currentThread().getContextClassLoader().getResource(".").toURI());
			List<File> files2 = FileUtils.scan(dir2, fileName);
			if (!files2.isEmpty()) {
				return files2;
			}
			
			// Option 3: try to get file relative to project root (outside of jar)
			File dir3 = new File(".");
			List<File> files3 = FileUtils.scan(dir3, fileName);
			if (!files3.isEmpty()) {
				// Note: only get the first match
				return files3;
			}
		} catch (URISyntaxException e) {
			// do nothing
		}
		
		// Return empty list
		return new ArrayList<File>();
	}

	/**
	 * Creates a WorldSetup from all the actors in the list. An empty world
	 * setup title is used.
	 * 
	 * @param actors
	 * @param worldWidth
	 * @param worldHeight
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:".
	 * @return
	 */
	public static WorldSetup createFromActors(List<Actor> actors,
			int worldWidth, int worldHeight, String titleKey) {
		return createFromActors(actors, worldWidth, worldHeight, titleKey, "",
				null);
	}

	/**
	 * Creates a WorldSetup from all the actors in the list.
	 * 
	 * @param actors
	 * @param worldWidth
	 * @param worldHeight
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:".
	 * @param title
	 *            The title of the world setup
	 * @param attributes
	 *            Attributes or an empty map if no attributes should be added.
	 * @return
	 */
	public static WorldSetup createFromActors(List<Actor> actors,
			int worldWidth, int worldHeight, String titleKey, String title,
			Map<String, String> attributes) {
		Builder builder = new Builder(titleKey);
		builder.setWidth(worldWidth).setHeight(worldHeight).setTitle(title);

		if (attributes != null) {
			for (Entry<String, String> entry : attributes.entrySet()) {
				builder.addAttribute(entry.getKey(), entry.getValue());
			}
		}

		if (actors != null) {
			for (Actor actor : actors) {
				builder.addFromActor(actor);
			}
		}

		return builder.build();
	}

	/**
	 * This is a builder class for WorldSetup according to the Builder design
	 * pattern. The Builder helps us to step-by-step add new properties and
	 * build an immutable WorldSetup at the end.
	 */
	public static class Builder {
		private final String titleKey;
		private int width = 0;
		private int height = 0;
		private String title = "";
		private String fileName;
		private Map<String, String> attributes = new LinkedHashMap<String, String>();
		private List<List<Character>> actorPositions = new ArrayList<List<Character>>();
		
		/**
		 * Default constructor.
		 */
		public Builder(String titleKey) {
			this.titleKey = titleKey;
		}

		/**
		 * Copy constructor.
		 */
		public Builder(WorldSetup worldSetup) {
			width = worldSetup.getWidth();
			height = worldSetup.getHeight();
			titleKey = worldSetup.getTitleKey();
			title = worldSetup.getTitle();
			attributes = worldSetup.attributes;
			actorPositions = worldSetup.actorPositions;
			fileName = worldSetup.fileName;
		}

		public Builder setWidth(int width) {
			this.width = width;
			return this;
		}

		public Builder setHeight(int height) {
			this.height = height;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public Builder setFileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public Builder addAttribute(String key, String value) {
			this.attributes.put(key, value);
			return this;
		}

		/**
		 * Add a line of actors to the actor positions.
		 * 
		 * @param actorLine
		 * @return
		 */
		public Builder addActorLine(String actorLine) {
			List<Character> chars = new ArrayList<Character>();
			for (char c : actorLine.toCharArray()) {
				chars.add(c);
			}
			this.actorPositions.add(chars);
			return this;
		}

		/**
		 * Set the actor type at the specified position.
		 */
		public Builder setActorTypeAt(int x, int y, char actorType) {
			return setActorTypeAt(x, y, actorType, false);
		}

		/**
		 * Set the actor type at the specified position.
		 * 
		 * @param x
		 * @param y
		 * @param actorType
		 * @param combine
		 *            if true, kara-leafs and mushroom-leafs are combined.
		 * @return
		 */
		public Builder setActorTypeAt(int x, int y, char actorType,
				boolean combine) {
			while (actorPositions.size() <= y) {
				// the line (y) is not present yet, so create lines until we
				// reach it
				actorPositions.add(new ArrayList<Character>());
			}
			// get the relevant line
			List<Character> line = actorPositions.get(y);

			while (line.size() <= x) {
				// fill with EMPTY characters until we reach position x
				line.add(EMPTY);
			}
			// now we can actually set the actor at x
			if (combine && isKaraLeaf(line.get(x), actorType)) {
				line.set(x, KARA_LEAF);
			} else if (combine && isMushroomLeaf(line.get(x), actorType)) {
				line.set(x, MUSHROOM_LEAF);
			} else {
				// just overwrite the current char
				line.set(x, actorType);
			}

			return this;
		}

		/**
		 * Adds the actor position and type from information of the specified
		 * actor.
		 * 
		 * @param actor
		 * @return
		 */
		public Builder addFromActor(Actor actor) {
			if (actor instanceof KaraDelegate) {
				setActorTypeAt(actor.getX(), actor.getY(), KARA, true);
			} else if (actor instanceof Tree) {
				setActorTypeAt(actor.getX(), actor.getY(), TREE, true);
			} else if (actor instanceof Leaf) {
				setActorTypeAt(actor.getX(), actor.getY(), LEAF, true);
			} else if (actor instanceof Mushroom) {
				setActorTypeAt(actor.getX(), actor.getY(), MUSHROOM, true);
			}

			return this;
		}

		/**
		 * Returns true if the two chars are a kara and a leaf.
		 */
		private boolean isKaraLeaf(char first, char second) {
			return (first == LEAF && second == KARA)
					|| (first == KARA && second == LEAF);
		}

		/**
		 * Returns true if the two chars are a mushroom and a leaf.
		 */
		private boolean isMushroomLeaf(char first, char second) {
			return (first == MUSHROOM && second == KARA)
					|| (first == KARA && second == MUSHROOM);
		}

		/**
		 * Builds the (immutable) WorldSetup.
		 * 
		 * @return
		 */
		public WorldSetup build() {
			if (width < 1) {
				// infer width from actor positions
				for (List<Character> line : actorPositions) {
					width = Math.max(width, line.size());
				}
			}
			if (height < 1) {
				// infer height from actor positions
				height = actorPositions.size();
			}

			return new WorldSetup(this);
		}
	}

	/**
	 * Utility class for loading and saving files.
	 */
	public static class FileUtils {

		/**
		 * Opens a file chooser dialog to ask the user for a filename. If
		 * successful, the given content is written to the chosen text file.
		 * 
		 * @param content
		 *            Content to write to the file
		 * @throws IOException
		 */
		public static void saveToFileWithDialog(String content)
				throws IOException {
			File f = new File(new File(".").getCanonicalPath());

			JFileChooser fileChooser = new JFileChooser(f);
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "*.txt, *.TXT";
				}

				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					String s = f.getName();
					return s.endsWith(".txt") || s.endsWith(".TXT");
				}
			});

			int retrival = fileChooser.showSaveDialog(null);

			if (retrival == JFileChooser.APPROVE_OPTION) {
				File chosenFile = fileChooser.getSelectedFile();

				if (chosenFile.exists()) {
					int option = JOptionPane
							.showConfirmDialog(
									null,
									"The file "
											+ chosenFile
											+ " exists already. Do you want to overwrite the existing file?",
									"File Exists", JOptionPane.YES_NO_OPTION);
					if (option != JOptionPane.YES_OPTION) {
						// abort
						return;
					}
				}

				// write to file
				if (!chosenFile.getName().endsWith(".txt")
						&& !chosenFile.getName().endsWith(".TXT")) {
					chosenFile = new File(chosenFile.getAbsolutePath() + ".txt");
				}
				FileUtils.writeToFile(chosenFile, content);
			}
		}

		/**
		 * Scans through the directory using wild-card patterns. All files
		 * matching the patterns are returned.
		 * <ul>
		 * <li>Use ? for one or no unknown character</li>
		 * <li>Use * for zero or more unknown characters</li>
		 * </ul>
		 * 
		 * @param dir
		 *            the directory
		 * @param patterns
		 *            the patterns that should be matched containing wild-cards.
		 * @return
		 */
		public static List<File> scan(File dir, String... patterns) {
			List<File> result = new ArrayList<File>();
			if (!dir.isDirectory()) {
				return result;
			}

			List<String> convertedPatterns = new ArrayList<String>();
			for (String p : patterns) {
				p = p.replace(".", "\\.");
				p = p.replace("?", ".?");
				p = p.replace("*", ".*");
				convertedPatterns.add(p);
			}

			File[] filesInDir = dir.listFiles();
			for (File currentFile : filesInDir) {
				if (matches(currentFile, convertedPatterns)) {
					result.add(currentFile);
				}
			}
			return result;
		}

		/**
		 * Returns true if the filename of the file matches one of the patterns.
		 * 
		 * @param file
		 * @param convertedPatterns
		 * @return
		 */
		private static boolean matches(File file, List<String> convertedPatterns) {
			for (String pattern : convertedPatterns) {
				if (file.getName().matches(pattern)) {
					return true;
				}
			}
			return false;
		}
		
	    /**
		 * Read all lines from a file.
		 * 
		 * @param file
		 *            the file to read from
		 * @return the lines from the file as a {@code List}.
		 * 
		 * @throws IOException
		 *             if an I/O error occurs reading from the file.
		 */
	    public static List<String> readAllLines(File file) throws IOException {
	    	List<String> result = new ArrayList<String>();
	    	BufferedReader reader = null;
	    	try {
	    		reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))); 
	    		
	    		for (;;) {
	    			String line = reader.readLine();
	    			if (line == null) {
	    				break;
	    			}
	    			result.add(line);
	    		}
	    	} finally {
	    		if (reader != null) {
	    			reader.close();
	    		}
	    	}
            return result;
	    }
	    
	    /**
		 * Writes to the specified file.
		 * 
		 * @param file
		 *            The file to write to
		 * @param content
		 *            The content to write.
		 * @throws IOException
		 *             if an I/O error occurs writing to the file.
		 */
	    public static void writeToFile(File file, String content) throws IOException {
	    	FileWriter fstream = new FileWriter(file);
	    	BufferedWriter out = new BufferedWriter(fstream);
			try {
				out.write(content);
				out.flush();
			} finally {
				// Close the output stream
				if (out != null) {
					out.close();
				}
			}
	    }
	}
}
