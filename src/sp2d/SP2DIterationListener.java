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
		
		float average = 1f;
		if (Data.algorithm == Data.Algorithm.EA)
		{
			average = (float)Data.solSumIter / Data.alpha;
			Data.evalsOutput += it + "\t" + Data.currentEvaluation + "\t" + deltaMilis + "\t"
					+ Data.worstSolIter + "\t" + Data.bestSolIter + "\t" + average + "\t"
					+ Data.bestSol + "\n";
		}
		else
			Data.evalsOutput += it + "\t" + Data.currentEvaluation + "\t" + deltaMilis
					+ "\t" +  Data.bestSol + "\n";
			
		
		Data.itersToLogCount++;
		if (Data.verboseProgress && Data.itersToLogCount > Data.itersToLog)
		{
			Data.itersToLogCount = 0;
			System.out.println(Data.dirSufix + " - " + (int)((float)it / Data.generations * 100) + "%");
		}
		
		Data.solSumIter = 0;
		Data.worstSolIter = 0;
		Data.bestSolIter = 0;
	}

}
