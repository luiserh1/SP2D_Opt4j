package sp2d;

import org.opt4j.core.config.Task;
import org.opt4j.core.config.TaskStateListener;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;

public class SP2DTaskStateListener implements TaskStateListener {

	private static boolean initiallized;
	
	public SP2DTaskStateListener()
	{
		super();
		initiallized = false;
	}
	
	@Override
	public void stateChanged(Task task) {
		if (!initiallized)
		{
			Opt4JTask opt4jtask = (Opt4JTask) task;
			Archive archive = opt4jtask.getInstance(Archive.class);
			SP2DIndividualSetListener listener = new SP2DIndividualSetListener();
			archive.addListener(listener);
			if (Data.verboseInit)
				System.out.println(task.getState());
			initiallized = true;
		}		
	}

}
