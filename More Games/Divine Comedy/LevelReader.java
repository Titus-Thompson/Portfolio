// Titus Thompson
//
// LevelReader.java
// This class takes the level text documents and builds the matrix.

import java.io.*;
import java.awt.*;
import java.util.Random;

public class LevelReader
{
	BufferedReader inStream;
	char[][] matrix, matrix2, matrix3;		//matrix: level layout data, matrix2: items, matrix3: more level layout data
	int[][] blockRndMatrix;					//index of random sprite for each block
	int numlines = 0, numrows = 0;
	public int[] boundaries;
	public Point[] enemiesloc;
	public char[] enemies;

	ImageThread im;

    public LevelReader(String fileName, ImageThread image)
    {
		im = image;
		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    	try{
			inStream = new BufferedReader(new FileReader(fileName));
			inStream.read();
    	} catch (FileNotFoundException e)
    	{
    		System.out.println("File Not Found! " + fileName);
    	}
    	catch (IOException e)
    	{ }

		try{
	    	while(true)
				if (inStream.readLine() != null)
				{
					numlines++;
					inStream.readLine();
				}
				else
					break;
		} catch (IOException e)
		{ }

		try{
			inStream = new BufferedReader(new FileReader(fileName));
			inStream.read();
    	} catch (FileNotFoundException e)
    	{ }
    	catch (IOException e)
    	{ }

    	int temp;
    	try{
	    	for (int i = 0; i < numlines; i++)
	    	{
	    		temp = inStream.readLine().length();
				inStream.readLine();

	    		if (temp > numrows)
	    			numrows = temp;
	    	}
    	} catch (IOException e)
    	{ }

    	matrix = new char[numlines][numrows];
    	try{
    		inStream = new BufferedReader(new FileReader(fileName));
    		inStream.read();
	    	for (int i = 0; i < numlines; i++)
	    	{
	    		String s =inStream.readLine();
	    		inStream.readLine();
	    		s.getChars(0, s.length(), matrix[i], 0);
	    	}
    	} catch (Exception e)
    	{ }

		purgeIndent();							//Remove tab intends and newlines from matrix
		purgeBlank();							//Halves the size of the currently half-empty lines
		locateBoundaries();
		separateMatricies();					//Remove some content from matrix to put in matrix2
    	fillGround();							//Fill blank spaces on ground with 'N'
    	fillSky();								//Fill blank spaces in sky with 'S'

    	genRandom();							//give each block a random sprite

    	//output();
	}

	private void purgeIndent()
	{
		for (int i = 0; i < numrows; i++)
			for (int j = 0; j < numlines; j++)
				if (matrix[j][i] == '\t' || matrix[j][i] == '\n' || matrix[j][i] == '\u0000')
					matrix[j][i] = ' ';
	}

