# GameGridKara

GameGridKara is a combination of the mini-world [Kara](http://www.swisseduc.ch/compscience/karatojava/index.html) and the [JGameGrid](http://www.aplu.ch/home/apluhomex.jsp?site=45) library to teach introductory programming in Java. All scenarios can be used with any IDE like Eclipse, Netbeans or BlueJ.


## Why GameGrid and Kara?
The concept of Kara has proven itself over years for motivating beginning programmers. Kara is a programmable ladybug, who lives in a simple graphical world and must solve problems of varying difficulty, for example, collect leafs or find his way through a labyrinth.

JGameGrid is a class library that can be used to create a game playground based on grid cells. JGameGrid is inspired by [Greenfoot](http://www.greenfoot.org/) but can be used with a single `.jar` which would not be possible with Greenfoot.

**JGameGrid provides visual and interactive Kara scenarios to program in any IDE like Eclipse, Netbeans or BlueJ.**


## Teaching Resources ##
To download the handouts and GameGridKara scenarios go to the following websites:
* [GameGridKara Downloas in English](http://www.swisseduc.ch/informatik/karatojava/gamegridkara/gamegridkara-english.html)
* [GameGridKara Downloads in German](http://www.swisseduc.ch/informatik/karatojava/gamegridkara/index.html)

To stay informed about new releases of GameGridKara take a look at my blog:
* [Edu Makery Blog](http://edu.makery.ch)


## How to use the Source on GitHub ##
This project on GitHub contains an eclipse project that contains the source of GameGridKara and all the scenarios. There is a source folder for the Kara classes and a source folder for the scenarios of each chapter.

To build all scenarios you should use the Ant build file (build.xml). This will ...
* ... create a target folder containing an eclipse project for each scenario (with and without solutions)
* ... create another folder containing zip files with scenarios zipped by chapter


## Feedback and Bug Report ##
You can leave comments on the Swisseduc pages (see links under *Teaching Resources* above) or on my blog.

For Bug Reports you should create a [new issue](https://github.com/marcojakob/gamegrid-kara/issues).