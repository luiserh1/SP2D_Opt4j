package sp2d;

import java.util.Comparator;

public class BlockComparator implements Comparator<int[]> 
{

	@Override
	public int compare(int[] arg0, int[] arg1)
	{
		int greater = arg0[1] - arg1[1];
		if (greater == 0)
			return greater + arg0[0] - arg1[0];
		else
			return greater;
	}

}
