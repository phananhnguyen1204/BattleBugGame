package BattleBugs;
import java.awt.Color;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;
import info.gridworld.actor.Rock;

public class TombStone extends Rock
{
	String name;
	int numAct;
	int lvl;

	public TombStone(Color c, String n, int nA, int l)
	{
		super(c);
		name = n;
		numAct = nA;
		lvl = l;
	}
	public String toString()
	{
		String ret;
		if(lvl < 8)
			ret = "a TombStone which reads: Here lies the simple bug named " + name + ".  Passed away in act # " + numAct;
		else if(lvl < 20)
			ret = "a TombStone which reads: Here lies the great bug named " + name + ".  Passed away in act # " + numAct;
		else
			ret = "a TombStone which reads: Here lies the 1337 bug named " + name + ".  Passed away in act # " + numAct;
		return ret;
	}
	public void act()
	{
		//setDirection(getDirection()+45);
	}
}