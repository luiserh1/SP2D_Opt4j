package sp2d;

import java.io.BufferedReader;
import com.google.inject.Module;
import com.google.inject.Provider;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.optimizers.sa.CoolingSchedule;
import org.opt4j.optimizers.sa.CoolingSchedulesModule;
import org.opt4j.optimizers.sa.SimulatedAnnealingModule;

public class Data {
	// Config. variables
	// Verbosity
	public static final boolean verboseDecoder = false;
	public static final boolean verboseCreator = false;
	public static final boolean verboseInit = true;
	public static final boolean verboseConfig = false;
	public static final boolean verboseJSON = false;
	public static final boolean verboseEvals = false;
	public static final boolean verboseProgress = true;
	
	// To determine the random numbers. If -1 the time is used
	public static final int seed = -1; // It is not reliable, the optimizers have its own randomizers
	
	public static final boolean closeOnFinish = true;
	
	// Type of case
	public static final TestCase testCase = TestCase.FAMILY;
	// Algorithm
	public static final Algorithm algorithm = Algorithm.EA;
	
	// Preordeering first
	public static final boolean preOrderingHeuristic = false;
	public static final boolean creationHeuristic = true; // Requieres preOrderingHeuristic
	
	// If TestCae.RANDOM
	public static final float sideSizeMaxProportion = 0.66f;
	public static final int numRandBlocks = 4096;
	public static final int maxHeigthR = 32;
	public static final int maxWidthR = 32;
	
	// When launched from the Main class
	public static final int generations = 150000; // Or iterations per re-heating (not implemented yet) in SA
	// If Algorithm.EA
	public static final int alpha = 40;	// Number of initial individuals
	public static final int mu = 4;		// Number of parents per generation
	public static final int lambda = 10;	// Number of offsprings per generation
	public static final float crossoverRate = 0.9f;	// Rate which the crossover action is performed with
	//public static final int reheatings = 3;
	// If Algorithm.SA
	public static CoolingSchedulesModule.Type coolingSchedule = CoolingSchedulesModule.Type.EXPONENTIAL; 
	public static float initialTemperature = 10000.0f;
	public static float finalTemperature = 1f;
	public static float saAlpha = 0.99f;

	
	// Aux. variables, enums...
	public static enum TestCase
	{
			MINI, DEFAULT, BIG, RANDOM, DEFAULT20, BIG_EXACT, FAMILY
	};
	
	public static enum Algorithm
	{
		EA, SA
	};
	
	public static boolean initByMain = false;
	public static Opt4JTask currentTask;
	public static Provider<Opt4JTask> currentProvider;
	
	public static Random randomGen;
	
	public static CoolingSchedulesModule csm;
	public static CoolingSchedule cs;
	public static SimulatedAnnealingModule sam;
	public static EvolutionaryAlgorithmModule ea;
	
	public static int bestSol;
	public static boolean newBestSol = false;
	public static int currentEvaluation;
	public static BlocksDistribution bestRep;
	public static int worstSolIter;
	public static int bestSolIter;
	public static int solSumIter;
	public static long preIterationTime = System.currentTimeMillis();
	public static boolean newSolutionAccepted;
	public static int lastSolutionAccepted;
	public static int renewedSolutions;
	
	public static final int SIZEX = 0, SIZEY = 1;
	public static int numBlocks;
	public static int maxHeight;
	public static int maxWidth;
	public static int[][] blockSizes;
	
	public static String evalsOutput;
	public static String jsonOutput;
	public static String evalsOutputPath = "res/default.tsv";
	public static String jsonOutputPath = "res/default.json";
	
	public static int itersToLog;
	public static int itersToLogCount;
	//public static int currentCooling;
	
	public static String dirSufix = "default";
	
	
	// Aux. Methods
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
			maxHeight = Integer.parseInt(st);
			
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
		Data.maxHeight = maxHeigth;
		Data.maxWidth = maxWidth;
		blockSizes = new int[numBlocks][2];
		int numB1x1 = 0;
		for (int i = 0; i < numBlocks; i++)
		{
			blockSizes[i][1] = (int) (randomGen.nextDouble() * maxHeigth * maxSideSizeProportion + 1);
			blockSizes[i][0] = (int) (randomGen.nextDouble() * maxWidth * maxSideSizeProportion  + 1);
			if (blockSizes[i][0] == 1 && blockSizes[i][1] == 1)
				numB1x1++;
		}
		if (verboseInit)
			System.out.println("Problem with " + numB1x1 + " blocks of area 1");
	}
	
	public static void printDataInfo()
	{
		System.out.println("Number of placebale blocks: " + numBlocks);
		System.out.println("Area limited to "  + maxWidth + "[MaxWidth] and " + maxHeight + "[MaxHeigth]");
		System.out.println("Block Ids and their size(sideX, sideY)");
		for (int i = 0; i < numBlocks; i++)
			System.out.println(i + "=(" + blockSizes[i][0] + ", " + blockSizes[i][1] + ")");
	}
	
	public static String jsonDataInfo()
	{
		String res = "{\n\t\"numBlocks\":\"" + numBlocks + "\"," + "\n";
		res += "\t\"maxWidth\":\""  + maxWidth  + "\"," + "\n";
		res += "\t\"maxHeigth\":\""  + maxHeight  + "\"," + "\n";
		res += "\t\"blockSizes\":\n\t[";
		
		int i;
		for (i = 0; i < numBlocks-1; i++)
			res += "\n\t\t{\n\t\t\t\"id\":\"" + i + "\",\n\t\t\t\"sizeX\":\"" + blockSizes[i][0] +
				"\", \n\t\t\t\"sizeY\":\"" + blockSizes[i][1] + "\"\n\t\t},";
		res += "\n\t\t{\n\t\t\t\"id\":\"" + i + "\",\n\t\t\t\"sizeX\":\"" + blockSizes[i][0] +
				"\", \n\t\t\t\"sizeY\":\"" + blockSizes[i][1] + "\"\n\t\t}\n\t],\n";
		
		return res;
	}
	
	
	public static void loadResStateFromJSON(String path)
	{
		String content = "";
		 
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(path) ) );
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        Object file = JSONValue.parse(content);
        JSONObject jsonObjectdecode = (JSONObject)file;
        
		numBlocks = Integer.parseInt((String)jsonObjectdecode.get("numBlocks"));
		maxWidth = Integer.parseInt((String)jsonObjectdecode.get("numBlocks"));
		maxHeight = Integer.parseInt((String)jsonObjectdecode.get("maxHeigth"));
		
		JSONArray blocksJSON = (JSONArray) jsonObjectdecode.get("blockSizes");
		blockSizes = new int[numBlocks][2];
		for (int i = 0; i < numBlocks; i++)
	    {
	        JSONObject jsonEntry = (JSONObject) blocksJSON.get(i);
	        JSONObject entry = (JSONObject) jsonEntry.get("entry");
	        int id = Integer.parseInt((String)entry.get("id"));
	        int sizeX = Integer.parseInt((String)entry.get("sizeX"));
	        int sizeY = Integer.parseInt((String)entry.get("sizeY"));
	        blockSizes[id][0] = sizeX;
	        blockSizes[id][1] = sizeY;
	    }
		
		JSONArray evalsJSON = (JSONArray) jsonObjectdecode.get("blockSizes");
		JSONObject bestEval = (JSONObject) blocksJSON.get(evalsJSON.size()-1);
		currentEvaluation = Integer.parseInt((String)bestEval.get("eval"));
		bestSol = Integer.parseInt((String)bestEval.get("bestSol"));
	}
}
