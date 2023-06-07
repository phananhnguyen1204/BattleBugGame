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
 * Use this class to test code/methods that you just created.  Make your own scenarios
 * by placing bugs/powerups where ever you want
 */
public class CodeTesterDriver {
    //Must be 3x3 or larger

    private static final int DEFAULT_ROWS = 27;
    private static final int DEFAULT_COLS = 27;

    public static void main(String[] args) {
        ArrayList<BattleBug> battleBugs = new ArrayList<BattleBug>();
        Grid<Actor> gr = new BoundedGrid<Actor>(DEFAULT_ROWS, DEFAULT_COLS);
        BBWorld world = new BBWorld(gr);
        

        //Create your BattleBugs here, and place them in any location you please.
        //Make sure you add them to bbs after you create/place them.
        //Create your BattleBug
        BBExample bb1 = new BBExample(10, 2, 0, "b1", Color.BLACK);
        BBExample bb2 = new BBExample(4, 3, 3, "b2", Color.GREEN);
        //UltraliskBug ult = new UltraliskBug(50, 3, 3, "b2", Color.GREEN, 27,27);     
        
        //Needed regulator code
        battleBugs.add(bb1);
        battleBugs.add(bb2);   
        //battleBugs.add(ult);
        Regulator theBoss = new Regulator(battleBugs);
        world.add(new Location(DEFAULT_ROWS/2, DEFAULT_COLS/2), theBoss);
        
        //Place the BB where you want and the in direction that you want.
        //This is for testing specific scenarios
        world.add(new Location(10, 10), bb1);
        world.add(new Location(11, 9), bb2);
        //world.add(new Location(11, 10), ult);
        bb1.setDirection(225);
        
        //OR let the Regulator put the bugs in like in a real game.
        //theBoss.placeBugsInGrid();
        //theBoss.checkLevel5();



        System.out.println(bb1.measurementOfAngleBetween(-225, 540));
        world.show();
    }
}


 /* Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY,
                Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.RED, Color.WHITE, Color.YELLOW */