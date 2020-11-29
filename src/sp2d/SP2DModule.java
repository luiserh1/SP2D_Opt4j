package sp2d;

import java.util.Iterator;
import java.util.Random;

import org.opt4j.core.problem.ProblemModule;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.optimizers.sa.CoolingSchedulesModule;
import org.opt4j.optimizers.sa.SimulatedAnnealingModule;

import com.google.inject.Module;

public class SP2DModule extends ProblemModule {
	
	@Override
	protected void config() {
		
		Data.currentProvider = getProvider(Opt4JTask.class);
		
		if (Data.seed == -1)
		{
			if (Data.verboseInit)
				System.out.println("Random seed init");
			Data.randomGen = new Random(System.currentTimeMillis());
		}
		else
		{
			if (Data.verboseInit)
				System.out.println("Init with seed: " + Data.seed);
			Data.randomGen = new Random(Data.seed);
		}
		
		switch (Data.testCase)
		{
			case MINI:
				Data.loadData("configs/mini.txt");
				break;
			case DEFAULT:
				Data.loadData("configs/default.txt");
				break;
			case DEFAULT20:
				Data.loadData("configs/default20.txt");
				break;
			case BIG:
				Data.loadData("configs/big.txt");
				break;
			case BIG_EXACT:
				Data.loadData("configs/big_exact.txt");
				break;
			case FAMILY:
				Data.loadData("configs/family.txt");
				break;
			case RANDOM:
			default:
				Data.loadData(Data.numRandBlocks, Data.maxWidthR, Data.maxHeigthR, Data.sideSizeMaxProportion);
				break;	
		}
		
		addOptimizerStateListener(SP2DStateListener.class);
		addOptimizerIterationListener(SP2DIterationListener.class);
		
		bindProblem(SP2DCreator.class, SP2DDecoder.class, SP2DEvaluator.class);
	}

}
