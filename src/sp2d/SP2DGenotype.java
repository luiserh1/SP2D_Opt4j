package sp2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.opt4j.core.genotype.PermutationGenotype;

public class SP2DGenotype extends PermutationGenotype<Integer>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public SP2DGenotype()
	{
		super();
	}
	
	public SP2DGenotype(Collection<Integer> values)
	{
		super(values);
	}
	
	public void init()
	{			
		this.clear();
		
		int firstBlock = (int) (Data.randomGen.nextDouble() * Data.numBlocks);
		HashSet<Integer> usedBlocks = new HashSet<Integer>();
		
		int combinations = 0;
		int heigthSum = 0;
		for (int i = 0; i < Data.numBlocks; i++)
		{
			int referenceBlock = firstBlock + i;
			if (referenceBlock > Data.numBlocks - 1)
				referenceBlock -= Data.numBlocks;
			
			ArrayList<Integer> possibleBlocks = new ArrayList<Integer>();
			int referenceHeigth = Data.blockSizes[referenceBlock][1];
			int widthSum = 0;
			
			if (heigthSum + referenceHeigth > Data.maxHeigth)
				continue;
			
			for (int j = 0; j < Data.numBlocks-1; j++)
			{
				int newBlock = referenceBlock + j + 1;
				if (newBlock > Data.numBlocks - 1)
					newBlock -= Data.numBlocks;
				
				if (usedBlocks.contains(newBlock))
					continue;
				
				int newBlockHeigth = Data.blockSizes[newBlock][0];
				if (newBlockHeigth == referenceHeigth)
				{
					int newBlockWidth = Data.blockSizes[newBlock][1];
					if (widthSum + newBlockWidth < Data.maxWidth)
					{
						possibleBlocks.add(newBlock);
						widthSum += newBlockWidth;
					}
					else if (widthSum + newBlockWidth == Data.maxWidth)
					{
						Iterator<Integer> it = possibleBlocks.iterator();
						while (it.hasNext())
						{
							int placedBlock = it.next();
							usedBlocks.add(placedBlock);
							this.add(placedBlock);
						}
						heigthSum += referenceHeigth;
						usedBlocks.add(newBlock);
						this.add(newBlock);
						combinations++;
						break;
					}
				}
			}
		}
		if (Data.verboseCreator)
			System.out.println("Number of combinations: " + combinations + " with " + usedBlocks.size()
			+ " perfectly fitting blocks");
		
		// The permutation is completed
		for (int i = 0; i < Data.numBlocks; i++)
			if (!usedBlocks.contains(i))
				this.add(i);
	}

}
