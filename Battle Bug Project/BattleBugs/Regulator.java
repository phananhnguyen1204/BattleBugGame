package BattleBugs;
import java.util.ArrayList;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.awt.Color;
import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;

public class Regulator extends Actor
{
        static DJ sound;
	private int numAct;
	private Color[] powerUps;
	private int rockRow,rockCol;
	private ArrayList<BattleBug> bugs;
	private int numActsBeforeRocks = 40;
	private int numFlowersDroped = 5;
	private boolean stateWinner = false;
	private int rowAdd = 0;
	private int colAdd = 0;
        DisplayThread display;
       

	public Regulator(ArrayList<BattleBug> b)
	{
		bugs = b;
		rockRow = 0;
		rockCol = 0;
		numAct = 0;
		powerUps = new Color[3];
		powerUps[0] = Color.RED;//Str
		powerUps[1] = Color.GREEN;//Def
		powerUps[2] = Color.BLUE;//Speed
                sound =  new DJ();
                display = new DisplayThread();
                //display.start();

	}

	public void placeBugsInGrid()
	{
		ArrayList<Integer> dirs = new ArrayList<Integer>();
		ArrayList<Location> locs = new ArrayList<Location>();
		dirs.add(new Integer(Location.SOUTHEAST));
		locs.add(new Location(0,0));

		dirs.add(new Integer(Location.SOUTHWEST));
		locs.add(new Location(0,getGrid().getNumCols()-1));

		dirs.add(new Integer(Location.NORTHWEST));
		locs.add(new Location(getGrid().getNumRows()-1,getGrid().getNumCols()-1));

		dirs.add(new Integer(Location.NORTHEAST));
		locs.add(new Location(getGrid().getNumRows()-1,0));

		for(int i = 0; i < bugs.size();i++)
		{
			int r = (int)(dirs.size()*Math.random());
			bugs.get(i).putSelfInGrid(getGrid(),locs.remove(r));
			bugs.get(i).setDirection(dirs.remove(r));
                        display.addBBName(bugs.get(i).getName());
                        display.addBBLevel(bugs.get(i).level() + "");
                        display.addBBLoc(bugs.get(i).getLocation().toString());
                        display.addBBStr(bugs.get(i).getStrength() + "");
                        display.addBBDef(bugs.get(i).getDefense() + "");
                        display.addBBSpd(bugs.get(i).getSpeed() + "");
                        display.addBBKills(bugs.get(i).getKillCount() + "");
                        display.addBBAmmo(((BattleBug2012)bugs.get(i)).getAmmo() + "");
                        
		}
                display.start();
	}
	public void checkLevel5()
	{
		for(int i = 0;i<bugs.size();i++)
		{
			if(bugs.get(i).level()!=5)
			{
				bugs.get(i).removeSelfFromGrid();
				System.out.println(bugs.get(i).getName() + " was removed from the game for not starting as level 5");
			}
			else if(bugs.get(i).getStrength()<0||bugs.get(i).getDefense()<0||bugs.get(i).getSpeed()<0)
			{
				bugs.get(i).removeSelfFromGrid();
				System.out.println(bugs.get(i).getName() + " was removed from the game for being a big fat cheater");
			}
		}
	}
	public void displayStats()
	{
		System.out.println("========================================");
		System.out.println("Stats at the end of act: " + (numAct -1));
		for(BattleBug b:bugs)
			System.out.println(b);
                
                display.setAct((numAct - 1)+"");
                display.setRestartAddingTrue();
		for(int i = 0; i < bugs.size();i++)
		{
                        display.addBBName(bugs.get(i).getName());
                        display.addBBLevel(bugs.get(i).level() + "");
                        if(bugs.get(i).isDead())
                            display.addBBLoc("DEAD");
                        else
                            display.addBBLoc(bugs.get(i).getLocation().toString());
                        display.addBBStr(bugs.get(i).getStrength() + "");
                        display.addBBDef(bugs.get(i).getDefense() + "");
                        display.addBBSpd(bugs.get(i).getSpeed() + "");
                        display.addBBKills(bugs.get(i).getKillCount() + "");
                        display.addBBAmmo(((BattleBug2012)bugs.get(i)).getAmmo() + "");
                        
		}
                
	}
	public void stateWinner()
	{
		int numDead = 0;
		BattleBug alive = null;
                
		for(BattleBug b:bugs)
		{
			if(b.isDead()==true)
			{
				numDead++;
			}
			else
				alive = b;
		}

		if(numDead == bugs.size())
		{
			stateWinner = true;
			System.out.println("All lose, NO ONE WINS");
                        display.setWinner("All lose, NO ONE WINS");
                        display.gameOver();
		}
		else if(numDead == bugs.size()-1)
		{
                        display.gameOver();
			stateWinner = true;
			System.out.println(alive.getName() + " is the winner");
                        display.setWinner(alive.getName() + " is the winner");
                        if(((BattleBug2012)alive).getVictoryMusic()!=null)
                            Regulator.sound.playSongLoop(((BattleBug2012)alive).getVictoryMusic());
			System.out.println(alive.victory());
		}
	}
	public void checkForCheaters()
	{
		for(int i = 0;i<bugs.size();i++)
		{
			if(bugs.get(i).getNumAct() < numAct && !bugs.get(i).isDead())
			{
				System.out.println(bugs.get(i).getName() + " did not move() or turn() in the previous step");
				bugs.get(i).removeSelfFromGrid();
			}
			else if(bugs.get(i).getNumAct() > numAct && !bugs.get(i).isDead())
			{
				System.out.println(bugs.get(i).getName() + " used move() or turn() too many times in the previous step");
				bugs.get(i).removeSelfFromGrid();
			}
		}
	}
	public void act()
	{
		if(stateWinner == false)
		{
			stateWinner();
			checkForCheaters();
			if(numAct%10==0)
			{


				for(int i=0;i < numFlowersDroped;i++)
				{
					int n = (int)(Math.random()*3);
					PowerUp pU = new PowerUp(powerUps[n]);//Makes a random PowerUp
					int r = (int)(Math.random()*(getGrid().getNumRows()-2*rowAdd))+rowAdd;
					int c = (int)(Math.random()*(getGrid().getNumCols()-2*colAdd))+colAdd;
					Location loc = new Location(r,c);
					//System.out.println("the Random Row is " + r + " And the random Col is " + c);
					if(getGrid().isValid(loc)&&getGrid().get(loc)==null)
					{
						System.out.println("place " + pU + " in " + loc);
						pU.putSelfInGrid(getGrid(),new Location(r,c));
					}
					else
						System.out.println("Someone was is in " + loc + ".  No PowerUp droped");
				}
				for(int i=0;i < 1;i++)//red flowers
				{
					PowerUp pU = new PowerUp(Color.RED);//Makes a random PowerUp
					int r = (int)(Math.random()*(getGrid().getNumRows()-2*rowAdd))+rowAdd;
					int c = (int)(Math.random()*(getGrid().getNumCols()-2*colAdd))+colAdd;
					Location loc = new Location(r,c);
					//System.out.println("the Random Row is " + r + " And the random Col is " + c);
					if(getGrid().isValid(loc)&&getGrid().get(loc)==null)
					{
						System.out.println("place " + pU + " in " + loc);
						pU.putSelfInGrid(getGrid(),new Location(r,c));
					}
					else
						System.out.println("Someone was is in " + loc + ".  No PowerUp droped");
				}
				//System.out.println("rowAdd = " + rowAdd + " colAdd= " + colAdd);

			}
			if(numAct%numActsBeforeRocks==0 && numAct!=0)
			{
				for(int i = rockCol; i < getGrid().getNumCols()-rockCol;i++)
				{
					Location loc = new Location(rockRow,i);
					if(getGrid().get(loc)!=null&&!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
						getGrid().get(loc).removeSelfFromGrid();
					if(!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
					{
						Rock rock = new Rock();
						rock.putSelfInGrid(getGrid(), loc);
					}
				}
				for(int i = rockCol; i < getGrid().getNumCols()-rockCol;i++)
				{
					Location loc = new Location(getGrid().getNumRows()-rockRow-1,i);
					if(getGrid().get(loc)!=null&&!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
						getGrid().get(loc).removeSelfFromGrid();
					if(!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
					{
						Rock rock = new Rock();
						rock.putSelfInGrid(getGrid(), loc);
					}
				}
				rockRow++;

				for(int i = rockRow; i < getGrid().getNumCols()-rockRow;i++)
				{
					Location loc = new Location(i,rockCol);
					if(getGrid().get(loc)!=null&&!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
						getGrid().get(loc).removeSelfFromGrid();
					if(!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
					{
						Rock rock = new Rock();
						rock.putSelfInGrid(getGrid(), loc);
					}
				}
				for(int i = rockRow; i < getGrid().getNumCols()-rockRow;i++)
				{
					Location loc = new Location(i,getGrid().getNumRows()-rockCol-1);
					if(getGrid().get(loc)!=null&&!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
						getGrid().get(loc).removeSelfFromGrid();
					if(!(getGrid().get(loc) instanceof Regulator)&&!(getGrid().get(loc) instanceof Rock) && !(getGrid().get(loc) instanceof TombStone))
					{
						Rock rock = new Rock();
						rock.putSelfInGrid(getGrid(), loc);
					}
				}
				rockCol++;
				rowAdd++;
				colAdd++;
			}

			numAct++;
		}

	}
	public boolean gameOver()
	{
		return stateWinner;
	}
	public ArrayList<BattleBug> sortedBattleBugs()
	{
		int max, maxPos;
		ArrayList<BattleBug> sorted = new ArrayList<BattleBug>();
		for(int k = 0;k<bugs.size();k++)
		{
			sorted.add(bugs.get(k));
		}
		for(int i = 0;i<sorted.size() - 1;i++)
		{
			max = sorted.get(i).getSpeed();
			maxPos = i;
			for(int j = i+1;j<sorted.size();j++)
			{
				if(sorted.get(j).getSpeed() > max)
				{
					max = sorted.get(j).getSpeed();
					maxPos = j;
				}

			}
			BattleBug temp = sorted.get(i);
			sorted.set(i, sorted.get(maxPos));
			sorted.set(maxPos, temp);
		}
		return sorted;

	}
        
        //added 5/11/2015
        public boolean getStateWinner()
        {
            return stateWinner;
        }
        public int getNumAct()
        {
            return numAct;
        }
        public void incNumAct()
        {
            numAct++;
        }
}