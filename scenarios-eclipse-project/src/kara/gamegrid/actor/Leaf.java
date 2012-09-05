package kara.gamegrid.actor;

import kara.gamegrid.world.WorldImages;
import ch.aplu.jgamegrid.Actor;

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
