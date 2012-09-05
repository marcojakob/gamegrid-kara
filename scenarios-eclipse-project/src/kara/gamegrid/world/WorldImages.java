package kara.gamegrid.world;

import java.awt.image.BufferedImage;

import ch.aplu.jgamegrid.GGBitmap;

/**
 * Manages all icons for the world. The icons are loaded first from the relative
 * path "images". In this folder one may put replacment images for the
 * standard Kara images. They must be named as follows:
 * <ul>
 * <li>images/leaf.png</li>
 * <li>images/mushroom.png</li>
 * <li>images/mushroom_on_target.png</li>
 * <li>images/tree.png</li>
 * <li>images/kara.png</li>
 * <li>images/field.png</li>
 * </ul>
 * 
 * If no replacement images are found in the folder "images", the standard
 * images in the folder "default_images" are used.
 * 
 * @author Marco Jakob (majakob@gmx.ch)
 */
public class WorldImages {
	private static final String USER_IMAGE_PATH = "images";
	private static final String DEFAULT_IMAGE_PATH = "default_images";
	
	// background icon
	public static final String BACKGROUND_FIELD_PATH = loadPath("field.png");
	public static final BufferedImage BACKGROUND_FIELD = loadImage("field.png");
	
	// all actor icons
	public static final BufferedImage LEAF = loadImage("leaf.png");
	public static final BufferedImage MUSHROOM = loadImage("mushroom.png");
	public static final BufferedImage MUSHROOM_ON_TARGET = loadImage("mushroom_on_target.png");
	public static final BufferedImage TREE = loadImage("tree.png");
	public static final BufferedImage KARA = loadImage("kara.png");
	
	// general icons
	public static final BufferedImage GENERAL_CONSOLE = loadImage("crystal_console.png");
	public static final BufferedImage GENERAL_DELETE = loadImage("crystal_delete.png");
	public static final BufferedImage GENERAL_INSPECT = loadImage("crystal_inspect.png");
	public static final BufferedImage GENERAL_SAVE = loadImage("crystal_save.png");
	
	// Sokoban Game images
	public static final String ICON_START_SCREEN_PATH = loadPath("start_screen.png");
	public static final BufferedImage ICON_START = loadImage("newmooon_start.png");
	public static final BufferedImage ICON_OK = loadImage("newmooon_ok.png");
	public static final BufferedImage ICON_ARROW_RIGHT = loadImage("newmooon_arrow_right.png");
	public static final BufferedImage ICON_ARROW_LEFT = loadImage("newmooon_arrow_left.png");
	public static final BufferedImage ICON_LOCKED = loadImage("newmooon_locked.png");
	public static final BufferedImage ICON_HOME = loadImage("newmooon_home.png");
	public static final BufferedImage ICON_RELOAD = loadImage("newmooon_reload.png");
	public static final BufferedImage ICON_FLAG = loadImage("fatcow_flag.png");
	public static final BufferedImage ICON_TROPHY = loadImage("impressions_trophy.png");
	public static final BufferedImage ICON_HIGHSCORE = loadImage("icon_highscore.png");
	public static final BufferedImage ICON_GOLD = loadImage("fatcow_star_gold.png");
	public static final BufferedImage ICON_SILVER = loadImage("fatcow_star_silver.png");
	public static final BufferedImage ICON_BRONZE = loadImage("fatcow_star_bronze.png");
	
    
	/**
	 * Tries to load the image path from the path "images" (usually outside the
	 * jar) first. If there is no image in this folder, the default path
	 * "default_images" (usually inside the jar) is used to load the image.
	 * 
	 * @param imageName the name of the image
	 * @return the loaded image or null, if it could not be found
	 */
    private static String loadPath(String fieldName) {
    	// first try to load from replacement path
    	String fieldPath = USER_IMAGE_PATH + "/" + fieldName;
    	if (GGBitmap.getImage(fieldPath) != null) {
    		return fieldPath;
    	} else {
    		return DEFAULT_IMAGE_PATH + "/" + fieldName;
    	}
    }
    
	/**
	 * Tries to load the image from the path "images" (usually outside the
	 * jar) first. If there is no image in this folder, the default path
	 * "default_images" (usually inside the jar) is used to load the image.
	 * 
	 * @param imageName the name of the image
	 * @return the loaded image or null, if it could not be found
	 */
    private static BufferedImage loadImage(String imageName) {
    	// first try to load from replacement path
    	BufferedImage image = GGBitmap.getImage(USER_IMAGE_PATH + "/" + imageName);
    	if (image == null) {
    		// if not available load from default path
    		image = GGBitmap.getImage(DEFAULT_IMAGE_PATH + "/" + imageName);
    	}
    	
    	return image;
    }
}
