package kara.gamegrid;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GGResetListener;
import ch.aplu.jgamegrid.GGTileMap;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

/**
 * The World class sets up the grid for all the Actors. The initial actors are
 * placed inside the World according to a specified {@link WorldSetup}.
 * 
 * @author Marco Jakob (http://edu.makery.ch)
 */
@SuppressWarnings("serial")
public class KaraWorld extends GameGrid implements GGMouseListener,
		GGResetListener {
	
	// Size of one cell
	public static final int CELL_SIZE = 28; 
	
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(180, 230, 180);
	public static final Color DEFAULT_GRID_COLOR = new Color(170, 170, 170);
	
	public static final String WORLD_SETUP_TITLE_KEY = "World:";
	public static final String KARA_DIRECTION_KEY = "Kara:";
	public static final String DIRECTION_RIGHT = "right";
	public static final String DIRECTION_DOWN = "down";
	public static final String DIRECTION_LEFT = "left";
	public static final String DIRECTION_UP = "up";
	
	public static final Class<?>[] PAINT_ORDER = {
			Actor.class,
			Kara.KaraDelegate.class, 
			Tree.class, 
			Mushroom.class, 
			Leaf.class};
	
    protected Class<? extends Kara> karaClass;
    
    /**
     * Contains an enumeration of settings for the mouse used inside the world.
     */
    public enum MouseSettings {
		ENABLED,
		DISABLED,
		DISABLED_WHEN_RUNNING
	}

	private MouseSettings mouseDragAndDrop = MouseSettings.DISABLED_WHEN_RUNNING;
	private MouseSettings mouseContextMenu = MouseSettings.DISABLED_WHEN_RUNNING;
	
    private WorldSetup worldSetup;
    
    private int resetCountDown = -1;
    
	/**
	 * Loads the Kara World from the specified world setup file <br>
	 * <i>Laedt die Kara Welt von der angegebenen Datei</i>
	 * 
	 * @param worldFile
	 *            The filename of the world setup file, relative to the class,
	 *            relative to the package root or relative to the project root.
	 *            Wildcards (? or *) may be used.
	 * @param karaClass
	 *            The class where Kara is programmed in.
	 */
	public KaraWorld(String worldFile,  Class<? extends Kara> karaClass) {
		// load world setup from file. the package name 
		this(loadWorldSetupFromFile(worldFile, karaClass), karaClass);
	}
	
	/**
	 * Creates a world for Kara with the specified world setup.
	 * 
	 * @param worldSetup
	 *            The world setup to load.
	 * 
	 * @param karaClass
	 *            The class implementing Kara, e.g. MyKara.class.
	 */
	public KaraWorld(WorldSetup worldSetup, Class<? extends Kara> karaClass) {
		// Create the new world
		this(worldSetup.getWidth(), worldSetup.getHeight(), karaClass);
		this.worldSetup = worldSetup;
		
		setTitle(worldSetup.getTitle());
		
		// since we have a world setup, we call prepare() here
		prepare();
	}
	
	/**
	 * Creates an empty world for Kara with specified width and height.
	 * 
	 * @param worldWidth
	 *            Number of horizontal cells
	 * @param worldHeight
	 *            Number of vertical cells
	 * @param karaClass
	 *            The class implementing Kara. e.g. MyKara.class.
	 */
	public KaraWorld(int worldWidth, int worldHeight, Class<? extends Kara> karaClass) {
		// Create the new world
		super(worldWidth, worldHeight, CELL_SIZE, null,
				null, true, 4);
		this.karaClass = karaClass;
		createFieldBackground();
		setTitle(karaClass.getSimpleName());
		getFrame().setIconImage(WorldImages.ICON_MY_KARA);
		
		// listen for right click --> context menu
		addMouseListener(this, GGMouse.rClick);
		
		addResetListener(this);
	}
	
	/**
	 * Loads the world setup from the specified file. If it cannot be found, a
	 * warning message is displayed and a default world setup is loaded.
	 * 
	 * @param worldFile
	 *            The filename of the world setup file, relative to the class,
	 *            relative to the package root or relative to the project root.
	 *            Wildcards (? or *) may be used.
	 * @param karaClass
	 *            The class where Kara is programmed in.
	 * @return
	 */
	private static WorldSetup loadWorldSetupFromFile(String worldFile, Class<? extends Kara> karaClass) {
		WorldSetup[] worldSetups = null;
		try {
			worldSetups = WorldSetup.parseFromFile(worldFile, karaClass, WORLD_SETUP_TITLE_KEY, 
					KARA_DIRECTION_KEY);
			
			if (worldSetups == null || worldSetups.length == 0) {
				String message = "<html>" + "Could not load world setup from file: <p><i>" 
						+ "Konnte keine Welt laden von der Datei: "
						+ "</i><p><p>" + worldFile 
						+ "<p><p>(A world-file must start with \"" + WORLD_SETUP_TITLE_KEY 
						+ "\")</html>";
				
				JOptionPane.showMessageDialog(null, message, "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (IOException e) {
			String message = "<html>" + "Could not find world setup file: <p><i>" 
					+ "Konnte die world setup Datei nicht finden: "
					+ "</i><p><p>" + worldFile + "</html>";
			
			JOptionPane.showMessageDialog(null, message, "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		
		WorldSetup result = null;
		
		if (worldSetups != null) {
			if (worldSetups.length == 1) {
				result = worldSetups[0];
			} else if (worldSetups.length > 1) {
				// User must choose from a list of world setups
				result = (WorldSetup) JOptionPane.showInputDialog(null, 
						"<html>Please Choose a World: <p><i>Bitte waehle eine Welt:</i>", 
						"Choose World",
						JOptionPane.QUESTION_MESSAGE, null,
						worldSetups, // Array of choices
						worldSetups[0]); // Initial choice
			}
		}
		
		if (result != null) {
			return result;
		} else {
			// return a default world setup
			return new WorldSetup.Builder(WORLD_SETUP_TITLE_KEY)
			.setWidth(10)
			.setHeight(10)
			.setTitle("Kara Default")
			.build();
		}
	}

	/**
	 * Initializes the background tiles with the field icon.
	 */
	public void createFieldBackground() {
		setBgImagePath(null);
		setGridColor(DEFAULT_GRID_COLOR);
		setBgColor(DEFAULT_BACKGROUND_COLOR);
		
		GGTileMap tileMap = createTileMap(getNbHorzCells(), getNbVertCells(), getCellSize(), getCellSize());
		for (int x = 0; x < getNbHorzCells(); x++) {
			for (int y = 0; y < getNbVertCells(); y++) {
				tileMap.setImage(WorldImages.ICON_BACKGROUND_FIELD_PATH, x, y);
			}
		}
		tileMap.setPosition(new Point(1, 1));
	}
	
	/**
	 * Removes the background tiles.
	 */
	public void clearFieldBackground() {
		setBgImagePath(null);
		setGridColor(null);
		setBgColor(null);
		// there is no method to remove the tile map, so we create
		// a new tile map with 0 tiles.
		createTileMap(0, 0, 1, 1);
	}
	
	/**
	 * Prepares the world, i.e. creates all initial actors. If a world setup is
	 * available, it is loaded.
	 * <p>
	 * This method may be overridden by subclasses to provide their own means to
	 * initialize actors, e.g. by calling {@link #addObject()}.
	 */
	protected void prepare() {
		if (worldSetup != null) {
			initActorsFromWorldSetup(worldSetup);
		}
	}
	
	/**
	 * Helper method to match Greenfoot's addObject. So we can just copy-paste
	 * the prepare method from KaraWorlds coming from Greenfoot.
	 * 
	 * @param actor
	 *            the actor that should be added to the world.
	 * @param x
	 *            the x position of the actor in the grid.
	 * @param y
	 *            the y position of the actor in the grid.
	 */
	public void addObject(Actor actor, int x, int y) {
		Location location = new Location(x, y);
	
		if (canAddActor(actor.getClass(), location)) {
			addActorNoRefresh(actor, location);
	
			// Add mouse listener to enable dragging of actors
			switch (mouseDragAndDrop) {
			case ENABLED:
				addMouseListener(new DragListener(actor, false), 
						GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
				break;
				
			case DISABLED_WHEN_RUNNING:
				addMouseListener(new DragListener(actor, true), 
						GGMouse.lPress | GGMouse.lDrag | GGMouse.lRelease);
				break;
				
			case DISABLED:
				// no drag listener
				break;
			}
	
			// Seems we have to set the paint order every time an object is
			// added...?
			setPaintOrder(PAINT_ORDER);
			
			updateMushroomImages();
	
		} else {
			System.out.println("Could not add " + actor.toString()
					+ " to field " + location.toString());
		}
	}

	/**
	 * Helper method to match Greenfoot's addObject. So we can just copy-paste
	 * the prepare method from KaraWorlds coming from Greenfoot.
	 * 
	 * @param kara
	 *            kara that should be added to the world.
	 * @param x
	 *            the x position of the actor in the grid.
	 * @param y
	 *            the y position of the actor in the grid.
	 */
	public void addObject(Kara kara, int x, int y) {
		addObject(kara.actorDelegate, x, y);
	}

	/**
	 * Initializes the actors based on actor information in the specified
	 * {@link WorldSetup}.
	 */
	public void initActorsFromWorldSetup(WorldSetup worldSetup) {
		for (int y = 0; y < worldSetup.getHeight(); y++) {
			for (int x = 0; x < worldSetup.getWidth(); x++) {
				switch (worldSetup.getActorTypeAt(x, y)) {
				case WorldSetup.KARA:
					addObject(createNewKaraInstance(), x, y);
					break;
				case WorldSetup.TREE:
					addObject(new Tree(), x, y);
					break;
				case WorldSetup.LEAF:
					addObject(new Leaf(), x, y);
					break;
				case WorldSetup.MUSHROOM:
					addObject(new Mushroom(), x, y);
					break;
				case WorldSetup.MUSHROOM_LEAF:
					addObject(new Mushroom(true), x, y);
					addObject(new Leaf(), x, y);
					break;
				case WorldSetup.KARA_LEAF:
					addObject(createNewKaraInstance(), x, y);
					addObject(new Leaf(), x, y);
					break;
				}
			}
		}
		
		String karaDirection = worldSetup.getAttribute(KARA_DIRECTION_KEY);
		if (karaDirection != null) {
			
			if (karaDirection.equalsIgnoreCase(DIRECTION_DOWN)) {
				for (Actor actor : getActors(Kara.KaraDelegate.class)) {
					Kara kara = ((Kara.KaraDelegate) actor).getKara();
					kara.turnRight();
				}
			} else if (karaDirection.equalsIgnoreCase(DIRECTION_LEFT)) {
				for (Actor actor : getActors(Kara.KaraDelegate.class)) {
					Kara kara = ((Kara.KaraDelegate) actor).getKara();
					kara.turnRight();
					kara.turnRight();
				}
			} else if (karaDirection.equalsIgnoreCase(DIRECTION_UP)) {
				for (Actor actor : getActors(Kara.KaraDelegate.class)) {
					Kara kara = ((Kara.KaraDelegate) actor).getKara();
					kara.turnLeft();
				}
			}
			// DIRECTION_RIGHT is the original direction - do nothing
		}
	}
	
	/**
	 * Sets if actor dragging should be enabled. This must be done before
	 * actors are created.
	 * Default is {@link MouseSettings#DISABLED_WHEN_RUNNING}.
	 * 
	 * @param actorDragging
	 */
	public void setMouseDragAndDrop(MouseSettings mouseDragAndDrop) {
		this.mouseDragAndDrop = mouseDragAndDrop;
	}
	
	/**
	 * Sets if the context menu should be enabled. 
	 * Default is {@link MouseSettings#DISABLED_WHEN_RUNNING}.
	 * 
	 * @param mouseContextMenu
	 */
	public void setMouseContextMenu(MouseSettings mouseContextMenu) {
		this.mouseContextMenu = mouseContextMenu;
	}
	
	@Override
	public boolean resetted() {
		resetCountDown = 1;
		setSimulationPeriod(0); // speed things up a bit
		
		if (!isRunning()) {
			// needs to run, so we get into the Worlds act() method.
			doRun();
		}
		
		// consume: prevent anything else to do something when resetted
		return true; 
	}
	
	@Override
	public void act() {
		// GameGrids act() is called before the act() of all actors
		if (resetCountDown > 0) {
			// Now we know that we should reset
			doPause();
			setSimulationPeriod(200);
			removeAllActors();
			prepare();
			refresh();
			
			// The newly created actors will be in the act-cycle that just
			// started. Therefore, we must disable act for all actors.
			for (Actor actor : getActors()) {
				actor.setActEnabled(false);
			}
			resetCountDown = 0;
		} else if (resetCountDown == 0) {
			// Now we are in the second cycle of resetting and can enable
			// the actors act again.
			for (Actor actor : getActors()) {
				actor.setActEnabled(true);
			}
			resetCountDown = -1;
		}
	}
	
	@Override
	public boolean mouseEvent(GGMouse mouse) {
		// Right click on world --> show context menu
		if (mouseContextMenu == MouseSettings.ENABLED 
				|| (mouseContextMenu == MouseSettings.DISABLED_WHEN_RUNNING && !isRunning())) {
			
			Location location = toLocationInGrid(mouse.getX(), mouse.getY());
			
			ContextMenu menu = new ContextMenu(this, location, getOneActorAt(location));
			menu.show(this, mouse.getX(), mouse.getY());
		}

		return false; // Don't consume the event, other listeners must be notified
	}
	
	/**
	 * Prints the world setup to the console.
	 */
	public void printWorldSetupToConsole() {
		System.out.println(";-------------------------- START --------------------------");
		System.out.println(toASCIIText());
		System.out.println(";--------------------------- END ---------------------------\n");
	}
	
	/**
	 * Saves the world setup to a file that the user can choose.
	 */
	public void saveWorldSetupToFile() {
		try {
			WorldSetup.FileUtils.saveToFileWithDialog(toASCIIText());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Creates an ASCII-representation of all the actors in the world.
	 * 
	 * @return the world as ASCII text
	 */
	protected String toASCIIText() {
		return WorldSetup.createFromActors(getActors(), getNbHorzCells(), getNbVertCells(),
				WORLD_SETUP_TITLE_KEY).toASCIIText(true);
	}

	/**
	 * Gets the most recently pressed key <br>
	 * <i>Ermittelt die zuletzt gedrueckte Taste</i>.
	 * <p>
	 * 
	 * <ul>
	 * <li>"a", "b", .., "z" (alphabetical keys), "0".."9" (digits), most punctuation marks. 
	 * 		Also returns uppercase characters when appropriate.</li>
	 * <li>"up", "down", "left", "right" (the cursor keys)</li>
	 * <li>"enter", "space", "tab", "escape", "backspace", "shift", "control"</li>
	 * <li>"F1", "F2", .., "F12" (the function keys)</li>
	 * </ul>
	 * 
	 * @return the most recently pressed key as String or an empty String if no key was pressed.
	 */
	protected String getKey() {
		return convertKeyCode(getKeyCode(), getKeyModifiers());
	}

	/**
	 * Converts the key code to a String representation resembling the Greenfoot keys.
	 * 
	 * <ul>
	 * <li>"a", "b", .., "z" (alphabetical keys), "0".."9" (digits), most punctuation marks. 
	 * 		Also returns uppercase characters when appropriate.</li>
	 * <li>"up", "down", "left", "right" (the cursor keys)</li>
	 * <li>"enter", "space", "tab", "escape", "backspace", "shift", "control"</li>
	 * <li>"F1", "F2", .., "F12" (the function keys)</li>
	 * </ul>
	 * 
	 * @param keyCode
	 * @return the key or an empty String if the keycode was KeyEvent.CHAR_UNDEFINED.
	 */
	private String convertKeyCode(int keyCode, int keyModifiers) {
		switch (keyCode) {
		case KeyEvent.VK_LEFT: return "left";
		case KeyEvent.VK_RIGHT: return "right";
		case KeyEvent.VK_DOWN: return "down";
		case KeyEvent.VK_UP: return "up";
		case KeyEvent.VK_ENTER: return "enter";
		case KeyEvent.VK_SPACE: return "space";
		case KeyEvent.VK_TAB: return "tab";
		case KeyEvent.VK_ESCAPE: return "escape";
		case KeyEvent.VK_BACK_SPACE: return "backspace";
		case KeyEvent.VK_SHIFT: return "shift";
		case KeyEvent.VK_CONTROL: return "control";
		case KeyEvent.VK_PERIOD: return ".";
		case KeyEvent.VK_COMMA: return ",";
		case KeyEvent.VK_EXCLAMATION_MARK: return "!";
		case KeyEvent.VK_SEMICOLON: return ";";
		case KeyEvent.VK_COLON: return ":";
		case KeyEvent.CHAR_UNDEFINED: return "";
		default: 
			String letter = KeyEvent.getKeyText(keyCode);
			if (letter != null && letter.length() == 1) {
				if (keyModifiers == InputEvent.SHIFT_MASK) {
					return letter.toUpperCase();
				} else {
					return letter.toLowerCase();
				}
			}
			return "";
		}
	}

	/**
	 * Creates a new (subclass of) Kara.
	 * 
	 * @return
	 */
	private Kara createNewKaraInstance() {
		try {
			return karaClass.newInstance();
		} catch (InstantiationException e1) {
			// could not create the class
			e1.printStackTrace();
		} catch (IllegalAccessException e2) {
			// could not create the class
			e2.printStackTrace();
		}
		// returning null here will likely lead to a NullPointerException in
		// the calling class.
		return null;
	}

	/**
	 * Checks whether we can drag the object to the specified location.
	 * 
	 * @param location
	 * @return
	 */
	private boolean canAddActor(Class<? extends Actor> actorClass, Location location) {
		if (actorClass.equals(Kara.KaraDelegate.class)) {
			// Kara can't be put on a Kara, mushroom or tree
			return getOneActorAt(location, Mushroom.class) == null
					&& getOneActorAt(location, Tree.class) == null
					&& getOneActorAt(location, Kara.KaraDelegate.class) == null;

		} else if (actorClass.equals(Mushroom.class)) {
			// Mushroom can't be put on a mushroom, tree or Kara
			return getOneActorAt(location, Mushroom.class) == null
					&& getOneActorAt(location, Tree.class) == null
					&& getOneActorAt(location, Kara.KaraDelegate.class) == null;

		} else if (actorClass.equals(Tree.class)) {
			// Tree can't be put on a tree, mushroom, Kara or Leaf
			return getOneActorAt(location, Mushroom.class) == null
					&& getOneActorAt(location, Tree.class) == null
					&& getOneActorAt(location, Leaf.class) == null
					&& getOneActorAt(location, Kara.KaraDelegate.class) == null;

		} else if (actorClass.equals(Leaf.class)) {
			// Leaf can't be put on a Leaf or Tree
			return getOneActorAt(location, Tree.class) == null
					&& getOneActorAt(location, Leaf.class) == null;
		}
		return true;
	}
	
	/**
	 * Updates all the mushroom image, i.e. adds mushroom glow if mushroom
	 * is on a leaf (target) or removes the glow if not on a leaf any more.
	 */
	private void updateMushroomImages() {
		for (Actor actor : getActors(Mushroom.class)) {
			if (actor instanceof Mushroom) {
				((Mushroom) actor).updateImage();
			}
		}
	}
	
	/**
	 * Listener for mouse dragging.
	 */
	private class DragListener implements GGMouseListener {
		private Location lastLocation;
		private boolean isDragging = false;

		private Actor actor;
		private KaraWorld world;
		private boolean disableWhenRunning;
		
		/**
		 * Creates a DragListener.
		 * 
		 * @param actor
		 * @param disableWhenRunning
		 *            if true, dragging is disabled when the game is running.
		 */
		public DragListener(Actor actor, boolean disableWhenRunning) {
			this.actor = actor;
			this.world = (KaraWorld) actor.gameGrid;
			this.disableWhenRunning = disableWhenRunning;
		}

		@Override
		public boolean mouseEvent(GGMouse mouse) {
			if (disableWhenRunning && isRunning()) {
				return false;
			}
			
			Location location = world.toLocationInGrid(mouse.getX(),
					mouse.getY());

			switch (mouse.getEvent()) {
			case GGMouse.lPress:
				lastLocation = location.clone();
				
				if (world.getOneActorAt(location) == actor) {
					isDragging = true;
				}
				break;

			case GGMouse.lDrag:
				if (isDragging 
						&& world.canAddActor(actor.getClass(), location)
						&& !lastLocation.equals(location)) {
					actor.setLocation(location);
					world.updateMushroomImages();
					
					lastLocation = location.clone();

					// must do a refresh if dragging is done when gamegrid is
					// not running
					world.refresh();
				}
				break;

			case GGMouse.lRelease:
				if (isDragging) {
					actor.setLocation(lastLocation);
					isDragging = false;
				}
				break;
			}
			return false;
		}
	}

	/**
	 * Context menu: Allows creating of new actors, deleting and inspecting actors,
	 * calling Kara's methods and printing the world setup to the console.
	 */
	private class ContextMenu extends JPopupMenu {

		private KaraWorld world;
		private Location location;
		private Actor actor;

		public ContextMenu(final KaraWorld world, final Location location,
				final Actor actor) {
			this.world = world;
			this.location = location;
			this.actor = actor;

			if (actor == null) {
				createNewItems();
				addSeparator();
				
				createSaveTheWorldItems();
			} else {
				if (actor instanceof Kara.KaraDelegate) {
					createKaraItems(((Kara.KaraDelegate) actor).getKara());
				} else {
					createNewItems();
				}
				
				if (getComponentCount() > 0) {
					addSeparator();
				}
				createRemoveItem();
				createInspectItem();
			}
		}

		/**
		 * Create menu items for "new ....".
		 */
		private void createNewItems() {
			if (world.canAddActor(Tree.class, location)) {
				JMenuItem newTree = new JMenuItem("new Tree()", new ImageIcon(
						WorldImages.ICON_TREE));
				newTree.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						world.addObject(new Tree(), location.getX(),
								location.getY());
						world.refresh();
					}
				});
				add(newTree);
			}

			if (world.canAddActor(Leaf.class, location)) {
				JMenuItem newLeaf = new JMenuItem("new Leaf()", new ImageIcon(
						WorldImages.ICON_LEAF));
				newLeaf.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						world.addObject(new Leaf(), location.getX(),
								location.getY());
						world.refresh();
					}
				});
				add(newLeaf);
			}

			if (world.canAddActor(Mushroom.class, location)) {
				JMenuItem newMushroom = new JMenuItem("new Mushroom()",
						new ImageIcon(WorldImages.ICON_MUSHROOM));
				newMushroom.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						world.addObject(new Mushroom(), location.getX(),
								location.getY());
						world.refresh();
					}
				});
				add(newMushroom);
			}

			if (world.canAddActor(Kara.KaraDelegate.class, location)) {
				JMenuItem newMyKara = new JMenuItem("new MyKara()", new ImageIcon(
						WorldImages.ICON_MY_KARA));
				newMyKara.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						world.addObject(world.createNewKaraInstance().actorDelegate,
								location.getX(), location.getY());
						world.refresh();
					}
				});
				add(newMyKara);
			}
		}

		/**
		 * Create menu items with all methods of Kara.
		 */
		private void createKaraItems(final Kara kara) {
			try {
				List<Method> methods = new ArrayList<Method>();
				methods.addAll(Arrays.asList(Kara.class.getDeclaredMethods()));
				methods.addAll(Arrays.asList(world.karaClass.getDeclaredMethods()));
				
				// Sort the methods by method name
				Collections.sort(methods, new Comparator<Method>() {
					@Override
					public int compare(Method m1, Method m2) {
						return m1.getName().compareTo(m2.getName());
					}
				});
				
				// Create the menu items
				for (final Method method : methods) {
					if (method.getModifiers() == Modifier.PUBLIC) {
						JMenuItem item = new JMenuItem(
								convertMethodToString(method));
						item.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								invokeKaraMethod(method, kara);
							}
						});
						add(item);
					}
				}
			} catch (Throwable e) {
				System.err.println(e);
			}
		}
		
		/**
		 * Helper method to invoke the specified method on Kara through reflection.
		 * @param method
		 * @param kara
		 */
		private void invokeKaraMethod(Method method, Kara kara) {
			try {
				Object result = method.invoke(kara);
				if (!method.getReturnType().equals(void.class)) {
					JOptionPane.showMessageDialog(this, "<html>The method <i>" + convertMethodToString(method) 
							+ "</i> returned: <p><b>" + result.toString() + "</b></html>", "Method Result",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception ex) {
				System.err.println("Could not invoke method "
								+ convertMethodToString(method)
								+ " - only no-argument methods may be called!");
				ex.printStackTrace();
			}
		}

		/**
		 * Returns a String representation of the specified method.
		 * 
		 * @param method
		 * @return
		 */
		private String convertMethodToString(Method method) {
			StringBuffer buf = new StringBuffer();
			buf.append(method.getReturnType()).append(' ');
			buf.append(method.getName()).append('(');

			Class<?>[] classes = method.getParameterTypes();
			for (int i = 0; i < classes.length; i++) {
				if (i > 0) {
					buf.append(", ");
				}
				buf.append(classes[i].getSimpleName());
			}
			buf.append(')');
			return buf.toString();
		}

		/**
		 * Creates a menu item to remove the actor.
		 */
		private void createRemoveItem() {
			JMenuItem removeItem = new JMenuItem("Remove", new ImageIcon(
					WorldImages.ICON_DELETE));
			removeItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actor.removeSelf();
					world.refresh();
				}
			});
			add(removeItem);
		}

		/**
		 * Create a menu item to inspect the position and rotation properties
		 * of an Actor.
		 */
		private void createInspectItem() {
			JMenuItem inspectItem = new JMenuItem("Inspect", new ImageIcon(
					WorldImages.ICON_INSPECT));
			inspectItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(KaraWorld.this, "<html><b>" + actor.toString()
							+ "</b> <p> <i>x-position:</i> " + location.getX() 
							+ "<p> <i>y-position:</i> " + location.getY() 
							+ "<p> <i>rotation:</i> " + actor.getIntDirection()
							+ "</html>", actor.toString(),
							JOptionPane.INFORMATION_MESSAGE);
				}
			});
			add(inspectItem);
		}

		/**
		 * Creates menu items to print the actor positions as text to console
		 * or to a file.
		 */
		private void createSaveTheWorldItems() {
			JMenuItem printToConsoleItem = new JMenuItem(
					"Print World Setup to Console", new ImageIcon(
							WorldImages.ICON_CONSOLE));
			printToConsoleItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					printWorldSetupToConsole();
				}
			});
			add(printToConsoleItem);
			
			JMenuItem saveToFileItem = new JMenuItem(
					"Save World Setup to File", new ImageIcon(
							WorldImages.ICON_SAVE));
			saveToFileItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					saveWorldSetupToFile();
				}
			});
			add(saveToFileItem);
		}

		/**
		 * Returns a String to be copied into the prepare() method to create initial
		 * actors through the addObject(...) method.
		 * @return
		 */
		@SuppressWarnings("unused")
		private String worldSetupToString() {
			StringBuffer buf = new StringBuffer();
			buf.append("\n*** Copy the following code into the prepare() method in KaraWorld ***");

			for (Actor actor : world.getActors()) {
				buf.append("\n");
				buf.append("addObject(new ");
				if (actor instanceof Kara.KaraDelegate) {
					buf.append("MyKara");
				} else {
					buf.append(actor.getClass().getSimpleName());
				}
				buf.append("(), ");
				buf.append(actor.getLocation().getX()).append(", ")
						.append(actor.getLocation().getY());
				buf.append(");");
			}

			buf.append("\n*** End ***");

			return buf.toString();
		}
	}
}
