package sp2d;

import org.opt4j.core.Individual;
import org.opt4j.core.IndividualSet;
import org.opt4j.core.IndividualSetListener;
import org.opt4j.core.optimizer.Population;

public class SP2DIndividualSetListener implements IndividualSetListener {

	public static int changes = 0;
	
	@Override
	public void individualAdded(IndividualSet arg0, Individual arg1) {
		// Population pop = (Population) arg0;
		
		//if (Data.verboseInit)
		//	System.out.println("Individual added");
		
	}

	@Override
	public void individualRemoved(IndividualSet arg0, Individual arg1) {
		if (Data.algorithm == Data.Algorithm.SA)
		{
			Data.lastSolutionAccepted = Data.bestSolIter;
			Data.newSolutionAccepted = true;
		}
		Data.renewedSolutions++;
	}

}
