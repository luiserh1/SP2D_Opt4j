package sp2d;

import java.util.Iterator;

//import org.opt4j.core.Individual;
//import org.opt4j.optimizers.ea.Nsga2Module;
//import org.opt4j.core.optimizer.Archive;
//import org.opt4j.optimizers.ea.ElitismSelectorModule;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.ea.EvolutionaryAlgorithmModule;
import org.opt4j.optimizers.sa.CoolingScheduleExponential;
import org.opt4j.optimizers.sa.CoolingScheduleHyperbolic;
import org.opt4j.optimizers.sa.CoolingScheduleLinear;
import org.opt4j.optimizers.sa.CoolingSchedulesModule;
import org.opt4j.optimizers.sa.SimulatedAnnealingModule;
import org.opt4j.viewer.ViewerModule;

public class Main {
	public static void main (String [] args)
	{				
		Data.initByMain = true;
		
		SP2DModule sp2d = new SP2DModule();
		ViewerModule viewer = new ViewerModule();
		viewer.setCloseOnStop(true);
		Opt4JTask task = new Opt4JTask(Data.closeOnFinish);
		
		if (Data.algorithm == Data.Algorithm.EA)
		{
			EvolutionaryAlgorithmModule ea = new EvolutionaryAlgorithmModule();
			Data.ea = ea;
			
			ea.setGenerations(Data.generations);
			ea.setAlpha(Data.alpha);
			ea.setLambda(Data.lambda);
			ea.setMu(Data.mu);
			ea.setCrossoverRate(Data.crossoverRate);
			task.init(ea, sp2d);
			//task.getModules().add(new ElitismSelectorModule());
			
		}
		else if (Data.algorithm == Data.Algorithm.SA)
		{
			SimulatedAnnealingModule sam = new SimulatedAnnealingModule();
			sam.setIterations(Data.generations);
			Data.sam = sam;
			
			CoolingSchedulesModule cs = new CoolingSchedulesModule();
			Data.csm = cs;
			
			switch (Data.coolingSchedule) {
			case LINEAR:
				Data.cs = new CoolingScheduleLinear(Data.initialTemperature, Data.finalTemperature);
				break;
			case HYPERBOLIC:
				Data.cs = new CoolingScheduleHyperbolic(Data.initialTemperature, Data.finalTemperature);
				break;
			default: // EXPONENTIAL
				Data.cs = new CoolingScheduleExponential(Data.initialTemperature, Data.finalTemperature, Data.saAlpha);
				break;
			}
			
			cs.setType(Data.coolingSchedule);
			cs.setInitialTemperature(Data.initialTemperature);
			cs.setFinalTemperature(Data.finalTemperature);
			cs.setAlpha(Data.saAlpha);
			
			task.init(cs, sam, sp2d);
			
		}

		SP2DTaskStateListener listener = new SP2DTaskStateListener();
		task.addStateListener(listener);
		try {
		        task.execute();
		        if (!Data.closeOnFinish)
		        {		        	
			        Iterator<com.google.inject.Module> it = task.getModules().iterator();
			        while (it.hasNext())
			        {
			        	com.google.inject.Module mod = it.next();
			        	System.out.println(mod);
			        }
		        }
		        
		} catch (Exception e) {
		        e.printStackTrace();
		} finally {
		        task.close();
		}
	}
}
