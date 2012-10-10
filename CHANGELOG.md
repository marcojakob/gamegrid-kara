# Changelog #

## GameGridKara NOT YET RELEASED ##

* Update to JGameGrid version 2.23. This simplifies drag-and-drop and context-menu
  handling because the getActor... methods will always return the topmost actor
  first. This wasn't the case before and had to be handled in the GameGridKara code.


## GameGridKara 2.0, 2012-10-01 ##

* Custom replacement images can be placed in a folder called "images". See new class 
	WorldImages.
* Background is now loaded from the field.png file and used as tile. This can also
	be changed by placing the corresponding file in the "images" folder.
* Changed the naming of Scenario to WorldSetup (for the reading from the world setup 
	text file).
* Changed package naming. Now we have a package per scenario.
* Put world setup text files inside scenario package.
* Generic way to generate a WorldSetup from a text file (see WorldSetup class). A 
	similar way was previously used for Sokoban levels and is now used also for 
	all other scenarios as well.
* Ability to save the current WorldSetup set-up to a text file.
* Support for multiple scenarios for one exercise: If a WorldSetup text file contains
	multile WorldSetups, a drop-down box is displayed at startup to choose one of the
	WorldSetups. 
* Added a title in the title bar.
* Added Kara icon to frame.
* Disable context menu for non-developer mode in sokoban.
* Enabled wildcard (* and ?) filename patterns to find world setup files.
* Switching to Java 6 compliance because of Greenfoot. Greenfoot is not
	able to use Java 7 at the moment.
* Made some adjustments to have similar code as GreenfootKara.
* Kara now has a stop() method to stop the simulation cycle. Now we can always use the
	run button.
* Kara Sokoban contains now an example of alternative images (skin). See Kara Sokoban
	Solution.

	
## GameGridKara 1.3.3 ##

* Drag-and-Drop improvements: Enable or disable Actor dra-and-drop in World class.
* Solved bug in context-menu: Sometimes an new ...() entry was in the context menu 
	even if it is not possible to add such an actor at this location.
* Solved bug in drag-and-drop: Sometimes when dragging an actor, the actor that is 
	not on top was picked.
* Mushroom glow when dragging: Updating the mushroom glow (on-target) whenever an 
	object is dragged or a new object is created.
* Removed restriction of max level 105 for the sokoban game (this was necessary for 
	greenfoots online highscore)