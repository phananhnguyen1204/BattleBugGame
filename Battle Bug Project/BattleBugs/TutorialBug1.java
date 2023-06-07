package BattleBugs;
import java.util.ArrayList;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.awt.Color;
import info.gridworld.actor.Actor;

public class TutorialBug1 extends BattleBug2012
{
    public TutorialBug1(int str, int def, int spd, String name, Color col)
    {
            super(str, def, spd, name, col);
    }
    public void act()
    {
        //declare a Location named goTo and initialize it with the location (0,5)


        //Find the direction towards goTo by calling the getDirectionToward() method and store the result in a variable named dir.


        //Using the getDirection() method check to see if your bug is facing the desired direction dir
        //If so then call the move() method
        //if not then call turnTo() method towards the desired direction dir.




    }
}