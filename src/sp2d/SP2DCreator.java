package sp2d;

import java.util.ArrayList;

import org.opt4j.core.problem.Creator;

public class SP2DCreator implements Creator<SP2DGenotype> {

	@Override
	public SP2DGenotype create() {
		ArrayList<Integer> blockIds = new ArrayList<Integer>();
		
		for (int i = 0; i < Data.numBlocks; i++ )
			blockIds.add(i);
		
		SP2DGenotype genotype = new SP2DGenotype(blockIds);
		//genotype.init(new Random());
		genotype.init(true);
		return genotype;
	}

}
