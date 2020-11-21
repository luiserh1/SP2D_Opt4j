package sp2d;

public class BlocksDistribution {

	Coords[] coords;
	
	public BlocksDistribution(Coords[] coords)
	{
		this.coords = coords;
	}
	
	@Override
	public String toString()
	{
		String res = "[";
		int i;
		for (i = 0; i < coords.length - 1; i++)
			res += coords[i].toString();
		res += coords[i].toString() + "]";
		return res;
	}
	
	public float evaluate()
	{
		int placedBlocks = 0;
		for (int i = 0; i < Data.numBlocks; i++)
			if (this.coords[i].isPlaced())
				placedBlocks++;
		
		return (float) placedBlocks / Data.numBlocks;
	}
	
}
