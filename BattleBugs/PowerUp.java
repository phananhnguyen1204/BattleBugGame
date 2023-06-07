package BattleBugs;
import java.awt.Color;
import info.gridworld.actor.Flower;

public class PowerUp extends Flower
{
	Color myColor;
	public PowerUp(Color initialColor)
    {
    	myColor = initialColor;
        setColor(initialColor);
    }
	public void act()
	{
		setDirection(getDirection()+45);
	}
	public String toString()
	{
		String str;
		if(myColor.equals(Color.RED))
			str = "Strength Power Up";
		else if(myColor.equals(Color.GREEN))
			str = "Defense Power Up";
		else
			str = "Speed Power Up";
		return str;
	}
}