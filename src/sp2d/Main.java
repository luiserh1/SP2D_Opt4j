package sp2d;

import org.opt4j.core.Individual;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.optimizers.sa.CoolingSchedulesModule;
import org.opt4j.optimizers.sa.SimulatedAnnealingModule;
import org.opt4j.viewer.ViewerModule;

public class Main {
	public static void main (String [] args)
	{				
		SP2DModule sp2d = new SP2DModule();
		ViewerModule viewer = new ViewerModule();
		viewer.setCloseOnStop(true);
		Opt4JTask task = new Opt4JTask(false);
		
		if (Data.algorithm == Data.Algorithm.EA)
		{
			EvolutionaryAlgorithmModule ea = new EvolutionaryAlgorithmModule();
			ea.setGenerations(Data.generations);
			ea.setAlpha(Data.alpha);
			ea.setLambda(Data.lambda);
			ea.setMu(Data.mu);
			ea.setCrossoverRate(Data.crossoverRate);
			task.init(ea, sp2d);
			
			Data.dirSufix = "EA/" + Data.generations + "_" + Data.alpha +	"_" + Data.lambda + "_" +
					Data.mu + "_" + ((int)(Data.crossoverRate * 100)) + "_" + Data.preOrderingHeuristic + "_"
					+ Data.testCase.toString();
		}
		else if (Data.algorithm == Data.Algorithm.SA)
		{
			SimulatedAnnealingModule sam = new SimulatedAnnealingModule();
			sam.setIterations(Data.generations);
			
			CoolingSchedulesModule cs = new CoolingSchedulesModule();
			
			cs.setType(Data.coolingSchedule);
			cs.setInitialTemperature(Data.initialTemperature);
			cs.setFinalTemperature(Data.finalTemperature);
			cs.setAlpha(Data.saAlpha);
			
			task.init(sam, sp2d);
			task.getModules().add(cs);
			
			Data.dirSufix = "SA/" + Data.generations + "_" + Data.coolingSchedule.toString() +	"_" + (int)Data.initialTemperature + "_" +
					(int)Data.finalTemperature + "_" + (int)(Data.saAlpha * 100) + "_" + Data.preOrderingHeuristic +
					"_" + Data.testCase.toString();
		}
		
		if (Data.testCase == Data.TestCase.RANDOM)
			Data.dirSufix += "_" + Data.numRandBlocks + "_" + Data.maxWidthR + "_" + Data.maxHeigthR +
					"_" + ((int)(Data.sideSizeMaxProportion * 100));
		
		Data.evalsOutputPath = "res/evals/" + Data.dirSufix + ".tsv";
		Data.jsonOutputPath = "res/reps/" + Data.dirSufix + ".json";

		try {
		        task.execute();
		        Archive archive = task.getInstance(Archive.class);
		        for (Individual individual : archive) {
		                // obtain the phenotype and objective, etc. of each individual
		        }
		} catch (Exception e) {
		        e.printStackTrace();
		} finally {
		        task.close();
		}
	}
}
