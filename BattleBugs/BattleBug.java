package BattleBugs;

import java.awt.Color;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Actor;

import java.util.ArrayList;

public class BattleBug extends Bug {

    private int numAct;
    private int strength;
    private int defense;
    private int speed;
    private int killCount;
    private boolean isDead;
    private String name;

    public BattleBug(int s, int d, int sp, String n, Color co) {
        name = n;
        strength = s;
        defense = d;
        speed = sp;
        killCount = 0;
        isDead = false;
        setColor(co);
    }

    private void killUpgrade(String t) {
        if (t.equals("Strength")) {
            strength++;
            System.out.println(name + " gained a strength increase from a kill");
        } else if (t.equals("Defense")) {
            defense++;
            System.out.println(name + " gained a defense increase from a kill");
        } else {
            speed++;
            System.out.println(name + " gained a speed increase from a kill");
        }

    }
    //If your Speed is >=10 then you can get actors 3 units away, otherwise you can
    //only get actors 2 units away.

    public ArrayList<Actor> getActors() {
        ArrayList<Actor> ret = new ArrayList<Actor>();

        ArrayList<Location> occupied = new ArrayList<Location>();
	if(speed >= 20)
	{
		for (Location loc1 : this.getGrid().getOccupiedLocations())
		{
			if ((Math.abs(getLocation().getRow() - loc1.getRow()) <= 4)&& (Math.abs(getLocation().getCol() - loc1.getCol()) <= 4)&& !loc1.equals(getLocation()))
			{
				occupied.add(loc1);
			}
		}
		for(int i = 0;i < occupied.size();i++)
		{
			ret.add(getGrid().get(occupied.get(i)));
		}
	}
	else
	{
		for (Location loc1 : this.getGrid().getOccupiedLocations())
		{
			if ((Math.abs(getLocation().getRow() - loc1.getRow()) <= 3)&& (Math.abs(getLocation().getCol() - loc1.getCol()) <= 3)&& !loc1.equals(getLocation()))
			{
				occupied.add(loc1);
			}
		}

		for(int i = 0;i < occupied.size();i++)
		{
			ret.add(getGrid().get(occupied.get(i)));
		}
	}
        /*for (Location loc1 : this.getGrid().getOccupiedLocations()) {
            if ((Math.abs(getLocation().getRow() - loc1.getRow()) <= 3) && (Math.abs(getLocation().getCol() - loc1.getCol()) <= 3) && !loc1.equals(getLocation())) {
                occupied.add(loc1);
            }
        }

        for (int i = 0; i < occupied.size(); i++) {
            ret.add(getGrid().get(occupied.get(i)));
        }*/
        return ret;
    }

    public ArrayList<Location> getPowerUpLocs() {
        ArrayList<Location> all = this.getGrid().getOccupiedLocations();
        ArrayList<Location> ret = new ArrayList<Location>();
        for (Location a : all) {
            if ((this.getGrid().get(a) instanceof PowerUp)) {
                ret.add(a);
            }
        }
        //System.out.println(ret);
        return ret;
    }

    public ArrayList<PowerUp> getPowerUps() {
        ArrayList<Location> all = this.getGrid().getOccupiedLocations();
        ArrayList<PowerUp> ret = new ArrayList<PowerUp>();
        for (Location a : all) {
            if ((this.getGrid().get(a) instanceof PowerUp)) {
                ret.add(((PowerUp) getGrid().get(a)));
            }
        }
        //System.out.println(ret);
        //System.out.println(ret.size());
        return ret;
    }

    public int level() {
        return strength + defense + speed;
    }

