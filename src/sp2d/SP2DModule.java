package sp2d;

import org.opt4j.core.problem.ProblemModule;

public class SP2DModule extends ProblemModule {
	
	@Override
	protected void config() {
		
		switch (Data.testCase)
		{
			case MINI:
				Data.loadData("configs/mini.txt");
				break;
			case DEFAULT:
				Data.loadData("configs/default.txt");
				break;
			case BIG:
				Data.loadData("configs/big.txt");
				break;
			case RANDOM:
			default:
				Data.sideSizeMaxProportion = 0.66f;
				Data.loadData(256, 8, 8, Data.sideSizeMaxProportion);
				break;	
		}
		
		addOptimizerStateListener(SP2DStateListener.class);
		addOptimizerIterationListener(SP2DIterationListener.class);
		
		bindProblem(SP2DCreator.class, SP2DDecoder.class, SP2DEvaluator.class);
	}

}
