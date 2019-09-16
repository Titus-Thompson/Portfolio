// Titus Thompson
//
// ImageThread.java
// Displays game, computing the output.

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.imageio.*;
import java.io.IOException;
import java.io.InputStream;

import java.awt.event.*;
import java.awt.Point;

public class ImageThread extends Thread
{
	Display d;
	DivineComedy dc;
	Graphics gBuf, gBuf2;
	Image vMem, vMem2;

	LevelReader l;
	Character character, character2;
	EnemyThread enemies;

	//allies
	Image[] dante;
	/* Player 2
	 * Image[] dante2;
	 */
	Image[] virgil;

	//special
	Image[] attacks;

	//enemies
	Image[] waspimg;

	//stage
	Image ga, gb, gc, gd, ge, gf, gg;				//flat surface ground
	Image p12Aa, p12Ab, p12Ba, p12Bb;				//slope: 1/2. A's are the first half (bottom), B's are the second (top).
	Image m12Aa, m12Ab, m12Ba, m12Bb;				//slope: -1/2. A's on top, B's on bottom.
	Image p14A, p14B, p14C, p14D;					//slope: 1/4. ascending with letters
	Image m14A, m14B, m14C, m14D;					//slope: -1/4. descending with letters
	Image p1a, p1b, m1a, m1b;						//slopes 1 and -1, respectively
	Image na, nb, nc, nd, ne;						//underground blocks

	int w, h;
	Point loc, offset;
	Point loc2, offset2;

	boolean imagesLoaded = true;

    public ImageThread(DivineComedy divine)
    {
    	super("ImageThread");

    	dc = divine;
    	l = new LevelReader("Levels/Level1.txt", this);
    	offset = new Point (0, 14);
    	offset2 = new Point (0, 14);

    	character = new Character(this, true);
    	/* Player 2
    	 * character2 = new Character(this, false);
    	 */

		d = new Display(this);
		gBuf = d.gBuffer;
		gBuf2 = d.gBuffer2;
		vMem = d.virtualMem;
		vMem2 = d.virtualMem2;

		loadImages();

		w = d.getSize().height;
		h = d.getSize().width;
		
		enemies = new EnemyThread(this);
		generateEnemies();

		start();
    }

