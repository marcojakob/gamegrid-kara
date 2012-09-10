package kara.gamegrid.sokoban;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBitmap;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

/**
 * This is a Label.
 * 
 * @author Marco Jakob (Label inspired by Taylor Borne)
 */
public class Label {
	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;

	protected int width;
	protected int height;
	private Font font = GameScreen.FONT_S;
	private Color backgroundColor = Color.WHITE;
	private int backgroundTransparency = 255;
	private Color textColor = Color.BLACK;
	private Color borderColor = Color.BLACK;
	private BufferedImage icon;
	private boolean iconVisible = false;
	private boolean visible = true;
	private int align = ALIGN_CENTER;
	private int inset = 8;

	private String text = "";

	private GameGrid gameGrid;
	private Actor actor = new Actor();
	private Map<GGMouseTouchListener, Integer> mouseTouchListeners = new HashMap<GGMouseTouchListener, Integer>();

	/**
	 * Constructor for a label.
	 */
	public Label(GameGrid gameGrid, String text, int width, int height, Font font) {
		this.gameGrid = gameGrid;
		this.text = text;
		this.width = width;
		this.height = height;
		this.font = font;

		createImage();
	}

	/**
	 * Constructor for a label with only an icon.
	 */
	public Label(GameGrid gameGrid, int width, int height, BufferedImage icon) {
		this.gameGrid = gameGrid;
		this.width = width;
		this.height = height;

		setIcon(icon);

		createImage();
	}

	/**
	 * Sets whether this label should be visible or not.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		if (visible) {
			actor.show();
		} else {
			actor.hide();
		}
	}

	/**
	 * Returns true if this label is visible
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Returns true, if the game is running.
	 */
	public boolean isRunning() {
		return gameGrid.isRunning();
	}

	/**
	 * Sets the alignement of the text and icon. ALIGN_LEFT, ALIGN_CENTER or
	 * ALIGN_RIGHT.
	 */
	public void setAlign(int align) {
		this.align = align;
		createImage();
	}

	/**
	 * Sets the inset to the border and between text and icon.
	 */
	public void setInset(int inset) {
		this.inset = inset;
		createImage();
	}

	/**
	 * Sets the icon.
	 */
	public void setIcon(BufferedImage icon) {
		this.icon = icon;
		if (icon != null) {
			iconVisible = true;
		} else {
			iconVisible = false;
		}
		createImage();
	}

	/**
	 * Set if the icon should be visible.
	 */
	public void setIconVisible(boolean visible) {
		this.iconVisible = visible;
		createImage();
	}

	/**
	 * Returns whether the icon is visible.
	 */
	public boolean isIconVisible() {
		return iconVisible;
	}

	/**
	 * Returns the label text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the text for the label.
	 */
	public void setText(String text) {
		this.text = text;
		createImage();
	}

	/**
	 * Set the font.
	 */
	public void setFont(Font font) {
		this.font = font;
		createImage();
	}

	/**
	 * Returns the font.
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Set the background color.
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		createImage();
	}

	/**
	 * Returns the background color.
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set the background transparancy (between 0 and 255).
	 */
	public void setBackgroundTransparency(int backgroundTransparency) {
		this.backgroundTransparency = backgroundTransparency;
		createImage();
	}

	/**
	 * Returns the background transparency.
	 */
	public int getBackgroundTransparency() {
		return backgroundTransparency;
	}

