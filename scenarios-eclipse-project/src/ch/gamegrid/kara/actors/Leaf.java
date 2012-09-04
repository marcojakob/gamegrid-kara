package ch.gamegrid.kara.actors;

import ch.aplu.jgamegrid.Actor;
import ch.gamegrid.kara.world.WorldImages;

/**
 * A leaf can be put and removed by Kara.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class Leaf extends Actor {

	/**
	 * Constructor.
	 */
	public Leaf() {
		super(WorldImages.LEAF);
	}
	
	@Override
	public String toString() {
		return "Leaf";
	}
}