    private void loadImages()
    {
    	try
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			waspimg = new Image[4];
			InputStream input = classLoader.getResourceAsStream("Images/wasp1b.png");
			waspimg[0] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/wasp2b.png");
			waspimg[1] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/wasp1.png");
			waspimg[2] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/wasp2.png");
			waspimg[3] = ImageIO.read(input);

			input = classLoader.getResourceAsStream("Images/ground/0a.png");
			ga = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/0b.png");
			gb = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/0c.png");
			gc = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/0d.png");
			gd = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/0e.png");
			ge = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/0f.png");
			gf = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/0g.png");
			gg = ImageIO.read(input);

			input = classLoader.getResourceAsStream("Images/ground/1%2Aa.png");
			p12Aa = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/1%2Ab.png");
			p12Ab = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/1%2Ba.png");
			p12Ba = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/1%2Bb.png");
			p12Bb = ImageIO.read(input);

			input = classLoader.getResourceAsStream("Images/ground/-1%2Aa.png");
			m12Aa = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1%2Ab.png");
			m12Ab = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1%2Ba.png");
			m12Ba = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1%2Bb.png");
			m12Bb = ImageIO.read(input);

			input = classLoader.getResourceAsStream("Images/ground/1%4A.png");
			p14A = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1%4A.png");
			m14A = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/1%4B.png");
			p14B = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1%4B.png");
			m14B = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/1%4C.png");
			p14C = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1%4C.png");
			m14C = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/1%4D.png");
			p14D = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1%4D.png");
			m14D = ImageIO.read(input);

			input = classLoader.getResourceAsStream("Images/ground/1a.png");
			p1a = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/1b.png");
			p1b = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1a.png");
			m1a = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/-1b.png");
			m1b = ImageIO.read(input);

			input = classLoader.getResourceAsStream("Images/ground/Na.png");
			na = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/Nb.png");
			nb = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/Nc.png");
			nc = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/Nd.png");
			nd = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/ground/Ne.png");
			ne = ImageIO.read(input);

			dante = new Image[8];
			input = classLoader.getResourceAsStream("Images/Characters/danteL2.png");
			dante[0] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/danteL1.png");
			dante[1] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/danteL3.png");
			dante[2] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/danteR2.png");
			dante[3] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/danteR1.png");
			dante[4] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/danteR3.png");
			dante[5] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/danteL4.png");
			dante[6] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/danteR4.png");
			dante[7] = ImageIO.read(input);
			
			
			/* Player 2
			dante2 = new Image[6];
			input = classLoader.getResourceAsStream("Images/Characters/dante2L2.png");
			dante2[0] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/dante2L1.png");
			dante2[1] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/dante2L3.png");
			dante2[2] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/dante2R2.png");
			dante2[3] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/dante2R1.png");
			dante2[4] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/dante2R3.png");
			dante2[5] = ImageIO.read(input);
			*/

			attacks = new Image[2];
			input = classLoader.getResourceAsStream("Images/Characters/attack1.png");
			attacks[0] = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Images/Characters/attack2.png");
			attacks[1] = ImageIO.read(input);
		}
		catch (IOException ioe) {
			imagesLoaded = false;
		}
    }

    public void run()
    {
    	while(!dc.equals(null))
    	{
			if (dc.stage == DivineComedy.play)
			{
				int m = 0;
				int n = 0;

				boolean iadjusted = false, jadjusted = false;

				gBuf2.setColor(Color.gray);
				gBuf2.fillRect(0, 0, 1100, 700);

				fLoopi: for(int i = loc.y - 23; i < l.matrix.length; i++)
				{
					if (!iadjusted)
					{
						if (i < 0)
							i = 0;
						else if (loc.y + 23 >= l.matrix.length)
						{
							i = l.matrix.length - 47;
						}
						n = i;
						iadjusted = true;
					}

					fLoopj: for(int j = loc.x - 37; j < l.matrix[0].length; j++)
					{
						if (!jadjusted)
						{
							if (j < l.boundaries[dc.sector - 1])
							{
								j = l.boundaries[dc.sector - 1];
							}
							else if (loc.x + 38 >= l.boundaries[dc.sector])
							{
								//System.out.println(l.matrix[0].length);
								j = l.boundaries[dc.sector] - 75;
							}
							m = j;
							jadjusted = true;
						}
						//System.out.println(i + " " + j);
						if (j - m < 75)
						{
							drawBlock(i, j, j - m, i - n);
						}
						else
						{
							if (i - n < 47)
								break fLoopj;
							else
							{
								break fLoopi;
							}
						}

					}
					jadjusted = false;
				}
				drawEnemies();
				drawCharacters();
			}
			gBuf.drawImage(vMem2, 0, 0, d);
    	}
    }

    private void drawBlock (int i, int j, int m, int n)
    {
		Image block = getBlock(l.matrix[i][j], i, j);

		int e = 1, f = 1;
		if (!(loc.x - 37 >= l.boundaries[dc.sector - 1] && loc.x + 38 < l.boundaries[dc.sector]))
			e = 0;
		if (!(loc.y - 23 > 0 && loc.y + 23 < l.matrix.length))
			f = 0;

		//int yAir = 0;
		//if (character.jump != null)
		//	yAir = character.jump.y;
		//System.out.println(m + " " + n);

		if (l.matrix3[i][j] == 'n')
		{
			gBuf2.drawImage(nc, m * 15 - (offset.x + 5) * e, n * 15 - offset.y * f + 30, null);
		}

		if (block != null)
			gBuf2.drawImage(block, m * 15 - (offset.x + 5) * e, n * 15 - offset.y * f + 30, null);
    }

    private Image getBlock(char c, int i, int j)
    {
		if (l.blockRndMatrix[i][j] == 0)
		{
			if (c == '0' || c == '9')
				return ga;
			else if (c == '1')
				return p1a;
			else if (c == '2')
				return p12Aa;
			else if (c == '3')
				return p12Ba;
			else if (c == '4')
				return p14A;
			else if (c == '5')
				return p14B;
			else if (c == '6')
				return p14C;
			else if (c == '7')
				return p14D;
			else if (c == '!')
				return m1a;
			else if (c == '@')
				return m12Aa;
			else if (c == '#')
				return m12Ba;
			else if (c == '$')
				return m14A;
			else if (c == '%')
				return m14B;
			else if (c == '^')
				return m14C;
			else if (c == '&')
				return m14D;
			else if (c == 'n' || c == '|' || c == '-' || c == '~')
				return na;
			//else
			//	return na;
		}
		else if (l.blockRndMatrix[i][j] == 1)
		{
			if (c == '0' || c == '9')
				return gb;
			else if (c == '1')
				return p1b;
			else if (c == '2')
				return p12Ab;
			else if (c == '3')
				return p12Bb;
			else if (c == '!')
				return m1b;
			else if (c == '@')
				return m12Ab;
			else if (c == '#')
				return m12Bb;
			else if (c == 'n' || c == '|' || c == '-' || c == '~')
				return nb;
		}
		else if (l.blockRndMatrix[i][j] == 2)
		{
			if (c == '0' || c == '9')
				return gc;
			else if (c == 'n' || c == '|' || c == '-' || c == '~')
				return nc;
		}
		else if (l.blockRndMatrix[i][j] == 3)
		{
			if (c == '0' || c == '9')
				return gd;
			else if (c == 'n' || c == '|' || c == '-' || c == '~')
				return nd;
		}
		else if (l.blockRndMatrix[i][j] == 4)
		{
			if (c == '0' || c == '9')
				return ge;
			else if (c == 'n' || c == '|' || c == '-' || c == '~')
				return ne;
		}
		else if (l.blockRndMatrix[i][j] == 5)
		{
			if (c == '0' || c == '9')
				return gf;
		}
		else if (l.blockRndMatrix[i][j] == 6)
		{
			if (c == '0' || c == '9')
				return gg;
		}

		return null;
    }

    private void drawEnemies()
    {
    	int xVal = findX(), yVal = findY();
    	int x, y;
    	for (int i = 0; i < enemies.en.length; i++)
    	{
    		if (enemies.en[i] != null)
    		{
    			if (enemies.en[i].alive)
    			{
	    			x = xVal - offset.x + 15 * (enemies.en[i].loc.x - loc.x) + enemies.en[i].off.x;
	    			y = yVal - offset.y + 15 * (enemies.en[i].loc.y - loc.y) + 30;
	    			
	    			gBuf2.drawImage(waspimg[enemies.en[i].getSprite()], x - 15, y - 6, null);
    			}
    		}
    	}
    }
    
    private void drawCharacters()
    {
    	//gBuf2.setColor(Color.black);
    	int xVal = findX(), yVal = findY();
    	/* Player 2
    	 * 
		 * int xVal2 = 0, yVal2 = 0;
		 * 
		 * xVal2 = xVal - offset.x + 15 * (loc2.x - loc.x) + offset2.x;
		 * yVal2 = yVal - offset.y + 15 * (loc2.y - loc.y) + offset2.y;
		 * 
		 */

		gBuf2.drawImage(dante[character.charSprite.getSprite()], xVal - 15, yVal - 27, null);
		/* Player 2
		 * gBuf2.drawImage(dante2[character2.charSprite.getSprite()], xVal2 - 15, yVal2 - 27, null);
		 */

		if (character.attacking)
    	{
    		if (character.charSprite.getSprite() < 3)
    			gBuf2.drawImage(attacks[0], xVal - 15 - 30, yVal - 27, null);
    		else
    			gBuf2.drawImage(attacks[0], xVal - 15 + 30, yVal - 27, null);
    	}
		/* Player 2
    	if (character2.attacking)
    	{
    		if (character2.charSprite.getSprite() < 3)
    			gBuf2.drawImage(attacks[1], xVal2 - 15 - 30, yVal2 - 27, null);
    		else
    			gBuf2.drawImage(attacks[1], xVal2 - 15 + 30, yVal2 - 27, null);

    	}
    	*/
    }
    
    private int findX()
    {
    	if (loc.x - 36 > l.boundaries[dc.sector - 1] && loc.x + 38 < l.boundaries[dc.sector])
		{
			return 550;
		}
		else
		{
			if ( !(loc.x - 36 > l.boundaries[dc.sector -1]) )
			{
				return (loc.x - l.boundaries[dc.sector - 1]) * 15 + offset.x;
			}
			else if ( !(loc.x + 38 < l.boundaries[dc.sector]) )
			{
				return 1100 - (l.boundaries[dc.sector] - loc.x - 2) * 15 + offset.x;
			}
		}
    	return -1;
    }
    
    private int findY()
    {
    	if (loc.y - 23 > 0 && loc.y + 23 < l.matrix.length)
		{
			return 350;
		}
		else
		{
			if ( !(loc.y - 23 > 0) )
			{
				return loc.y * 15 + offset.y;
			}
			else if ( !(loc.y + 23 < l.matrix.length) )
			{
				return 700 - (l.matrix.length - loc.y) * 15 + offset.y + 10;
			}
		}
    	return -1;
    }

    public void switchSector(boolean foreward)
    {
    	if (foreward)
    	{
    		if (l.boundaries.length == dc.sector)
    		{
    			levelComplete();
    		}
    		else
    		{
    			dc.sector++;
        		loc.x++;
        		loc2.x = loc.x + 2;
        		loc2.y = loc.y;
    		}
    	}
    	else
    	{
    		if (dc.sector == 1)
    		{

    		}
    		else
    		{
    			dc.sector--;
        		loc.x--;
        		loc2.x = loc.x - 2;
        		loc2.y = loc.y;
    		}
    	}
    	generateEnemies();
    }
    
    private void generateEnemies()
    {
    	for(int i = 0; i < enemies.en.length; i++)
    		enemies.en[i] = null;
    	int next = 0, sect = 0;
    	for (int i = 0; i < l.enemies.length; i++)
    	{
    		if (l.enemies[i] == 'b' || l.enemies[i] == 'a')
    		{
    			if (l.enemiesloc[i].x < l.boundaries[1])
    				sect = 1;
    			else if (l.enemiesloc[i].x < l.boundaries[2])
    				sect = 2;
    			else
    				sect = 3;
    			
    			if (l.enemies[i] == 'a')
    				enemies.en[next] = null;
    			else if (l.enemies[i] == 'b')
    				enemies.en[next] = new Wasp(this, l.enemiesloc[i], sect);
    			next++;
    		}
    	}
    }

    public void levelComplete()
    {
    	System.out.println("Level Complete!");
    }

}

