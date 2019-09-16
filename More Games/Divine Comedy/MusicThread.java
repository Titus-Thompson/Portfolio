// Titus Thompson
//
// MusicThread.java
// Plays game's audio.

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicThread extends Thread
{
	DivineComedy dc;
	
	Clip ves1a, ves1b, ves1c,
		 ves2a, ves2b, ves2c,
		 ves3a, ves3b, ves3c;
	AudioInputStream vestibule1a, vestibule1b, vestibule1c,
					 vestibule2a, vestibule2b, vestibule2c,
					 vestibule3a, vestibule3b, vestibule3c;
	
	long starttime, temptime;
	
	int clipNum;			// Indicates which part of the song is playing. May have different ranges for different parts of game.
	
	public MusicThread(DivineComedy divine)
	{
		super("Music Thread");
		dc = divine;
		
		importFiles();
		
		clipNum = 0;
		
		start();
	}
	
	private void importFiles()
    {
    	try
    	{
    		File soundFile = new File("Music/Vestibule/1a.wav");
    		vestibule1a = AudioSystem.getAudioInputStream(soundFile);
    		DataLine.Info info = new DataLine.Info(Clip.class, vestibule1a.getFormat());
    		soundFile = new File("Music/Vestibule/1b.wav");
    		vestibule1b = AudioSystem.getAudioInputStream(soundFile);
    		soundFile = new File("Music/Vestibule/1c.wav");
    		vestibule1c = AudioSystem.getAudioInputStream(soundFile);
    	    
    	    soundFile = new File("Music/Vestibule/2a.wav");
    		vestibule2a = AudioSystem.getAudioInputStream(soundFile);
    		soundFile = new File("Music/Vestibule/2b.wav");
    		vestibule2b = AudioSystem.getAudioInputStream(soundFile);
    		soundFile = new File("Music/Vestibule/2c.wav");
    		vestibule2c = AudioSystem.getAudioInputStream(soundFile);
    	    
    	    soundFile = new File("Music/Vestibule/3a.wav");
    		vestibule3a = AudioSystem.getAudioInputStream(soundFile);
    		soundFile = new File("Music/Vestibule/3b.wav");
    		vestibule3b = AudioSystem.getAudioInputStream(soundFile);
    		soundFile = new File("Music/Vestibule/3c.wav");
    		vestibule3c = AudioSystem.getAudioInputStream(soundFile);
    		
    		ves1a = (Clip) AudioSystem.getLine(info);
    		ves1b = (Clip) AudioSystem.getLine(info);
    		ves1c = (Clip) AudioSystem.getLine(info);
    		ves2a = (Clip) AudioSystem.getLine(info);
    		ves2b = (Clip) AudioSystem.getLine(info);
    		ves2c = (Clip) AudioSystem.getLine(info);
    		ves3a = (Clip) AudioSystem.getLine(info);
    		ves3b = (Clip) AudioSystem.getLine(info);
    		ves3c = (Clip) AudioSystem.getLine(info);
    	}
    	catch (UnsupportedAudioFileException e)
    	{
    		System.out.println(e);
    	}
    	catch (IOException e)
    	{
    		System.out.println(e);
    	}
    	catch (LineUnavailableException e)
    	{
    		System.out.println(e);
    	}
    	
    	try
    	{
	    	ves1a.open(vestibule1a);
	    	ves1b.open(vestibule1b);
	    	ves1c.open(vestibule1c);
	    	ves2a.open(vestibule2a);
	    	ves2b.open(vestibule2b);
	    	ves2c.open(vestibule2c);
	    	ves3a.open(vestibule3a);
	    	ves3b.open(vestibule3b);
	    	ves3c.open(vestibule3c);
	    }
		catch(IOException e)
		{
			System.out.println(e);
		}
		catch(LineUnavailableException e)
		{
			System.out.println(e);
		}
    }
	
	public void run()
	{
		Clip currentClip = null;
		starttime = System.nanoTime();
		while(true)
		{
			currentClip = getCurrentClip();
			currentClip.setFramePosition(0);
			currentClip.start();
			temptime = starttime + currentClip.getMicrosecondLength() * 1000;
			while (System.nanoTime() < temptime)
			{ }
			starttime = temptime;
		}
	}
	
	private Clip getCurrentClip()
	{
		if(dc.stage == DivineComedy.play)
		{
			if (dc.level == 1)
			{
				if (clipNum < 3)
					clipNum++;
				else
					clipNum = 1;
				
				if (dc.sector == 1)
				{
					if (clipNum == 1)
						return ves1a;
					else if (clipNum == 2)
						return ves1b;
					else if (clipNum == 3)
						return ves1c;
				}
				else if (dc.sector == 2)
				{
					if (clipNum == 1)
						return ves2a;
					else if (clipNum == 2)
						return ves2b;
					else if (clipNum == 3)
						return ves2c;
				}
				else if (dc.sector == 3)
				{
					if (clipNum == 1)
						return ves3a;
					else if (clipNum == 2)
						return ves3b;
					else if (clipNum == 3)
						return ves3c;
				}
			}
		}
		
		return null;
	}
	
	public void setClipNum(int number)
	{
		clipNum = number;
	}
	
}