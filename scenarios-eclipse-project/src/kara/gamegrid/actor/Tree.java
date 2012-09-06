package kara.gamegrid.actor;

import kara.gamegrid.world.WorldImages;
import ch.aplu.jgamegrid.Actor;

/**
 * A tree is a barrier for Kara. Kara can neither move through nor push trees.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class Tree extends Actor {
	
	/**
	 * Constructor.
	 */
	public Tree() {
		super(WorldImages.ICON_TREE);
	}
	
	@Override
	public String toString() {
		// used for "inspect"
		return "Tree";
	}
}
