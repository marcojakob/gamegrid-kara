package kara.gamegrid;

import ch.aplu.jgamegrid.Actor;

/**
 * Mushrooms can be pushed by Kara if there is nothing behind them. The
 * Mushrooms must be pushed onto a target (the Leafs). If a Mushroom is on
 * target, a target image is shown.
 * 
 * @author Marco Jakob (http://edu.makery.ch)
 */
public class Mushroom extends Actor {

	/**
	 * Create a Mushroom.
	 */
	public Mushroom() {
		this(false);
	}

	/**
	 * Create a Mushroom.
	 * 
	 * @param onTarget
	 *            if true, the on-target-image is used.
	 */
	public Mushroom(boolean onTarget) {
		super(WorldImages.ICON_MUSHROOM, WorldImages.ICON_MUSHROOM_ON_TARGET);
		if (onTarget) {
			showOnTargetImage();
		}
	}

	@Override
	public String toString() {
		// used for "inspect"
		return "Mushroom";
	}

	/**
	 * If the Mushroom is on target (i.e. on a Leaf), the on-target-image,
	 * otherwise the default image is shown.
	 */
	public void updateImage() {
		if (gameGrid.getOneActorAt(getLocation(), Leaf.class) != null) {
			showOnTargetImage();
		} else {
			showDefaultImage();
		}
	}

	/**
	 * Shows the default image.
	 */
	private void showDefaultImage() {
		show(0);
	}

	/**
	 * Shows the on-target-image.
	 */
	private void showOnTargetImage() {
		show(1);
	}
}
