package edu.gonzaga;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileNotFoundException;

public class Music
{
    public AudioInputStream audIn;
    public Clip clip;

    public void loopSound(String str) throws Exception
    {

        try
        {
            try
            {
                clip.close();
            }
            catch(Exception e)
            {

            }
            //use a .wav file name for the string
            audIn = AudioSystem.getAudioInputStream(new File(str));
            clip = AudioSystem.getClip();
            clip.open(audIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(FileNotFoundException exception)
        {
            System.out.println(exception);
        }
    }

    public static synchronized void playSound(String sound) {
        new Thread(
        new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Display.class.getResourceAsStream(sound));
                    clip.open(inputStream);
                    clip.start(); 
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
}
