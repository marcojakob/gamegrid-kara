package kara.gamegrid;

import ch.aplu.jgamegrid.Actor;

/**
 * A leaf can be put and removed by Kara.
 * 
 * @author Marco Jakob (http://edu.makery.ch)
 */
public class Leaf extends Actor {

	/**
	 * Constructor.
	 */
	public Leaf() {
		super(WorldImages.ICON_LEAF);
	}
	
	@Override
	public String toString() {
		// used for "inspect"
		return "Leaf";
	}
}
