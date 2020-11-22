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
		
		Long deltaMilis = System.currentTimeMillis() - Data.lastIterationTime;
		
		Data.evalsOutput += it + "\t" + Data.currentEvaluation + "\t" + deltaMilis + "\t" + Data.bestSol + "\n";
		Data.lastIterationTime = System.currentTimeMillis();
	}

}
