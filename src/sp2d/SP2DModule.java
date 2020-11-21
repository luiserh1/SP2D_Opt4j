package sp2d;

import org.opt4j.core.problem.ProblemModule;

public class SP2DModule extends ProblemModule {
	
	@Override
	protected void config() {
		Data.loadData("configs/mini.txt");
		//Data.loadData("configs/default.txt");
		//Data.loadData("configs/big.txt");
		//Data.loadData(5000, 50, 50, 0.66f);
		if (Data.verboseConfig)
			Data.printDataInfo();
		bindProblem(SP2DCreator.class, SP2DDecoder.class, SP2DEvaluator.class);
	}

}
