package BattleBugs;
import java.util.ArrayList;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.awt.Color;
import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import javax.swing.JOptionPane;

public class TutorialRegulator extends Regulator
{

        
        //added for tutorial Regulator
        private int tutorial;
        private Location rememberPULoc = null;
        private int numFlowersDroped = 0;
        BattleBug2012 b;
	public TutorialRegulator(ArrayList<BattleBug> b, int t)
	{
            super(b);
		
                
            tutorial = t;
            if(tutorial == 2)
            {
                numFlowersDroped = 1;
            }

	}
	
	
	
	public void stateWinner()
	{
            if(tutorial == 1)
            {
                if(getGrid().get(new Location(0,5)) instanceof TutorialBug1)
                {
                    super.stateWinner();
                    JOptionPane.showMessageDialog(null, "CONGRATULATIONS YOU ARE DONE WITH TUTORIAL 1!!\nMove on to Tutorial 2 where you will learn how to pick up PowerUps");
                    System.out.println("CONGRATULATIONS YOU ARE DONE WITH TUTORIAL 1!!");
                    System.out.println("Move on to Tutorial 2 where you will learn how to pick up PowerUps");
                }
            }
            else if(tutorial==2)
            {
                if(rememberPULoc!=null&&!(getGrid().get(rememberPULoc) instanceof PowerUp))
                {
                    super.stateWinner();
                    JOptionPane.showMessageDialog(null, "CONGRATULATIONS YOU ARE DONE WITH TUTORIAL 2!!\nMove on to Tutorial 3 where you will learn how to attack");
                    System.out.println("CONGRATULATIONS YOU ARE DONE WITH TUTORIAL 2!!");
                    System.out.println("Move on to Tutorial 3 where you will learn how to attack");
                }
            }
            else if(tutorial == 3)
            {
                if(rememberPULoc!=null&&!(getGrid().get(rememberPULoc) instanceof PowerUp) &&!b.isDead())
                {
                    super.stateWinner();
                    JOptionPane.showMessageDialog(null, "You got the PowerUp BEFORE killing the Bug.  You fail, try again");
                    System.out.println("You got the PowerUp BEFORE killing the Bug.  You fail, try again");
                    
                }
                else if(rememberPULoc!=null&&!(getGrid().get(rememberPULoc) instanceof PowerUp) &&b.isDead())
                {
                    super.stateWinner();
                    JOptionPane.showMessageDialog(null, "CONGRATULATIONS YOU ARE DONE WITH TUTORIAL 3!!\nYou are now ready for BattleBugs4!!\n GOOD LUCK!!");
                }
            }
            else
            {
		super.stateWinner();
            }
	}
	
	public void act()
	{
            if(tutorial == 1)
            {
                if(getStateWinner()==false)
                {
                    stateWinner();
                    checkForCheaters();
                    
                    incNumAct();
                }
            }
            else if(tutorial == 2)
            {
                if(getStateWinner()==false)
                {
                    stateWinner();
                    checkForCheaters();
                    if(getNumAct() == 0)
                    {
                        for(int i=0;i < numFlowersDroped;i++)
                        {
                                int n = (int)(Math.random()*3);
                                PowerUp pU = new PowerUp(Color.RED);//Makes a random PowerUp
                                int r = (int)(Math.random()*(getGrid().getNumRows()-2*0))+0;
                                int c = (int)(Math.random()*(getGrid().getNumCols()-2*0))+0;
                                Location loc = new Location(r,c);
                                //System.out.println("the Random Row is " + r + " And the random Col is " + c);
                                if(getGrid().isValid(loc)&&getGrid().get(loc)==null)
                                {
                                    rememberPULoc = loc;
                                    System.out.println("place " + pU + " in " + loc);
                                    pU.putSelfInGrid(getGrid(),new Location(r,c));
                                }
                                else
                                {   rememberPULoc = new Location(0,getGrid().getNumCols()/2);
                                    System.out.println("place " + pU + " in " + loc);
                                    pU.putSelfInGrid(getGrid(),rememberPULoc);
                                    System.out.println("Someone was is in " + loc + ".  so placed in " + rememberPULoc);
                                }
                        }
                    }
                    incNumAct();
                }
                
            }
            else if(tutorial == 3)
            {
                if(getStateWinner()==false)
                {
                    stateWinner();
                    checkForCheaters();
                    if(getNumAct() == 0)
                    {
                        if(getGrid().get(new Location(0,0)) instanceof TutorialBug3)
                        {
                            PowerUp pU = new PowerUp(Color.BLUE);
                            rememberPULoc = new Location(0,3);
                            pU.putSelfInGrid(getGrid(),rememberPULoc);
                            
                            b = new BattleBug2012(0,0,5,"The Enemy", Color.RED);
                            b.putSelfInGrid(getGrid(), new Location(3,0));
                        }
                        else if(getGrid().get(new Location(0,6)) instanceof TutorialBug3)
                        {
                            PowerUp pU = new PowerUp(Color.BLUE);
                            rememberPULoc = new Location(0,3);
                            pU.putSelfInGrid(getGrid(),rememberPULoc);
                            
                            b = new BattleBug2012(0,0,5,"The Enemy", Color.RED);
                            b.putSelfInGrid(getGrid(), new Location(3,6));
                        }
                        else if(getGrid().get(new Location(6,0)) instanceof TutorialBug3)
                        {
                            PowerUp pU = new PowerUp(Color.BLUE);
                            rememberPULoc = new Location(3,0);
                            pU.putSelfInGrid(getGrid(),rememberPULoc);
                            
                            b = new BattleBug2012(0,0,5,"The Enemy", Color.RED);
                            b.putSelfInGrid(getGrid(), new Location(6,3));
                        }
                        else 
                        {
                            PowerUp pU = new PowerUp(Color.BLUE);
                            rememberPULoc = new Location(3,6);
                            pU.putSelfInGrid(getGrid(),rememberPULoc);
                            
                            b = new BattleBug2012(0,0,5,"The Enemy", Color.RED);
                            b.putSelfInGrid(getGrid(), new Location(6,3));
                        }
                    }
                    b.act();
                    incNumAct();
                }
            }
            else
                super.act();
        }
            
            
           
}