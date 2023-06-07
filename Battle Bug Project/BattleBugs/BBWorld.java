package BattleBugs;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;
import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Actor;

import java.util.ArrayList;

public class BBWorld extends ActorWorld {

    private int numAct;

    public BBWorld() {
        numAct = 0;
    }

    public BBWorld(Grid<Actor> grid) {
        super(grid);
        numAct = 0;
    }

    public void step() {
        Grid<Actor> gr = getGrid();
        Regulator reg = null;
        ArrayList<Actor> actors = new ArrayList<Actor>();
        ArrayList<BattleBug> bbugs = null;
        for (Location loc : gr.getOccupiedLocations()) {
            actors.add(gr.get(loc));
        }

        for (Actor a : actors) {
            if (a.getGrid() == gr && a instanceof Regulator) {
                reg = ((Regulator) a);
                a.act();
                bbugs = reg.sortedBattleBugs();
            }
        }
        for (Actor a : bbugs) {
            if (a.getGrid() != null && a.getLocation() != null) {
                a.act();
            }
        }
        for (Actor a : actors) {
            if (a.getGrid() == gr && !(a instanceof Regulator) && !(a instanceof BattleBug)) {
                a.act();
            }
        }

//        if(numAct%2==0)
//        {
//        	for (Actor a : actors)
//        	{
//            	if (a.getGrid() == gr&&!(a instanceof Regulator))
//                	a.act();
//        	}
//        }
//        else
//        {
//        	for (int i = actors.size()-1; i >=0;i--)
//        	{
//        		Actor a = actors.get(i);
//            	if (a.getGrid() == gr&&!(a instanceof Regulator))
//                	a.act();
//        	}
//        }
        if (!reg.gameOver()) {
            reg.displayStats();
        }
        numAct++;
    }
}