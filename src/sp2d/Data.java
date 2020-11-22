package sp2d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Data {
	public static final boolean verboseDecoder = false;
	public static final boolean verboseCreator = false;
	public static final boolean verboseInit = true;
	public static final boolean verboseConfig = false;
	public static final boolean verboseJSON = true;
	public static final boolean verboseEvals = true;
	
	public static int bestSol;
	public static boolean newBestSol = false;
	public static int currentEvaluation;
	public static BlocksDistribution bestRep;
	public static long lastIterationTime = System.currentTimeMillis();
	
	public static final int SIZEX = 0, SIZEY = 1;
	public static int numBlocks;
	public static int maxHeigth;
	public static int maxWidth;
	public static int[][] blockSizes;
	
	public static float sideSizeMaxProportion;
	
	public static String evalsOutput = "res/default.tsv";
	public static String jsonOutput = "res/default.json";
	public static String evalsOutputPath;
	public static String jsonOutputPath;
	
	public static enum TestCase
	{
			MINI, DEFAULT, BIG, RANDOM
	};
	public static TestCase testCase;
	
	public static void loadData(String filePath)
	{
		bestSol = 0;
		currentEvaluation = 0;
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
			if (verboseInit)
				System.out.println("Success reading the file: " + filePath);
			return;
		}
		catch (IOException e)
		{
			if (verboseInit)
				System.out.println("ERROR: Fail reading the file: " + filePath);
		}
		catch (Exception e)
		{
			if (verboseInit)
				System.out.println("ERROR: Incorrect format of file: " + filePath);
		}
		if (verboseInit)
		System.out.println("Using default random config");
		loadData(5000, 25, 25, 0.5f);
	}
	
	public static void loadData(int numBlocks, int maxWidth, int maxHeigth, float maxSideSizeProportion)
	{
		Data.numBlocks = numBlocks;
		Data.maxHeigth = maxHeigth;
		Data.maxWidth = maxWidth;
		blockSizes = new int[numBlocks][2];
		for (int i = 0; i < numBlocks; i++)
		{
			blockSizes[i][1] = (int) (Math.random() * maxHeigth * maxSideSizeProportion + 1);
			blockSizes[i][0] = (int) (Math.random() * maxWidth * maxSideSizeProportion  + 1);
		}
	}
	
	public static void printDataInfo()
	{
		System.out.println("Number of placebale blocks: " + numBlocks);
		System.out.println("Area limited to "  + maxWidth + "[MaxWidth] and " + maxHeigth + "[MaxHeigth]");
		System.out.println("Block Ids and their size(sideX, sideY)");
		for (int i = 0; i < numBlocks; i++)
			System.out.println(i + "=(" + blockSizes[i][0] + ", " + blockSizes[i][1] + ")");
	}
	
	public static String jsonDataInfo()
	{
		String res = "{\n\t\"numBlocks\":\"" + numBlocks + "\"," + "\n";
		res += "\t\"maxWidth\":\""  + maxWidth  + "\"," + "\n";
		res += "\t\"maxHeigth\":\""  + maxHeigth  + "\"," + "\n";
		res += "\t\"blockSizes\":\n\t[";
		
		int i;
		for (i = 0; i < numBlocks-1; i++)
			res += "\n\t\t{\n\t\t\t\"id\":\"" + i + "\",\n\t\t\t\"sizeX\":\"" + blockSizes[i][0] +
				"\", \n\t\t\t\"sizeY\":\"" + blockSizes[i][1] + "\"\n\t\t},";
		res += "\n\t\t{\n\t\t\t\"id\":\"" + i + "\",\n\t\t\t\"sizeX\":\"" + blockSizes[i][0] +
				"\", \n\t\t\t\"sizeY\":\"" + blockSizes[i][1] + "\"\n\t\t}\n\t],\n";
		
		return res;
	}
}
