package sp2d;

import java.util.ArrayList;

import org.opt4j.core.genotype.PermutationGenotype;
import org.opt4j.core.problem.Creator;

public class SP2DCreator implements Creator<PermutationGenotype<Integer>> {

	@Override
	public PermutationGenotype<Integer> create() {
		ArrayList<Integer> blockIds = new ArrayList<Integer>();
		
		for (int i = 0; i < Data.numBlocks; i++ )
			blockIds.add(i);
		
		PermutationGenotype<Integer> genotype = new PermutationGenotype<Integer>();
		return genotype;
	}

}
