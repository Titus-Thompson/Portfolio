// Titus Thompson
//
// Character.java
// This class is in charge of player's character movements.

import java.awt.Point;

public class Character
{
	ImageThread im;

	Jump jump;
	Attack attack;
	boolean jumping, falling, attacking, ducking;

	boolean p1;

	Point loc, off;

	CharacterSpriteThread charSprite;

    public Character(ImageThread imgt, boolean player1)
    {
    	im = imgt;

		jump = null;
		attack = null;
    	jumping = false;
    	falling = false;
    	attacking = false;

    	p1 = player1;

    	if (p1)
    	{
    		loc = im.loc;
    		off = im.offset;
    	}
    	else
    	{
    		loc = im.loc2;
    		off = im.offset2;
    	}

    	charSprite = new CharacterSpriteThread(this, 3);

		gravity();
    }

    public void upArrow()
    {
    	if (jump == null)
    	{
    		ducking = false;
    		charSprite.derp = !charSprite.derp;
    		jump = new Jump(this, true);
    	}

		//checkOffsets();
    }

    public void downArrow(boolean down)
    {
    	if (down)
    	{
    		if (!jumping && !falling)
    		{
	    			ducking = true;
	    	}
    	}
    	else
    	{
    		ducking = false;
    	}
    }

    public void leftArrow()
    {
    	charSprite.left();
    	if (!ducking)
    	{
	    	if (off.x - 5 > 0 || im.l.matrix[loc.y][loc.x - 1] != '|' && im.l.matrix[loc.y][loc.x - 1] != '9')
	    	{
		    	int c = -5;
		    	if ( loc.x - 1 >= 0 )
					off.x += c;
				checkOffsets();
				gravity();
				if (loc.x == im.l.boundaries[im.dc.sector -1])
					im.switchSector(false);
	    	}
    	}
    }

    public void rightArrow()
    {
    	charSprite.right();
    	if (!ducking)
    	{
	    	if (off.x + 5 < 15 || im.l.matrix[loc.y][loc.x + 1] != '|' && im.l.matrix[loc.y][loc.x + 1] != '9')
	    	{
	    		int c = 5;
		    	if ( loc.x + 1 <= im.l.matrix[0].length )
					off.x += c;
				checkOffsets();
				gravity();
				if (loc.x == im.l.boundaries[im.dc.sector])
					im.switchSector(true);
	    	}
    	}
    }

    public void attack()
    {
    	attack = new Attack(this);
    }

    public void checkOffsets()
    {
    	if(off.x < 0)
		{
			off.x += 15;
			loc.x--;
			checkGround(true);
		}
		else if(off.x > 14)
		{
			off.x -= 15;
			loc.x++;
			checkGround(false);
		}
		else if(off.y > 34)
		{
			off.y -= 15;
			loc.y++;
		}
		else if(off.y < 0)
		{
			off.y += 15;
			loc.y--;
		}
    }

    private void checkGround(boolean left)				//adjusts character if walking to a sloping block on a different level.
    {
		if (left)			//player moved to the left
		{
			if ( (im.l.matrix[loc.y][loc.x] >= '!' && im.l.matrix[loc.y][loc.x] <= '*') || im.l.matrix[loc.y][loc.x] == '@' || im.l.matrix[loc.y][loc.x] == '^')
			{
				loc.y--;
			}
			else if(im.l.matrix[loc.y][loc.x] > '0' && im.l.matrix[loc.y][loc.x] <= '9')
			{
				//loc.y++;
			}
			else if (im.l.matrix[loc.y + 1][loc.x] == 'n' || im.l.matrix[loc.y + 1][loc.x] == 's')
			{
				if (jump == null)
					jump = new Jump(this, false);
			}
		}
		else				//player moved to the right
		{
			if (im.l.matrix[loc.y][loc.x] > '0' && im.l.matrix[loc.y][loc.x] <= '9')
			{
				loc.y--;
			}
			else if ( (im.l.matrix[loc.y][loc.x] >= '!' && im.l.matrix[loc.y][loc.x] <= '*') || im.l.matrix[loc.y][loc.x] == '@' || im.l.matrix[loc.y][loc.x] == '^')
			{
				//loc.y++;
			}
			else if (im.l.matrix[loc.y + 1][loc.x] == 'n' || im.l.matrix[loc.y + 1][loc.x] == 's')
			{
				if (jump == null)
					jump = new Jump(this, false);
			}
		}
		//System.out.println(loc);
    }

