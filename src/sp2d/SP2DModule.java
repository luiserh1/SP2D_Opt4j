package sp2d;

import org.opt4j.core.problem.ProblemModule;

public class SP2DModule extends ProblemModule {

	@Override
	protected void config() {
		Data.loadData("../configs/default.txt");
		bindProblem(SP2DCreator.class, SP2DDecoder.class, SP2DEvaluator.class);
	}

}
