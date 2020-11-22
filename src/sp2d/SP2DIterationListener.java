package sp2d;

import org.opt4j.core.optimizer.OptimizerIterationListener;

public class SP2DIterationListener implements OptimizerIterationListener
{

	@Override
	public void iterationComplete(int it) {
		if (Data.newBestSol)
		{
			String str = Data.bestRep.toJson();
			Data.newBestSol = false;
			Data.jsonOutput += str + "\n";
		}
		
		Long deltaMilis = System.currentTimeMillis() - Data.preIterationTime;
		
		Data.evalsOutput += it + "\t" + Data.currentEvaluation + "\t" + deltaMilis + "\t" + Data.bestSol + "\n";
		
		Data.itersToLogCount++;
		if (Data.verboseProgress && Data.itersToLogCount > Data.itersToLog)
		{
			Data.itersToLogCount = 0;
			System.out.println(Data.dirSufix + " - " + (int)((float)it / Data.generations * 100) + "%");
		}
	}

}
