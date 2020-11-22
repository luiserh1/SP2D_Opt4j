package sp2d;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.opt4j.core.config.annotations.File;
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
		Data.jsonOutput = Data.jsonDataInfo() + "\t\"evals\":\n\t[\n";
		Data.evalsOutput = "";
		if (Data.verboseConfig)
			Data.printDataInfo();
		
		Data.itersToLog = Data.generations / 100;
		Data.itersToLogCount = 0;
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
