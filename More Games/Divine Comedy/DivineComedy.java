// Titus Thompson
//
// DivineComedy.java
// This class runs the game.


public class DivineComedy
{
	public static final int title = -1;
	public static final int menu = 0;
	public static final int play = 1;
	
	ImageThread im;
	MusicThread mus;
	int stage, level, sector;

	public static void main(String[] args)
	{
		DivineComedy i = new DivineComedy();
		i.end();
	}

    public DivineComedy()
    {
    	stage = play;
    	level = 1;
    	sector = 1;

		im = new ImageThread(this);
		mus = new MusicThread(this);
    }

    public void end()
    {

    }

}