	private void purgeBlank()
	{
		char[][] mtemp = new char[matrix.length][(matrix[0].length / 2) + 1];
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[0].length - 1; j += 2)
			{
				mtemp[i][j/2] = matrix[i][j + 1];
			}
		}
		matrix = new char[mtemp.length][mtemp[0].length];
		matrix = mtemp;
	}

	private void locateBoundaries()
	{
		int[] temp = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
		int count = 0;
		for(int j = 0; j < matrix[0].length; j++)
		{
			iloop: for(int i = 0; i < matrix.length; i++)
			{
				if (matrix[i][j] == 'W')
				{
					//System.out.println(j);
					temp[count] = j;
					count++;
					break iloop;
				}
			}
		}
		boundaries = new int[count];
		for (int a = 0; a < boundaries.length; a++)
		{
			boundaries[a] = temp[a];
		}
	}

	private void separateMatricies()
	{
		numlines = matrix.length;
		numrows = matrix[0].length;
		matrix2 = new char[matrix.length][matrix[0].length];
		for (int i = 0; i < numlines; i++)
		{
			for (int j = 0; j < numrows; j++)
			{
				if (matrix[i][j] == 'z')
				{
					im.loc = new Point(j, i);
					im.loc2 = new Point(j + 2, i);
					//System.out.println(im.loc.x + " " + im.loc.y);
				}
				if (matrix[i][j] >= 'a' && matrix[i][j] <= 'z')
				{
					matrix2[i][j] = matrix[i][j];
					matrix[i][j] = ' ';

					char k = matrix[i - 1][j];
					char l = matrix[i][j + 1];
					char m = matrix[i][j - 1];
					if(k == l)
					{
						matrix[i][j] = k;
					}
					else if (k == m)
					{
						matrix[i][j] = k;
					}
					else if (l == m)
					{
						matrix[i][j] = l;
					}
				}
				else
				{
					matrix2[i][j] = ' ';
				}
			}
		}
		
		int counte = 0;
		for (int i = 0; i < numlines; i++)
		{
			for (int j = 0; j < numrows; j++)
			{
				if (matrix2[i][j] == 'a' || matrix2[i][j] == 'b')
				{
					counte++;
				}
			}
		}
		enemies = new char[counte];
		enemiesloc = new Point[counte];
		counte = 0;
		for (int i = 0; i < numlines; i++)
		{
			for (int j = 0; j < numrows; j++)
			{
				if (matrix2[i][j] == 'a' || matrix2[i][j] == 'b')
				{
					enemies[counte] = matrix2[i][j];
					enemiesloc[counte] = new Point(j, i);
					counte++;
				}
			}
		}
	}

	private void fillGround()
	{
		boolean foundGround = false;
		matrix3 = new char[matrix.length][matrix[0].length];
		for (int i = 0; i < numrows; i++)
		{
			for (int j = 0; j < numlines; j++)
			{
				matrix3[j][i] = ' ';
				if (foundGround && matrix[j][i] == ' ')
				{
					matrix[j][i] = 'n';
				}
				else if (foundGround && ( (matrix[j][i] >= '0' && matrix[j][i] <= '9') || (matrix[j][i] >= '!' && matrix[j][i] <= '*') || matrix[j][i] == '@' || matrix[j][i] == '^') )
				{
					matrix3[j][i] = 'n';
				}
				else
				{
					if (foundGround && matrix[j][i] == 'A')
						matrix[j][i] = 'Y';

					if ((matrix[j][i] >= '0' && matrix[j][i] <= '9') || (matrix[j][i] >= '!' && matrix[j][i] <= '*') || matrix[j][i] == '@' || matrix[j][i] == '^')
						foundGround = true;
				}
			}
			foundGround = false;
		}
	}

	private void fillSky()
	{
		for (int i = 0; i < numrows; i++)
		{
			for (int j = 0; j < numlines; j++)
			{
				if (matrix[j][i] == ' ')
					matrix[j][i] = 's';
			}
		}
	}

	private void genRandom()
	{
		Random r = new Random();
		blockRndMatrix = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[0].length; j++)
			{
				//System.out.println(j + " " + i);
				if (matrix[i][j] == '0' || matrix[i][j] == '9')
					blockRndMatrix[i][j] = r.nextInt(7);
				else if (matrix[i][j] == '1' || matrix[i][j] == '!' || matrix[i][j] == '2' || matrix[i][j] == '3' || matrix[i][j] == '@' || matrix[i][j] == '#')
					blockRndMatrix[i][j] = r.nextInt(2);
				else if (matrix[i][j] == '4' || matrix[i][j] == '5' || matrix[i][j] == '6' || matrix[i][j] == '7' || matrix[i][j] == '$' || matrix[i][j] == '%' || matrix[i][j] == '^' || matrix[i][j] == '&')
					blockRndMatrix[i][j] = 0;
				else if (matrix[i][j] == 'n' || matrix[i][j] == '|' || matrix[i][j] == '-' || matrix[i][j] == '~')
					blockRndMatrix[i][j] = r.nextInt(5);
				else
					blockRndMatrix[i][j] = 0;
				//System.out.println(matrix[i][j]);
				//System.out.println(blockRndMatrix[i][j]);
			}
		}
	}

	public void output()
	{
		for (int i = 0; i < numlines; i++)
		{
			for (int j = 0; j < numrows; j++)
			{
				if (matrix2[i][j] != ' ')
					System.out.print(matrix2[i][j]);
				else
				{
					if (matrix[i][j] != 's')
						System.out.print(matrix[i][j]);
					else
						System.out.print(' ');
				}
			}
			System.out.print('\n');
		}

		System.out.println();
    	System.out.println("Rows: " + matrix[0].length);
    	System.out.println("Lines: " + matrix.length);
	}

}