package BattleBugs;
import java.util.ArrayList;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.awt.Color;
import info.gridworld.actor.Actor;

public class BBExample extends BattleBug2012
{
	public BBExample(int str, int def, int spd, String name, Color col)
	{
		super(str, def, spd, name, col);
	}
	public void act()
	{
		
		ArrayList<Actor> actors = getActors();
		int[] dirs = {90,-90,45,-45,135,-135,0};
		if(getNumAct()%4==3)
		{
			int d = (int)(dirs.length*Math.random());
			turn(dirs[d]);
		}
		else if(getSpeed()<10)
			move();
		else
		{
			double r = Math.random();
			if(r<.75)
			{
				move2();
                        }
			else
				move();
		}
		attack();
	}
}