class Display extends JFrame implements Runnable, KeyListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	Graphics g, gBuffer, gBuffer2;
	Image virtualMem, virtualMem2;

	ImageThread im;
	private Thread thread;

	Walker walk;
	/* Player 2
	 * Walker walk2;
	 */
	

	public Display(ImageThread i)
	{
		im = i;

		setUpWindow();

		addKeyListener(this);
		//addMouseListener(this);
		//addMouseMotionListener(this);

		walk = new Walker("WalkThread", false, false, im.character);
		/* Player 2
		walk2 = new Walker("WalkThread", false, false, im.character2);
		*/

		start();
	}

	private void setUpWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 700);
		setResizable(false);

		// Move to center
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); // Get the size of the screen
		int w = getSize().width;
		int x = (dim.width - w) / 2; // Determine the new location of the window
		setLocation(x, 0); // Move the window

		setVisible(true);
		setFocusable(true);

		// Set up gBuffers
		virtualMem = createImage(getSize().width, getSize().height);
		virtualMem2 = createImage(getSize().width, getSize().height);
		gBuffer = virtualMem.getGraphics();
		gBuffer2 = virtualMem2.getGraphics();
	}

	public void start()
   	{
   		if (thread == null)
   		{
        	thread = new Thread(this);
       		thread.start();
   		}
    }


    public void stop()
  	{
    	if (thread != null)
    	{
      		thread = null;
    	}
  	}

  	public void run()
  	{

  	}

	public void paint(Graphics g)
	{
		this.g = g; // allows all methods to access g without passing it as a parameter

		g.drawImage(virtualMem, 0, 0, this);

		repaint();
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public void keyPressed(KeyEvent ke)
	{
		int keyCode = ke.getKeyCode();

		//System.out.println(keyCode);

		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN ||
			keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S)
		{
			switch( keyCode ) {
				case KeyEvent.VK_UP:
					im.character.upArrow();
					break;
				case KeyEvent.VK_DOWN:
					im.character.downArrow(true);
					break;
				case KeyEvent.VK_LEFT:
					walk.press(true);
					break;
				case KeyEvent.VK_RIGHT:
					walk.press(false);
					break;
				case KeyEvent.VK_W:
					im.character.upArrow();
					break;
				case KeyEvent.VK_S:
					im.character.downArrow(true);
					break;
				case KeyEvent.VK_A:
					walk.press(true);
					break;
				case KeyEvent.VK_D:
					walk.press(false);
					break;
			}
		}
		else if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_ENTER)
		{
			switch (keyCode)
			{
				case KeyEvent.VK_SPACE:
					im.character.attack();
					break;
				case KeyEvent.VK_ENTER:
					im.character.attack();
					break;
			}
		}
	}

	public void keyReleased(KeyEvent ke)
	{
		int keyCode = ke.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_S)
		{
			switch( keyCode ) {
				case KeyEvent.VK_LEFT:
					walk.release(true);
					break;
				case KeyEvent.VK_RIGHT:
					walk.release(false);
					break;
				case KeyEvent.VK_DOWN:
					im.character.downArrow(false);
					break;
				case KeyEvent.VK_A:
					walk.release(true);
					break;
				case KeyEvent.VK_D:
					walk.release(false);
					break;
				case KeyEvent.VK_S:
					im.character.downArrow(false);
					break;
			}
		}
	}

	public void keyTyped(KeyEvent ke)
	{

	}

}

class Walker extends Thread
{
	Character c;

	boolean left,			//left arrow is being pressed
		right;				//right arrow is being pressed

	public Walker(String name, boolean l, boolean r, Character character)
	{
		super(name);

		c = character;

		left = l;
		right = r;

		start();
	}

	public void press(boolean leftRight)		//left is true, right is false
	{
		if (leftRight)
			left = true;
		else
			right = true;
	}

	public void release(boolean leftRight)		//left is true, right is false
	{
		if (leftRight)
			left = false;
		else
			right = false;
	}

	public void run()
	{
		long starttime = System.nanoTime();
		long temptime = 0;
		while (true)
		{
			//System.out.println(c + " " + left + " " + right);
			if (left && right || !left && !right)
			{
				c.charSprite.stopRunning();
			}
			else if (left)
			{
				c.charSprite.startRunning();
				c.leftArrow();
			}
			else if (right)
			{
				c.charSprite.startRunning();
				c.rightArrow();
			}

			temptime = starttime + 60000000;
			while (System.nanoTime() < temptime)
			{ }
			starttime = temptime;
		}

	}
}