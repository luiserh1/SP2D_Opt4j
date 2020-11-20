package sp2d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Data {
	public static final int SIZEX = 0, SIZEY = 1;
	public static int numBlocks;
	public static int maxHeigth;
	public static int maxWidth;
	public static int[][] blockSizes;
	
	public static void loadData(String filePath)
	{
		try
		{
			File file = new File(filePath); 
			  
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			
			// First line represents the number of blocks
			String st = br.readLine();
			numBlocks = Integer.parseInt(st);
			
			// Second line represents the heigth limit
			st = br.readLine();
			maxHeigth = Integer.parseInt(st);
			
			// Third line represents the width limit
			st = br.readLine();
			maxWidth = Integer.parseInt(st);
			
			blockSizes = new int[numBlocks][2];
			int count = 0;
			while ((st = br.readLine()) != null)
			{				
				int sizeX, sizeY;
				int sepIndex = st.indexOf(" ");
				sizeX = Integer.parseInt(st.substring(0, sepIndex));
				sizeY = Integer.parseInt(st.substring(sepIndex+1));
				
				blockSizes[count][SIZEX] = sizeX;
				blockSizes[count][SIZEY] = sizeY;
				
				//System.out.println(st + ": " + sizeX +"," + sizeY);
				count++;
			}
			br.close();
			return;
		}
		catch (IOException e)
		{
			System.out.println("ERROR: Fail reading the file");
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Incorrect format of file");
		}
		System.out.println("Using default config");
		
		numBlocks = 12;
		maxHeigth = 10;
		maxWidth = 10;
		blockSizes = new int[numBlocks][2];
		for (int i = 0; i < numBlocks; i++)
		{
			blockSizes[i][1] = (int) (Math.random() * maxHeigth + 1);
			blockSizes[i][0] = (int) (Math.random() * maxWidth + 1);
		}
		
	}
}