    public void checkFall()
    {
    	if (loc.y + 1 < im.l.matrix.length)
    	{
	    	switch (im.l.matrix[loc.y + 1][loc.x])
			{
				case '0':
					if(off.y > 10)
					{
						falling = false;
						off.y = 10;
					}
					break;
				case '9':
					if(off.y > 10)
					{
						falling = false;
						off.y = 10;
					}
					break;
				case '~':
					if(off.y > 10)
					{
						falling = false;
						off.y = 10;
					}
					break;
				case '1':
					if(off.y > 20 - off.x)
					{
						falling = false;
						off.y = 20 - off.x;
					}
					break;
				case '2':
					if(off.y > 20 - off.x / 2 + 7)
					{
						falling = false;
						off.y = 20 - off.x / 2 + 7;
					}
					break;
				case '3':
					if(off.y > 20 - off.x / 2)
					{
						falling = false;
						off.y = 20 - off.x / 2;
					}
					break;
				case '4':
					if(off.y > 12 - off.x / 4 + 15)
					{
						falling = false;
						off.y = 12 - off.x / 4 + 15;
					}
					break;
				case '5':
					if(off.y > 12 - off.x / 4 + 11)
					{
						falling = false;
						off.y = 12 - off.x / 4 + 11;
					}
					break;
				case '6':
					if(off.y > 12 - off.x / 4 + 7)
					{
						falling = false;
						off.y = 12 - off.x / 4 + 7;
					}
					break;
				case '7':
					if(off.y > 12 - off.x / 4 + 4)
					{
						falling = false;
						off.y = 12 - off.x / 4 + 4;
					}
					break;
				case '!':
					if(off.y > 10 + off.x)
					{
						falling = false;
						off.y = 10 + off.x;
					}
					break;
				case '@':
					if(off.y > 10 + off.x / 2)
					{
						falling = false;
						off.y = 10 + off.x / 2;
					}
					break;
				case '#':
					if(off.y > 10 + off.x / 2 + 7)
					{
						falling = false;
						off.y = 10 + off.x / 2 + 7;
					}
					break;
				case '$':
					if(off.y > 10 + off.x / 4 + 4)
					{
						falling = false;
						off.y = 10 + off.x / 4 + 4;
					}
					break;
				case '%':
					if(off.y > 10 + off.x / 4 + 7)
					{
						falling = false;
						off.y = 10 + off.x / 4 + 7;
					}
					break;
				case '^':
					if(off.y > 10 + off.x / 4 + 11)
					{
						falling = false;
						off.y = 10 + off.x / 4 + 11;
					}
					break;
				case '&':
					if(off.y > 10 + off.x / 4 + 15)
					{
						falling = false;
						off.y = 10 + off.x / 4 + 15;
					}
					break;
			}
    	}
    	else
    	{
    		if (im.dc.level == 1 && im.dc.sector == 2)
    		{
    			loc.y = 34;
    			loc.x = 270;
    		}
    		else if (im.dc.level == 1 && im.dc.sector == 3)
    		{
    			loc.y = 53;
    			loc.x = 659;
    		}
    	}
    }

    private void gravity()
    {
    	if (!jumping && !falling)
    	{
    		while ( (im.l.matrix[loc.y + 1][loc.x] == 's' || im.l.matrix[loc.y + 1][loc.x] == 'n' )&& loc.y + 1 <= im.l.matrix.length)
			{
				loc.y++;
			}

			switch (im.l.matrix[loc.y + 1][loc.x])
			{
				case '0':
					off.y = 10;
					break;
				case '~':
					off.y = 10;
					break;
				case '1':
					off.y = 20 - off.x;
					break;
				case '2':
					off.y = 20 - off.x / 2 + 7;
					break;
				case '3':
					off.y = 20 - off.x / 2;
					break;
				case '4':
					off.y = 12 - off.x / 4 + 15;
					break;
				case '5':
					off.y = 12 - off.x / 4 + 11;
					break;
				case '6':
					off.y = 12 - off.x / 4 + 7;
					break;
				case '7':
					off.y = 12 - off.x / 4 + 4;
					break;
				case '!':
					off.y = 10 + off.x;
					break;
				case '@':
					off.y = 10 + off.x / 2;
					break;
				case '#':
					off.y = 10 + off.x / 2 + 7;
					break;
				case '$':
					off.y = 10 + off.x / 4 + 4;
					break;
				case '%':
					off.y = 10 + off.x / 4 + 7;
					break;
				case '^':
					off.y = 10 + off.x / 4 + 11;
					break;
				case '&':
					off.y = 10 + off.x / 4 + 15;
					break;
			}
    	}
    }

    public void nullify(Jump j)
    {
    	jump = null;
    }

    public void nullify(Attack a)
    {
    	attack = null;
    }

}

class Jump extends Thread
{
	Character c;

