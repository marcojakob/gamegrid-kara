package kara.gamegrid.sokoban;

import java.awt.Font;

import kara.gamegrid.world.GameScreen;


/**
 * This Label can be used to react to keyboard input and display it.
 * 
 * @author Marco Jakob
 */
public class InputLabel extends Label {
	private int maxLength = -1;
	private boolean textChanged = false;
	private boolean allowSpace = true;
	private boolean allowBackspace = true;

	/**
	 * Constructor for an InputLabel.
	 */
	public InputLabel(GameScreen gameScreen, String text, int width,
			int height, Font font) {
		super(gameScreen, text, width, height, font);
	}

	/**
	 * Sets the maximum length of the input String.
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Sets whether spaces should be allowed as input.
	 */
	public void setAllowSpace(boolean allowSpace) {
		this.allowSpace = allowSpace;
	}

	/**
	 * Sets whether backspaces should be allowed as input.
	 */
	public void setAllowBackspace(boolean allowBackspace) {
		this.allowBackspace = allowBackspace;
	}

	/**
	 * Returns whether the text was changed.
	 */
	public boolean wasTextChanged() {
		boolean result = textChanged;
		textChanged = false;
		return result;
	}

	/**
	 * Changes the label text according to the pressed key.
	 * 
	 * Only single characters are supported. "space" and "backspace" can
	 * optionally be supported.
	 */
	public void handleKeyPress(String key) {
		if (key != null) {
			if ((maxLength == -1 || getText().length() < maxLength)
					&& key.length() == 1) {
				setText(getText() + key);
				textChanged = true;
			} else if (allowBackspace && key.equals("backspace")) {
				// delete character
				if (getText().length() > 0) {
					setText(getText().substring(0, getText().length() - 1));
					textChanged = true;
				}
			} else if (allowSpace
					&& (maxLength == -1 || getText().length() < maxLength)
					&& key.equals("space")) {
				setText(getText() + " ");
			}
		}
	}
}
