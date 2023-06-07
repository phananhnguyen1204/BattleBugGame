package BattleBugs;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;
import info.gridworld.grid.Grid;
import info.gridworld.grid.BoundedGrid;
import java.awt.Color;
import java.util.ArrayList;
import info.gridworld.actor.Actor;

/**
 * This class runs a BattleBugWorld. <br />
 * Use this class to test a real game scenario
 */
public class GameDriver1Test
{
    //Must be 3x3 or larger
    private static final int DEFAULT_ROWS = 27;
    private static final int DEFAULT_COLS = 27;


    public static void main(String[] args)
    {

    	ArrayList<BattleBug> battleBugs = new ArrayList<BattleBug>();
    	Grid<Actor> gr = new BoundedGrid<Actor>(DEFAULT_ROWS, DEFAULT_COLS);
        BBWorld world = new BBWorld(gr);

        //This is where you enter your Bugs into the game
        battleBugs.add(new phanfromvietnam(0,3,2, "BBExample1", null));
	battleBugs.add(new BBExample(2,2,1, "BBExample2", null));
	battleBugs.add(new BBExample(0,4,1, "BBExample3", null));
	battleBugs.add(new BBExample(0,5,0, "BBExample4", Color.RED));
        Regulator theBoss = new Regulator(battleBugs);

        world.add(new Location(DEFAULT_ROWS/2, DEFAULT_COLS/2), theBoss);
        theBoss.placeBugsInGrid();
        theBoss.checkLevel5();
        world.show();
    }
}


 /* Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY,
                Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.RED, Color.WHITE, Color.YELLOW */