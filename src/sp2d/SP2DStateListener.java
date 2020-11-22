package sp2d;

import org.opt4j.core.optimizer.Optimizer;
import org.opt4j.core.optimizer.OptimizerStateListener;

public class SP2DStateListener implements OptimizerStateListener {

	public SP2DStateListener()
	{
		super();
	}
	
	@Override
	public void optimizationStarted(Optimizer opt)
	{
		Data.jsonOutput += Data.jsonDataInfo() + "\t\"evals\":\n\t[\n";
		if (Data.verboseConfig)
			Data.printDataInfo();
		
		
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
	}

}