    public void attack() {
        ArrayList<Location> locs = this.getGrid().getOccupiedAdjacentLocations(this.getLocation());
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
        }
    }
    public void destroy(BattleBug b)
    {
         String kU = getGreatestStat(b);
         killUpgrade(kU);
         b.removeSelfFromGrid();
         killCount++;
    }

    public void removeSelfFromGrid() {
        Grid gr = getGrid();
        Location currentLoc = getLocation();
        Color myC = getColor();
        isDead = true;
        super.removeSelfFromGrid();
        TombStone r = new TombStone(myC, getName(), getNumAct(), level());
        r.putSelfInGrid(gr, currentLoc);

    }

    public boolean canMove() {
        Grid<Actor> gr = getGrid();
        if (gr == null) {
            return false;
        }
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(getDirection());
        if (!gr.isValid(next)) {
            return false;
        }
        Actor neighbor = gr.get(next);
        return (neighbor == null) || (neighbor instanceof PowerUp) || (neighbor instanceof Projectile) ;
        // ok to move into empty location or onto PowerUp
        // not ok to move onto any other actor
    }

    public void move() {
        if (canMove()) {
            Grid<Actor> gr = getGrid();
            if (gr == null) {
                return;
            }
            Location loc = getLocation();
            Location next = loc.getAdjacentLocation(getDirection());
            if (gr.isValid(next)) {
                if (gr.get(next) instanceof PowerUp) {
                    PowerUp p = ((PowerUp) gr.get(next));
                    if (p.getColor().equals(Color.RED)) {
                        strength++;
                    } else if (p.getColor().equals(Color.GREEN)) {
                        defense++;
                    } else if (p.getColor().equals(Color.BLUE)) {
                        speed++;
                    }
                    p.removeSelfFromGrid();
                }
                moveTo(next);
            }
        }
        numAct++;
    }

    public void move2() {
        if (speed >= 10) {
            move();
            move();
            numAct--;
        } else {
            move();
        }
    }

    public void turn() {
        numAct++;
    }

    public void turn(int dir) {
        setDirection(getDirection() + dir);
        numAct++;
    }

    public void turnTo(int dir) {
        numAct++;
        setDirection(dir);
    }

    public int getStrength() {
        return strength;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDefense() {
        return defense;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getNumAct() {
        return numAct;
    }

    public boolean isDead() {
        return isDead;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        if (isDead) {
            return "BattleBug: " + name + " Level: " + level() + " is DEAD " + " \nStrength: " + strength + " \nDefense: " + defense + " \nSpeed: " + speed + " \nKills: " + getKillCount();
        }
        return "BattleBug: " + name + " Level: " + level() + " in location  " + getLocation() + " \nStrength: " + strength + " \nDefense: " + defense + " \nSpeed: " + speed + " \nKills: " + getKillCount();
    }

    public boolean equalDirections(int d1, int d2) {
        while (d1 >= 360 || d1 < 0) {
            if (d1 >= 360) {
                d1 -= 360;
            } else {
                d1 += 360;
            }
        }
        while (d2 >= 360 || d2 < 0) {
            if (d2 >= 360) {
                d2 -= 360;
            } else {
                d2 += 360;
            }
        }
        return d1 == d2;
    }

    public int measurementOfAngleBetween(int d1, int d2) {
        int ret;
        while (d1 >= 360 || d1 < 0) {
            if (d1 >= 360) {
                d1 -= 360;
            } else {
                d1 += 360;
            }
        }
        while (d2 >= 360 || d2 < 0) {
            if (d2 >= 360) {
                d2 -= 360;
            } else {
                d2 += 360;
            }
        }
        ret = Math.abs(d1 - d2);
        if (ret >= 180) {
            return Math.abs(ret - 360);
        } else {
            return ret;
        }
    }

    public String getGreatestStat(BattleBug b) {
        ArrayList<String> a = new ArrayList<String>();

        int max = b.getStrength();
        if (b.getDefense() > max) {
            max = b.getDefense();
        }
        if (b.getSpeed() > max) {
            max = b.getSpeed();
        }
        //System.out.println("The max stat has a value of " + max);
        if (b.getStrength() == max) {
            a.add(new String("Strength"));
        }
        if (b.getDefense() == max) {
            a.add(new String("Defense"));
        }
        if (b.getSpeed() == max) {
            a.add(new String("Speed"));
        }

        int r = (int) (a.size() * Math.random());

        return a.get(r);
    }

    public String victory() {
        String ret = getName() + " is the best!!   \n" + "   \\ \n" + "    '-.__.-' \n" + "     /oo |--.--,--,-- \n " + "    \\_.-'._i__i__i_.'. \n " + "          \"\"\"\"\"\"\"\"\"";
        return ret;
    }
}