package sp2d;

import java.util.Comparator;

public class BlockComparator implements Comparator<int[]> 
{

	@Override
	public int compare(int[] arg0, int[] arg1)
	{
		return arg0[1] - arg1[1];
	}

}
