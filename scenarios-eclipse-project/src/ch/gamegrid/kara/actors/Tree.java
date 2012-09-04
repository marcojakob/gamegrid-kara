package ch.gamegrid.kara.actors;

import ch.aplu.jgamegrid.Actor;
import ch.gamegrid.kara.world.WorldImages;

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
		super(WorldImages.TREE);
	}
	
	@Override
	public String toString() {
		return "Tree";
	}
}
