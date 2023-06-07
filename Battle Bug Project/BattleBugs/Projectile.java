/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BattleBugs;
import info.gridworld.actor.Flower;
import java.awt.Color;
/**
 *
 * @author emsuri
 */
public class Projectile extends Flower 
{
    private int lifeTime;
    public Projectile(int dir)
    {
        super(Color.RED);
        setDirection(dir);
        lifeTime = 0;
    }
    public void act()
    {
        lifeTime++;
        if(lifeTime>1)
            removeSelfFromGrid();

    }
}
