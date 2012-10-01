package kara.gamegrid;

import ch.aplu.jgamegrid.Actor;

/**
 * A tree is a barrier for Kara. Kara can neither move through nor push trees.
 * 
 * @author Marco Jakob (http://edu.makery.ch)
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