	/**
	 * Set the color of the text.
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		createImage();
	}

	/**
	 * Returns the text color.
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * Set the color of the border. If set to null, no border is created.
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		createImage();
	}

	/**
	 * Returns the border color.
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * Paint the background. Is returned as a GGBitmap.
	 */
	protected BufferedImage paintBackground() {
		Color bg = getBackgroundColor();
		Color bgColorWithTransparency = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), getBackgroundTransparency());
		return new GGBitmap(width, height, bgColorWithTransparency).getBufferedImage();
	}

	/**
	 * Paint the border. Is returned as a GGBitmap.
	 */
	protected BufferedImage paintBorder() {
		GGBitmap pic = new GGBitmap(width, height);
		if (getBorderColor() != null) {
			pic.setPaintColor(getBorderColor());
			pic.drawRectangle(new Point(0, 0), new Point(width - 1, height - 1));
		}
		return pic.getBufferedImage();
	}

	/**
	 * Paint the text. 
	 */
	protected BufferedImage paintTextAndIcon() {
		GGBitmap pic = new GGBitmap(width, height);
		Graphics2D graphics = (new GGBitmap(1, 1)).getContext();
		graphics.setFont(getFont());
		FontMetrics fm = graphics.getFontMetrics();
		pic.setPaintColor(getTextColor());
		if (getFont() != null) {
			pic.setFont(getFont());
		}

		// calculate x positions of text and icon
		int textX = 0;
		int iconX = 0;
		switch (align) {
		case ALIGN_LEFT:
			textX = inset;
			if (iconVisible && icon != null) {
				textX += icon.getWidth() + inset;
				iconX = inset;
			}
			break;

		case ALIGN_CENTER:
			textX = (width - fm.charsWidth((getText()).toCharArray(), 0,
					(getText()).length())) / 2;
			if (iconVisible && icon != null) {
				iconX = textX - (icon.getWidth() / 2) - (inset / 2);
				textX += icon.getWidth() / 2 + (inset / 2);
			}
			break;

		case ALIGN_RIGHT:
			textX = (width - fm.charsWidth((getText()).toCharArray(), 0,
					(getText()).length())) - inset;
			if (iconVisible && icon != null) {
				iconX = textX - icon.getWidth() - inset;
			}
			break;
		}

		// calculate y positions for text and icon
		int textY = (height / 2 - 1) - ((fm.getAscent() + fm.getDescent()) / 2)
				+ fm.getAscent();
		int iconY = 0;
		if (iconVisible && icon != null) {
			iconY = height / 2 - icon.getHeight() / 2 + 1;
			pic.drawImage(icon, iconX, iconY);
		}

		if (!getText().isEmpty()) {
			pic.drawText(getText(), new Point(textX, textY));
		}
			

		graphics.dispose();
		return pic.getBufferedImage();
	}

	/**
	 * Paint and return the Image.
	 */
	protected BufferedImage paintImage() {
		GGBitmap pic = new GGBitmap(width, height);

		pic.drawImage(paintBackground(), 0, 0);
		pic.drawImage(paintTextAndIcon(), 0, 0);
		pic.drawImage(paintBorder(), 0, 0);

		return pic.getBufferedImage();
	}

	/**
	 * Create the image. Creates a new actor because we can't change the actors image.
	 */
	public void createImage() {
		if (actor.gameGrid != null) {
			// is in world!
			Location location = actor.getLocation();
			
			// remove old actor
			removeFromWorld();
			
			actor = new Actor(paintImage());
			
			// add new actor
			addToWorld(location.getX(), location.getY());
			
		} else {
			// actor is not in world
			actor = new Actor(paintImage());
		}
		
		// re-attach the listeners to new actor
		for (Map.Entry<GGMouseTouchListener, Integer> entry : mouseTouchListeners.entrySet()) {
			actor.addMouseTouchListener(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Adds this label to the world.
	 * 
	 * @param x
	 *            the x position of the actor in the grid.
	 * @param y
	 *            the y position of the actor in the grid.
	 */
	public void addToWorld(int x, int y) {
		if (actor.gameGrid == null) {
			// has not been added before --> add it
			gameGrid.addActor(actor, new Location(x, y));
		}
	}
	
	/**
	 * Removes this label from the world.
	 */
	public void removeFromWorld() {
		gameGrid.removeActor(actor);
	}

	public void addMouseTouchListener(GGMouseTouchListener listener, int mouseEventMask) {
		// keep a reference to the listeners
		mouseTouchListeners.put(listener, mouseEventMask);
		
		actor.addMouseTouchListener(listener, mouseEventMask);
	}
}