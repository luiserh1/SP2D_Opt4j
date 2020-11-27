package sp2d;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.opt4j.core.optimizer.Optimizer;
import org.opt4j.core.optimizer.OptimizerStateListener;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.optimizers.sa.CoolingSchedulesModule;
import org.opt4j.optimizers.sa.SimulatedAnnealingModule;

import com.google.inject.Module;

public class SP2DStateListener implements OptimizerStateListener {

	public SP2DStateListener()
	{
		super();
	}
	
	@Override
	public void optimizationStarted(Optimizer opt)
	{				
		/*if (!Data.initByMain)
		{
			Data.currentTask = Data.currentProvider.get();
			Iterator<Module> it = Data.currentTask.getModules().iterator();
			System.out.println(Data.currentTask);
			while (it.hasNext())
			{
				Module md = it.next();
				if (md instanceof EvolutionaryAlgorithmModule)
					System.out.println("Added EAModule");
				if (md instanceof SimulatedAnnealingModule)
					System.out.println("Added SAModule");
				if (md instanceof CoolingSchedulesModule)
					System.out.println("Added SCModule");
				System.out.println(md);
			}
		}*/
		
		if (Data.algorithm == Data.Algorithm.EA)
		{
			Data.dirSufix = "EA/" + Data.generations + "_" + Data.alpha +	"_" + Data.mu + "_" +
					Data.lambda + "_" + ((int)(Data.crossoverRate * 100)) + "_" + Data.preOrderingHeuristic + "_"
					+ Data.testCase.toString();
		}
		else if (Data.algorithm == Data.Algorithm.SA)
		{
			Data.dirSufix = "SA/" + Data.generations + "_" + Data.coolingSchedule.toString() +	"_" + (int)Data.initialTemperature + "_" +
					(int)Data.finalTemperature + "_" + (int)(Data.saAlpha * 100) + "_" + Data.preOrderingHeuristic +
					"_" + Data.testCase.toString();
		}
		
		if (Data.testCase == Data.TestCase.RANDOM)
			Data.dirSufix += "_" + Data.numRandBlocks + "_" + Data.maxWidthR + "_" + Data.maxHeigthR +
					"_" + ((int)(Data.sideSizeMaxProportion * 100));
		
		Data.evalsOutputPath = "res/evals/" + Data.dirSufix + ".tsv";
		Data.jsonOutputPath = "res/reps/" + Data.dirSufix + ".json";
		
		
		if (Data.preOrderingHeuristic)
		{
			List<int[]> aux = Arrays.asList(Data.blockSizes);
			Collections.sort(aux, new BlockComparator());
			Data.blockSizes = aux.toArray(Data.blockSizes);
			if (Data.verboseCreator)
			{
				System.out.println("Pre-ordered BlockIds");
				for (int i = 0; i < Data.numBlocks; i++)
					System.out.println(i + "=(" + Data.blockSizes[i][0] + ", " + Data.blockSizes[i][1] + ")");
			}
		}
		
		if (Data.algorithm == Data.Algorithm.SA)
		{
			Data.lastSolutionAccepted = 0;
			Data.newSolutionAccepted = false;
		}
			
		
		Data.jsonOutput = Data.jsonDataInfo() + "\t\"evals\":\n\t[\n";
		Data.evalsOutput = "";
		if (Data.verboseConfig)
			Data.printDataInfo();
		
		Data.itersToLog = Data.generations / 100;
		Data.itersToLogCount = 0;
		Data.solSumIter = 0;
		Data.worstSolIter = 2^31;
		Data.bestSolIter = 0;
		Data.renewedSolutions = 0;
		if (Data.verboseInit)
			System.out.println("SP2DStateListener init completed");
	}

	@Override
	public void optimizationStopped(Optimizer opt) {
		String str = "\t\t{\n\t\t\t\"eval\":\"" + Data.currentEvaluation + "\",\n\t\t\t" +
				"\"best_sol\":\"" + Data.bestSol + "\",\n\t\t\t" + 
				"\"rep\":";
		str += "[";
		int i;
		for (i = 0; i < Data.bestRep.getCoords().length - 1; i++)
			str += Data.bestRep.getCoords()[i].toJson() + ", ";
		str += Data.bestRep.getCoords()[i].toJson() + "]\n\t\t}\n\t]\n}";
		Data.jsonOutput += str;
		
		if (Data.verboseJSON)
			System.out.println(Data.jsonOutput);
		if (Data.verboseEvals)
			System.out.println(Data.evalsOutput);
		
		// Storing the output in the corresponding files
		BufferedWriter evalsWriter, jsonWriter;
		try {
			evalsWriter = new BufferedWriter(new FileWriter(Data.evalsOutputPath));
			evalsWriter.write(Data.evalsOutput);
			jsonWriter = new BufferedWriter(new FileWriter(Data.jsonOutputPath));
			jsonWriter.write(Data.jsonOutput);
			evalsWriter.close();
			jsonWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	    
		if (Data.verboseProgress)
			System.out.println(Data.dirSufix + " - 100%");
	}

}
