package sp2d;

import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class SP2DEvaluator implements Evaluator<Integer[][]>{

	@Override
	public Objectives evaluate(Integer[][] pheno) {
		int placedBlocks = 0;
		for (int i = 0; i < Data.numBlocks; i++)
			if (pheno[i][0] < 0)
				placedBlocks++;
		
		Objectives objectives = new Objectives();
		float value = (float) placedBlocks / Data.numBlocks;
		objectives.add("Valor objetivo-MAX", Sign.MAX, value);
		return objectives;
	}

}
