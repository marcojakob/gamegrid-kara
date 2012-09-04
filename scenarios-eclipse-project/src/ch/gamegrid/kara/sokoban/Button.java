package ch.gamegrid.kara.sokoban;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseTouchListener;
import ch.aplu.jgamegrid.GameGrid;

/**
 * A simple button.
 * 
 * @author Marco Jakob (Button inspired by Taylor Borne)
 */
public class Button extends Label implements GGMouseTouchListener {
	private boolean hover = false;
	private boolean pressed = false;
	private boolean clicked = false;
	private boolean enabled = true;

	/**
	 * Constructor for a button.
	 */
	public Button(GameGrid gameGrid, String text, int width, int height, Font font) {
		super(gameGrid, text, width, height, font);
		addMouseListener();
	}

	/**
	 * Constructor for a button with only an icon.
	 */
	public Button(GameGrid gameGrid, int width, int height, BufferedImage icon) {
		super(gameGrid, width, height, icon);
		addMouseListener();
	}
	
	private void addMouseListener() {
		addMouseTouchListener(this, GGMouse.enter | GGMouse.leave | GGMouse.lClick);
	}

	@Override
	public void mouseTouched(Actor actor, GGMouse mouse, Point spot) {
		if (enabled && isRunning()) {
			boolean last = hover;
			boolean lastP = pressed;
			
			switch (mouse.getEvent()) {
			case GGMouse.enter: 
				hover = true;
				break;
				
			case GGMouse.leave: 
				hover = false;
				break;
				
			case GGMouse.lPress:
				pressed = true;
				break;
			
			case GGMouse.lClick: 
				clicked = true;
				break;
				
			case GGMouse.lRelease: // || dragEnded any where??
				pressed = false; 
				break;
			}
			
			if (last != hover || lastP != pressed) {
				// state changed
				createImage();
			}
		}
	}

	/**
	 * Override the getter of the superclass to support reaction to mouse
	 * events.
	 */
	public Color getTextColor() {
		if (!enabled) {
			return new Color(102, 102, 102);
		}

		return super.getTextColor();
	}

	/**
	 * Override the getter of the superclass to support reaction to mouse
	 * events.
	 */
	public Color getBorderColor() {
		if (pressed) {
			if (super.getBorderColor() != null) {
				if (isBrightColor(super.getBorderColor())) {
					return Color.DARK_GRAY;
				} else {
					return Color.LIGHT_GRAY;
				}
			} else {
				return Color.GRAY;
			}
		}

		return super.getBorderColor();
	}

	/**
	 * Override the getter of the superclass to support reaction to mouse
	 * events.
	 */
	public Color getBackgroundColor() {
		if (hover) {
			if (isBrightColor(super.getBackgroundColor())) {
				return super.getBackgroundColor().darker();
			} else {
				return super.getBackgroundColor().brighter();
			}
		}

		return super.getBackgroundColor();
	}

	/**
	 * Returns whether the Button was clicked.
	 */
	public boolean wasClicked() {
		boolean c = clicked;
		clicked = false;
		return c;
	}

//	/**
//	 * This method is called by Greenfoot when this Button was added to the
//	 * world.
//	 */
//	public void addedToWorld(World world) {
//		hover = false;
//		createImage();
//	}

	/**
	 * Set whether the Button is enabled.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		createImage();
	}

	/**
	 * Returns whether the specified color is a bright color.
	 */
	private boolean isBrightColor(Color color) {
		return (color.getRed() + color.getGreen() + color.getBlue()) > 384;
	}
}