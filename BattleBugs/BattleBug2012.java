/*
 * Updated 5/11/2015
 */
package BattleBugs;
import java.awt.Color;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import java.applet.Applet;
import java.applet.AudioClip;
import java.util.ArrayList;
/**
 *
 * @author emsuri
 */
public class BattleBug2012 extends BattleBug
{
    private int ammo;
    private int killCount;
    private String victoryMusic = null;
    private String killSong = null;

    public BattleBug2012(int s, int d, int sp, String n, Color co)
    {
        super(s,d,sp,n,co);
        ammo = 2*s;
        killCount=0;
    }
    public BattleBug2012(int s, int d, int sp, String n, Color co, String kS, String vM)
    {
        super(s,d,sp,n,co);
        ammo = 2*s;
        killCount=0;
        if(kS != null)
            killSong = kS;
        if(vM != null)
            victoryMusic =vM;
    }


    public String getKillSong()
    {
        return killSong;
    }

    public String getVictoryMusic()
    {
        return victoryMusic;
    }
    public void attack()
    {
        if(ammo > 0)
        {
            ammo--;
            int dir = getDirection();
            ArrayList<Location> locs = new ArrayList<Location>();//this.getGrid().getOccupiedAdjacentLocations(this.getLocation());
            Location front1 = getLocation().getAdjacentLocation(dir);
            Location front2,front3;
            locs.add(front1);
            if(getStrength()>=10)
            {
                front2 = front1.getAdjacentLocation(dir);
                locs.add(front2);
                if(getStrength()>=20)
                {
                    front3 = front2.getAdjacentLocation(dir);
                    locs.add(front3);
                }
            }

            for (Location a : locs) {
                if (getGrid().isValid(a) && this.getGrid().get(a) != null && this.getGrid().get(a) instanceof BattleBug &&((BattleBug) (this.getGrid().get(a))).getDefense() <= getStrength() - 3)
                {
                    destroy(((BattleBug) (this.getGrid().get(a))));
                    if(killSong!=null)
                    {
                        Regulator.sound.playSong(killSong);
                    }

                }
                else if(getGrid().isValid(a) &&!(this.getGrid().get(a) instanceof BattleBug)&& !(this.getGrid().get(a) instanceof Rock)&& !(this.getGrid().get(a) instanceof Regulator))
                {
                    Projectile p  = new Projectile(getDirection());
                    p.putSelfInGrid(getGrid(), a);
                }
            }
        }
        else
            System.out.println(getName() + " is trying to attack but has no ammo");

        /*ArrayList<Location> locs = this.getGrid().getOccupiedAdjacentLocations(this.getLocation());
        ArrayList<Location> locs2 = new ArrayList<Location>();

        for (Location a : locs) {

            int dirTwds = getLocation().getDirectionToward(a);
            int dir = getDirection();
            if ((equalDirections(dirTwds, dir) || equalDirections(dirTwds, dir - 45) || equalDirections(dirTwds, dir + 45))
                    && this.getGrid().get(a) instanceof BattleBug) {
                locs2.add(a);
            }

        }

        for (Location a : locs2) {
            if (((BattleBug) (this.getGrid().get(a))).getDefense() <= strength - 3) {
                String kU = getGreatestStat(((BattleBug) getGrid().get(a)));
                killUpgrade(kU);
                this.getGrid().get(a).removeSelfFromGrid();
                killCount++;
            }
        }*/
    }
    public boolean canMove2Away()
    {
        Grid<Actor> gr = getGrid();
        if (gr == null) {
            return false;
        }
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(getDirection()).getAdjacentLocation(getDirection());
        if (!gr.isValid(next)) {
            return false;
        }
        if(canMove())
        {
            Actor neighbor = gr.get(next);
            return (neighbor == null) || (neighbor instanceof PowerUp) || (neighbor instanceof Projectile) ;
        }
        else
            return false;

        // ok to move into empty location or onto PowerUp
        // not ok to move onto any other actor
    }
    public boolean rock2Away()
    {
        Grid<Actor> gr = getGrid();
        if (gr == null) {
            return false;
        }
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(getDirection()).getAdjacentLocation(getDirection());
        if (!gr.isValid(next)) {
            return false;
        }
        Actor neighbor = gr.get(next);
        if(neighbor instanceof Rock)
            return false;
        return true ;
        // ok to move into empty location or onto PowerUp
        // not ok to move onto any other actor
    }
    public void move()
    {
        int currStr = getStrength();
        super.move();
        ammo = ammo +(getStrength()-currStr);
    }
    public String toString()
    {
        String str = super.toString() + " \nammo: " + getAmmo();
        return str;
    }
    public int getAmmo()
    {
        return ammo;
    }
    
    //added for 2014 - 2015 matches
    public ArrayList<Location> getValidAdjacentLocations()
    {
        return getGrid().getValidAdjacentLocations(getLocation());
    }
    public ArrayList<Location> getEmptyAdjacentLocations()
    {
        return getGrid().getEmptyAdjacentLocations(getLocation());
    }
    public ArrayList<Location> getOccupiedAdjacentLocations()
    {
        return getGrid().getOccupiedAdjacentLocations(getLocation());
    }
    public ArrayList<Actor> getNeighbors()
    {
        return getGrid().getNeighbors(getLocation());
    }
    
    public int getDirectionToward(Location target)
    {
        return getLocation().getDirectionToward(target);
    }
    public void act()
    {
        turn(90);
    }

}
