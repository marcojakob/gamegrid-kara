# Changelog

## GameGridKara 1.3.4

* Custom replacement images can be placed in a folder called "images". See new class 
	WorldImages.
* Background is now loaded from the field.png file and used as tile. This can also
	be changed by placing the corresponding file in the "images" folder.
* Generic way to generate a Scenario from a text file (see Scenario class). A 
	similar way was previously used for Sokoban levels and is now used also for 
	all other scenarios as well.
* Ability to save the current scenario set-up to a text file.
* Support for multiple scenarios for one exercise: If a scenario text file contains
	multile scenarios, a drop-down box is displayed at startup to choose one of the
	scenarios. 
* Added a title in the title bar.
* Disable context menu for non-developer mode.
* Changed the naming of scenario to world setup. "Scenario" was confusing because 
	in Greenfoot projects are also called scenarios.


## GameGridKara 1.3.3 

* Drag-and-Drop improvements: Enable or disable Actor dra-and-drop in World class.
* Solved bug in context-menu: Sometimes an new ...() entry was in the context menu 
	even if it is not possible to add such an actor at this location.
* Solved bug in drag-and-drop: Sometimes when dragging an actor, the actor that is 
	not on top was picked.
* Mushroom glow when dragging: Updating the mushroom glow (on-target) whenever an 
	object is dragged or a new object is created.
* Removed restriction of max level 105 for the sokoban game (this was necessary for 
	greenfoots online highscore)