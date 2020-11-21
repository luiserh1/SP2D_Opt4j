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
	
	public String toJson()
	{
		String res = "{\n\t\"eval\":" + Data.currentEvaluation + "\n\t" +
				"\"best_sol\":" + Data.bestSol + "\n\t" + 
				"\"rep\":";
		res += "[";
		int i;
		for (i = 0; i < coords.length - 1; i++)
			res += coords[i].toJson() + ", ";
		res += coords[i].toJson() + "]\n}";
		return res;
	}
	
	public int evaluate()
	{
		int placedBlocks = 0;
		for (int i = 0; i < Data.numBlocks; i++)
			if (this.coords[i].isPlaced())
				placedBlocks++;
		
		Data.currentEvaluation++;
		if (placedBlocks + Data.verboseSols > Data.bestSol + 1)
		{
			Data.bestSol = placedBlocks;
			System.out.println(this.toJson());
		}
		
		return placedBlocks;
	}
	
}