	int y, temp, diff;
	double off, off2;

	long starttime, temptime;

	public Jump(Character ch, boolean jump)		//if jump is true, character is about to jump. if false, they're falling.
	{
		super("JumpThread");
		c = ch;

		if (jump)
		{
			c.jumping = true;
			c.falling = false;
		}
		else
		{
			c.falling = true;
			c.jumping = false;
		}

		y = 0;
		temp = 0;
		diff = 0;
		off = 0;
		off2 = 0;

		temptime = 0;
		starttime = 0;

		start();
	}

	public void run()
	{
		double i = 0;
		boolean adjusted = false;
		starttime = System.nanoTime();
		wloop: while (true)
		{

			if (c.jumping && !adjusted)
			{
				off = 0;
				off2 = 0;
				adjusted = true;
			}
			else if (c.falling && ! adjusted)
			{
				off = 4.15;
				off2 = -3 * ((i + off) * (i + off)) + 22 * (i + off);
				adjusted = true;
			}

			if (!c.falling && (i >= 4.1 && i <= 4.2))
			{
				c.jumping = false;
				c.falling = true;
			}

			temp = y;
			y = (int) (-3 * ((i + off) * (i + off)) + 22 * (i + off) - off2);
			diff = y - temp;
			c.off.y -= diff;
			c.checkOffsets();

			i += .05;
			temptime = starttime + 5000000;
			while (System.nanoTime() < temptime)
			{ }
			starttime = temptime;

			if (c.falling)
			{
				c.checkFall();
			}

			if (!c.jumping && !c.falling)
			{
				break wloop;
			}
		}
		c.nullify(this);
	}
}

class CharacterSpriteThread extends Thread
{
	boolean running, left;
	boolean herp, derp;
	Character c;

	int sprite;						//sprite index.
										//0: facing left, 1: running left 1, 2: running left 2
										//3: facing right, 4: running right 1, 5: running right 2

	long starttime, temptime;

	public CharacterSpriteThread (Character ch)
	{
		super("CharacterSpriteThread");

		sprite = 0;
		c = ch;

		left = false;
		running = false;
		herp = true;
		derp = true;

		start();
	}

	public CharacterSpriteThread (Character ch, int init)
	{
		super("CharacterSpriteThread");

		sprite = init;
		c = ch;

		left = false;
		running = false;
		herp = true;

		start();
	}

	public void run()
	{
		starttime = System.nanoTime();
		while(true)
		{
			try{
				Thread.sleep(10);}
			catch (InterruptedException e) { }
			if(running)
			{
				switchRunSprite();
				temptime = starttime + 250000000;
				while (System.nanoTime() < temptime)
				{ }
				starttime = temptime;
			}
		}
	}

	private void switchRunSprite()
	{
		if (!(sprite < 6))		//not already running
		{
			if(left)
				sprite = 0;
			else
				sprite = 3;
		}
		else
		{
			if (left)
			{
				if (sprite == 0)
				{
					if (herp)
					{
						sprite = 1;
						herp = false;
					}
					else
					{
						sprite = 2;
						herp = true;
					}
				}
				else
					sprite = 0;
			}
			else
			{
				if (sprite == 3)
				{
					if (herp)
					{
						sprite = 4;
						herp = false;
					}
					else
					{
						sprite = 5;
						herp = true;
					}
				}
				else
					sprite = 3;
			}
		}
	}

	public int getSprite()
	{
		if (c.jumping)
		{
			if(left)
			{
				if (derp)
					return 2;
				else
					return 1;
			}
			else
			{
				if (derp)
					return 4;
				else
					return 5;
			}
		}
		if (c.falling)
		{
			if(left)
				return 0;
			else
				return 3;
		}
		if (c.ducking)
		{
			if(left)
				return 6;
			else
				return 7;
		}
		
		return sprite;
	}

	public void left()
	{
		left = true;
	}

	public void right()
	{
		left = false;
	}

	public void startRunning()
	{
		running = true;
	}

	public void stopRunning()
	{
		running = false;
		if(left)
			sprite = 0;
		else
			sprite = 3;
	}


}

class Attack extends Thread
{
	Character c;

	long starttime, temptime;

	public Attack(Character ch)		//if jump is true, character is about to jump. if false, they're falling.
	{
		super("AttackThread");
		c = ch;
		c.attacking = true;

		temptime = 0;
		starttime = 0;

		start();
	}

	public void run()
	{
		starttime = System.nanoTime();
		temptime = starttime + 300000000;
		while (System.nanoTime() < temptime)
		{ }
		starttime = temptime;

		c.attacking = false;
		c.nullify(this);
	}
}