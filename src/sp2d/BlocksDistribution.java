package sp2d;

public class BlocksDistribution {

	Coords[] coords;
	private int myEvalNum;
	
	public BlocksDistribution(Coords[] coords)
	{
		this.coords = coords;
		myEvalNum = -1;
	}
	
	public Coords[] getCoords()
	{
		return this.coords;
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
		String res = "\t\t{\n\t\t\t\"eval\":\"" + myEvalNum + "\",\n\t\t\t" +
				"\"best_sol\":\"" + Data.bestSol + "\",\n\t\t\t" + 
				"\"rep\":";
		res += "[";
		int i;
		for (i = 0; i < getCoords().length - 1; i++)
			res += getCoords()[i].toJson() + ", ";
		res += getCoords()[i].toJson() + "]\n\t\t},";
		
		return res;
	}
	
	public int evaluate()
	{
		int placedBlocks = 0;
		for (int i = 0; i < Data.numBlocks; i++)
			if (this.coords[i].isPlaced())
				placedBlocks++;
		
		Data.currentEvaluation++;
		if (placedBlocks > Data.bestSol + 1)
		{
			Data.bestSol = placedBlocks;
			Data.bestRep = this;
			myEvalNum = Data.currentEvaluation;
			Data.newBestSol = true;
		}
		
		return placedBlocks;
	}
	
}
