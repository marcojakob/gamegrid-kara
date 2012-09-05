package ch.gamegrid.kara.world;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBitmap;
import ch.gamegrid.kara.actors.Kara.KaraDelegate;
import ch.gamegrid.kara.actors.Leaf;
import ch.gamegrid.kara.actors.Mushroom;
import ch.gamegrid.kara.actors.Tree;

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
	
	private final Map<String, String> attributes = new LinkedHashMap<>();
	
	/**
	 * Actor positions with the outer List as line (y-position) and
	 * the inner List as column (x-position).
	 */
	private final List<List<Character>> actorPositions = new ArrayList<>();

	/**
	 * Constructor to be used by the Builder.
	 */
	private WorldSetup(Builder builder) {
		this.width = builder.width;
		this.height = builder.height;
		this.titleKey = builder.titleKey;
		this.title = builder.title;
		
		// copy values to make shure we have an immutable WorldSetup
		for (Entry<String, String> entry : builder.attributes.entrySet()) {
			this.attributes.put(entry.getKey(), entry.getValue());
		}
		for (List<Character> line : builder.actorPositions) {
			this.actorPositions.add(new ArrayList<>(line));
		}
	}
	
	/**
	 * This is a "copy constructor".
	 * 
	 * @param worldSetup the WorldSetup to copy into the new WorldSetup.
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
	 * Returns the title key that is used to identify the title inside
	 * the world setup text file.
	 * 
	 * @return
	 */
	public String getTitleKey() {
		return titleKey;
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
		return new LinkedHashMap<>(attributes);
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
	 * @param cellSize The size of each cell of the grid.
	 * @return
	 */
	public BufferedImage toImage(int cellSize) {
		GGBitmap img = new GGBitmap(width * cellSize, height * cellSize);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				switch (this.getActorTypeAt(x, y)) {
				case WorldSetup.KARA:
					img.drawImage(WorldImages.KARA, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.TREE:
					img.drawImage(WorldImages.TREE, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.LEAF:
					img.drawImage(WorldImages.LEAF, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.MUSHROOM:
					img.drawImage(WorldImages.MUSHROOM, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.MUSHROOM_LEAF:
					img.drawImage(WorldImages.LEAF, 
							x * cellSize + 10, y * cellSize);
					img.drawImage(WorldImages.MUSHROOM_ON_TARGET, 
							x * cellSize + 10, y * cellSize);
					break;

				case WorldSetup.KARA_LEAF:
					img.drawImage(WorldImages.LEAF, 
							x * cellSize + 10, y * cellSize);
					img.drawImage(WorldImages.KARA, 
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
			buf.append(entry.getKey()).append(' ').append(entry.getValue()).append('\n');
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
		return getTitle();
	}
	
	/**
	 * Parses (one or many) world setups from the specified file.
	 * The default width and height attribute keys are used to get width
	 * and height from the file. If those attributes are not defined in the
	 * text file, width and height are determined from the actor positions.
	 * 
	 * @param file
	 *            The filename of the world setup file.
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:". The characters following the key will
	 *            be used as title.
	 * @param attributeKeys
	 *            Keys for optional attributes, e.g. "Password:".
	 * @return the world setups as an array
	 */
	public static WorldSetup[] parseFromFile(String file, String titleKey, 
			String... attributeKeys) {
		return parseFromFile(file, titleKey, -1, -1, attributeKeys);		
	}
	
	/**
	 * Parses (one or many) world setups from the specified file.
	 * 
	 * @param file
	 *            The filename of the world setup file.
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:". The characters following the key will
	 *            be used as title.
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
	public static WorldSetup[] parseFromFile(String file, String titleKey, int worldWidth, 
			int worldHeight, String... attributeKeys) {
		List<WorldSetup> result = new ArrayList<WorldSetup>();

		try {
			// 1. Option: try to get file inside jar
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
			if (stream == null) {
				// 2. Option: try to get file outside of jar
				stream = new FileInputStream(file);
			}
			
			BufferedReader input = new BufferedReader(new InputStreamReader(stream));
			
			String line;
			
			Builder currentBuilder = null;

			try {
				while ((line = input.readLine()) != null) {
					if (!line.startsWith(";")) {
						if (line.startsWith(titleKey)) {
							if (currentBuilder != null) {
								result.add(currentBuilder.build());
							}
							currentBuilder = new Builder(titleKey);
							
							currentBuilder.setTitle(line.substring(titleKey.length()).trim());
							if (worldWidth > -1 && worldHeight > -1) {
								currentBuilder.setWidth(worldWidth);
								currentBuilder.setHeight(worldHeight);
							}
							continue;
						} else if (currentBuilder == null) {
							continue; // continue until we have the first valid world setup title key
						}
						
						if (line.startsWith(WIDTH_KEY)) {
							try {
								currentBuilder.setWidth(Integer.parseInt(
										line.substring(WIDTH_KEY.length()).trim()));
								continue;
							} catch (NumberFormatException e) {
								// do nothing
							}
						}
						
						if (line.startsWith(HEIGHT_KEY)) {
							try {
								currentBuilder.setHeight(Integer.parseInt(
										line.substring(HEIGHT_KEY.length()).trim()));
								continue;
							} catch (NumberFormatException e) {
								// do nothing
							}
						}
							
						boolean foundAttribute = false;
						for (String attributeKey : attributeKeys) {
							if (line.startsWith(attributeKey)) {
								currentBuilder.addAttribute(attributeKey, 
										line.substring(attributeKey.length()).trim());
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
			} finally {
				input.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result.toArray(new WorldSetup[result.size()]);
	}
	
	/**
	 * Creates a WorldSetup from all the actors in the list.
	 * An empty world setup title is used.
	 * 
	 * @param actors
	 * @param worldWidth
	 * @param worldHeight
	 * @param titleKey
	 *            The key to recognize the start of the world setup inside the
	 *            file, e.g. "World:". 
	 * @return
	 */
	public static WorldSetup createFromActors(List<Actor> actors, int worldWidth, int worldHeight, 
			String titleKey) {
		return createFromActors(actors, worldWidth, worldHeight, titleKey, "", null);
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
	 * @param title The title of the world setup
	 * @param attributes Attributes or an empty map if no attributes should be added.
	 * @return
	 */
	public static WorldSetup createFromActors(List<Actor> actors, int worldWidth, int worldHeight,
			String titleKey, String title, Map<String, String> attributes) {
		Builder builder = new Builder(titleKey);
		builder.setWidth(worldWidth)
				.setHeight(worldHeight)
				.setTitle(title);

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
		private Map<String, String> attributes = new LinkedHashMap<>();
		private List<List<Character>> actorPositions = new ArrayList<>();
		
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
			List<Character> chars = new ArrayList<>();
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
		 * @param combine if true, kara-leafs and mushroom-leafs are combined.
		 * @return
		 */
		public Builder setActorTypeAt(int x, int y, char actorType, boolean combine) {
			while (actorPositions.size() <= y) {
				// the line (y) is not present yet, so create lines until we reach it
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
			return (first == LEAF && second == KARA) || (first == KARA && second == LEAF);
		}
		
		/**
		 * Returns true if the two chars are a mushroom and a leaf.
		 */
		private boolean isMushroomLeaf(char first, char second) {
			return (first == MUSHROOM && second == KARA) || (first == KARA && second == MUSHROOM);
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
}