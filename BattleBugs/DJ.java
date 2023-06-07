package BattleBugs;

import java.applet.Applet;
import java.applet.AudioClip;

public class DJ
{
    private AudioClip song; 
    private AudioClip soundEffect ;
    public DJ()
    {
        song = null;
        soundEffect = null;
    }
    public void playSong(String s)
    {
        if(song == null)
        {
            try
            {
                song = Applet.newAudioClip(DJ.class.getResource(s));
                song.play();
            }
            catch(Exception e)
            {
                System.out.println("=================================== Song: " + s + " not found");
            }
        }
        else
        { 
            try
            {
                song.stop();
                song = Applet.newAudioClip(DJ.class.getResource(s));
                song.play();
            }
            catch(Exception e)
            {
                System.out.println("=================================== Song: " + s + " not found");
            }
        }
    }
    public void stopSong()
    {
        if(song == null)
        {

        }
        else
        {
            song.stop();
        }
    }
    public void playSongLoop(String s)
    {
        if(song == null)
        {
            try
            {
                song = Applet.newAudioClip(DJ.class.getResource(s));
                song.loop();
            }
            catch(Exception e)
            {
                System.out.println("=================================== Song: " + s + " not found");
            }
        }
        else
        {
            try
            {
                song.stop();
                song = Applet.newAudioClip(DJ.class.getResource(s));
                song.loop();
            }
            catch(Exception e)
            {
                System.out.println("=================================== Song: " + s + " not found");
            }
        }
    }
    public void playEffect(String e)
    {
        if(soundEffect == null)
        {
            try
            {
                soundEffect = Applet.newAudioClip(DJ.class.getResource(e));
                soundEffect.play();
            }
            catch(Exception ex)
            {
                System.out.println("=================================== soundEffect: " + e + " not found");
            }
        }
        else
        {
            try
            {
                //soundEffect.stop();  shouldnt stop the previous soundEffect
                soundEffect = Applet.newAudioClip(DJ.class.getResource(e));
                soundEffect.play();
            }
            catch(Exception ex)
            {
                System.out.println("=================================== soundEffect: " + e + " not found");
            }
        }
    }
}