// Titus Thompson
//
// Wasp.java
// This class controls the wasps' movements.

import java.awt.Point;
import java.util.Random;

public class Wasp extends Enemy
{
	ImageThread im;
	Random r;

	long starttime, temptime;
	
	boolean leftToRight, derp;
	int count;
	int delay;

    public Wasp(ImageThread imgt, Point p, int s)
    {
    	im = imgt;
    	start = new Point (p.x, p.y);
    	loc = new Point (p.x, p.y);
    	sector = s;
    	
    	off = new Point (0, 0);
    	leftToRight = false;
    	alive = false;
    	derp = true;
    	
    	r = new Random();
    	delay = r.nextInt(300);
    }
    
    public void step()
    {
    	if (!alive && checkSpawn())
		{
    		if (delay > 0)
    			delay--;
    		else
    		{
				alive = true;
				if(im.loc.x > im.l.boundaries[im.dc.sector - 1] && im.loc.x < im.l.boundaries[im.dc.sector - 1] + 37)
		    	{	
		    		loc.x = im.l.boundaries[im.dc.sector - 1] + 74;
		    	}
		    	else if (im.loc.x > im.l.boundaries[im.dc.sector] - 37 && im.loc.x < im.l.boundaries[im.dc.sector])
		    	{
		    		loc.x = im.l.boundaries[im.dc.sector];
		    	}
		    	else
		    	{
		    		loc.x = im.character.loc.x + 38;
		    	}
    		}
			return;
		}
    	
    	if (alive)
    	{
			move();
			if(derp)
				derp = false;
			else
				derp = true;
			if (!checkOnscreen() && !checkSpawn())
				alive = false;
    	}
    }

    public boolean checkSpawn()
    {
    	//System.out.println(start + ", " + im.character.loc);
    	if (sector != im.dc.sector)
    		return false;
    	if(im.loc.x > im.l.boundaries[im.dc.sector - 1] && im.loc.x < im.l.boundaries[im.dc.sector - 1] + 74)
    	{	
    		if (start.x < im.l.boundaries[im.dc.sector - 1] + 74 && start.x > im.l.boundaries[im.dc.sector - 1])
    			return true;
    	}
    	else if (im.loc.x > im.l.boundaries[im.dc.sector] - 37 && im.loc.x < im.l.boundaries[im.dc.sector])
    	{
    		if (start.x >= im.l.boundaries[im.dc.sector] - 74 && start.x < im.l.boundaries[im.dc.sector])
    			return true;
    	}
    	else if (start.x > im.loc.x - 37 && start.x < im.loc.x + 37 && 
    			start.y > im.character.loc.y - 23 && start.y < im.character.loc.y + 23)
    		return true;
    	
    	return false;
    }
    
    private void move()
    {
    	if (leftToRight)
    	{
    		int c = 5;
	    	if ( loc.x + 1 <= im.l.matrix[0].length )
				off.x += c;
			checkOffsets();
    	}
    	else
    	{
	    	int c = -5;
	    	if ( loc.x - 1 >= 0 )
				off.x += c;
			checkOffsets();
    	}
    	
    }
    
    private void checkOffsets()
    {
    	if(off.x < 0)
		{
			off.x += 15;
			loc.x--;
		}
		else if(off.x > 14)
		{
			off.x -= 15;
			loc.x++;
		}
    }
    
    private boolean checkOnscreen()
    {
    	if (leftToRight)
    	{
    		if (!(loc.x < im.character.loc.x + 37))
    		{
	    		loc.x = im.character.loc.x - 38;
	    		return false;
    		}
    	}
    	else
    	{
	    	if (!(loc.x > im.character.loc.x - 37) && im.loc.x + 38 < im.l.boundaries[im.dc.sector] && !(im.loc.x - 38 < im.l.boundaries[im.dc.sector - 1]))
	    	{
	    		loc.x = im.character.loc.x + 38;
	    		return false;
	    	}
	    	else if (loc.x == 0 || loc.x < im.l.boundaries[im.dc.sector - 1])
	    	{
	    		loc.x += 74;
	    		return false;
	    	}
	    	else if(!(im.loc.x + 38 < im.l.boundaries[im.dc.sector]) && loc.x < im.l.boundaries[im.dc.sector] - 75)
	    	{
	    		loc.x = im.l.boundaries[im.dc.sector];
	    		return false;
	    	}
    	}
    	return true;
    }
    
    public int getSprite()
    {
    	if (leftToRight)
    	{
    		if (derp)
    			return 0;
    		else
    			return 1;
    	}
    	else
    	{
    		if (derp)
    			return 2;
    		else
    			return 3;
    	}
    }

}