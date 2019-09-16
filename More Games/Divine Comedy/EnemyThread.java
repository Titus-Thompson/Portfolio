// Titus Thompson
//
// EnemyThread.java
// This class is in charge of all the enemies in the overworld.

import java.awt.Point;

public class EnemyThread extends Thread
{
	ImageThread im;
	
	Enemy[] en;
	
	long starttime, temptime;

    public EnemyThread(ImageThread imgt)
    {
    	super("EnemyThread");
    	
    	im = imgt;
    	
    	en = new Enemy[50];
    	
    	start();
    }
    
    public void run()
    {
    	starttime = System.nanoTime();
    	while (true)
    	{
    		step();
			temptime = starttime + 40000000;
			while (System.nanoTime() < temptime)
			{ }
			starttime = temptime;
    	}
    }
    
    public void step()
    {
    	for (Enemy e: en)
    	{
    		if (e != null)
    			e.step();
    	}
    }
    
}