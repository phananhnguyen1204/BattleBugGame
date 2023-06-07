package BattleBugs;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.util.ArrayList;

public class DisplayThread extends Thread
{
    private final int WIDTH = 650, HEIGHT = 850;
    private String act;
    private ArrayList<String> bbNames, bbStr, bbDef, bbSpd, bbLoc, bbAmmo, bbKills, bbLevel;
    private Graphics g;
    private boolean keepRefreshing;
    private boolean reStartAdding;
    private boolean gameOver;
    private String winner = "NO ONE WINS";
    
    public DisplayThread()
    {
        keepRefreshing = true;
        act = "0";
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Pong 1.0");
        f.setResizable(false);
        Canvas c = new Canvas();
        c.setSize(WIDTH, HEIGHT);
        f.add(c);
        f.pack();
        f.setVisible(true);
        g = c.getGraphics();
        
        bbNames = new ArrayList<String>();
        bbLevel = new ArrayList<String>();
        bbStr = new ArrayList<String>();
        bbDef = new ArrayList<String>();
        bbSpd = new ArrayList<String>();
        bbLoc = new ArrayList<String>();
        bbAmmo = new ArrayList<String>();
        bbKills = new ArrayList<String>();
        reStartAdding = true;
        
        gameOver = false;
        
        draw();
    }
    public void setKeepRefershing(boolean b)
    {
        keepRefreshing = b;
    }
    public void setWinner(String w)
    {
        winner = w;
    }
    public void gameOver()
    {
        gameOver = true;
    }
    public void run()
    {
        while(keepRefreshing)
        {
            draw();
            try
            {
                    Thread.sleep(250);
            }
            catch (Exception e)
            {
                    e.printStackTrace();
            }
            draw();
        }
    }

    //=================================================================
    public void setAct(String a)
    {
        act = a;
    }
    public void addBBName(String s)
    {
        if(reStartAdding)
        {
            //reStartAdding = false;
            while(bbNames.size()>0)
                bbNames.remove(0);
            
            bbNames.add(s);
        }
        else
        {
            bbNames.add(s);
        }
    }
    public void addBBLevel(String s)
    {
        if(reStartAdding)
        {
            //reStartAdding = false;
            while(bbLevel.size()>0)
                bbLevel.remove(0);
            
            bbLevel.add(s);
        }
        else
        {
            bbLevel.add(s);
        }
    }
    public void addBBLoc(String s)
    {
        if(reStartAdding)
        {
            //reStartAdding = false;
            while(bbLoc.size()>0)
                bbLoc.remove(0);
            
            bbLoc.add(s);
        }
        else
        {
            bbLoc.add(s);
        }
    }

    public void addBBStr(String s)
    {
        if(reStartAdding)
        {
            //reStartAdding = false;
            while(bbStr.size()>0)
                bbStr.remove(0);
            
            bbStr.add(s);
        }
        else
        {
            bbStr.add(s);
        }
    }
    public void addBBSpd(String s)
    {
        if(reStartAdding)
        {
            //reStartAdding = false;
            while(bbSpd.size()>0)
                bbSpd.remove(0);
            
            bbSpd.add(s);
        }
        else
        {
            bbSpd.add(s);
        }
    }
    public void addBBDef(String s)
    {
        if(reStartAdding)
        {
            //reStartAdding = false;
            while(bbDef.size()>0)
                bbDef.remove(0);
            
            bbDef.add(s);
        }
        else
        {
            bbDef.add(s);
        }
    }
    public void addBBKills(String s)
    {
        if(reStartAdding)
        {
            //reStartAdding = false;
            while(bbKills.size()>0)
                bbKills.remove(0);
            
            bbKills.add(s);
        }
        else
        {
            bbKills.add(s);
        }
    }
    public void addBBAmmo(String s)
    {
        if(reStartAdding)
        {
            reStartAdding = false;
            while(bbAmmo.size()>0)
                bbAmmo.remove(0);
            
            bbAmmo.add(s);
        }
        else
        {
            bbAmmo.add(s);
        }
    }

    public void setRestartAddingTrue()
    {
        reStartAdding = true;
    }
    
    //=================================================================    
    public void draw()
    {
        int n = 1;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        Font f=new Font("IMPACT",Font.BOLD,45);
        g.setFont(f);
        g.setColor(Color.BLACK);
        if(!gameOver)
            g.drawString("Stats at the end of act: " + act ,WIDTH/10, HEIGHT/10);
        else
        {
            g.drawString(winner  ,WIDTH/10, HEIGHT/11);
            g.drawString("act: " + act ,WIDTH/10, HEIGHT/11 + 45);// 45 from font size
        }
        
        for(int i = 0; i < bbNames.size(); i++)
        {
            try
            {
                int fontSize = 30;
                f=new Font("IMPACT",Font.BOLD,fontSize + 10);
                g.setFont(f);
                g.setColor(Color.BLACK);
                g.drawString(bbNames.get(i) + " Level: " + bbLevel.get(i) + "  : " + bbLoc.get(i)  ,WIDTH/11, n*HEIGHT/5);

                f=new Font("IMPACT",Font.PLAIN,fontSize);
                g.setFont(f);
                g.setColor(Color.RED);
                g.drawString("Strength: " + bbStr.get(i) ,WIDTH/10, n*HEIGHT/5 + fontSize);

                g.setColor(Color.GREEN);
                g.drawString("Defense: " + bbDef.get(i) ,WIDTH/10, n*HEIGHT/5 + 2*fontSize);

                g.setColor(Color.BLUE);
                g.drawString("Speed: " + bbSpd.get(i) ,WIDTH/10, n*HEIGHT/5 + 3*fontSize);

                g.setColor(Color.BLACK);
                g.drawString("Ammo: " + bbAmmo.get(i)+ "          Kills : " + bbKills.get(i) ,WIDTH/10, n*HEIGHT/5 + 4*fontSize);

                n++;
            }
            catch(Exception e)
            {
                
            }
        }

    }

}