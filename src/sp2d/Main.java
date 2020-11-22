package sp2d;

import org.opt4j.core.Individual;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.viewer.ViewerModule;

public class Main {
	public static void main (String [] args)
	{
		Data.testCase = Data.TestCase.MINI;
		
		EvolutionaryAlgorithmModule ea = new EvolutionaryAlgorithmModule();
		ea.setGenerations(500);
		ea.setAlpha(100);
		SP2DModule sp2d = new SP2DModule();
		ViewerModule viewer = new ViewerModule();
		viewer.setCloseOnStop(true);
		Opt4JTask task = new Opt4JTask(false);
		task.init(ea, sp2d);
		try {
		        task.execute();
		        Archive archive = task.getInstance(Archive.class);
		        for (Individual individual : archive) {
		                // obtain the phenotype and objective, etc. of each individual
		        }
		} catch (Exception e) {
		        e.printStackTrace();
		} finally {
		        task.close();
		}
	}
}
