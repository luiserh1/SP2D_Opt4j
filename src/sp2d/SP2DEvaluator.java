package sp2d;

import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class SP2DEvaluator implements Evaluator<BlocksDistribution>{

	@Override
	public Objectives evaluate(BlocksDistribution pheno) {		
		Objectives objectives = new Objectives();
		float value = pheno.evaluate();
		objectives.add("Porción Colocados-MAX", Sign.MAX, value);
		
		//System.out.println(placedBlocks + "/" + Data.numBlocks + "=" + value);
		return objectives;
	}

}
