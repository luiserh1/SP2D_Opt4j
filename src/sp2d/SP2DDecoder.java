package sp2d;

import java.util.Iterator;

import org.opt4j.core.genotype.PermutationGenotype;
import org.opt4j.core.problem.Decoder;

public class SP2DDecoder implements Decoder<PermutationGenotype<Integer>, Integer[][]> {

	@Override
	public Integer[][] decode(PermutationGenotype<Integer> geno) {
		Integer[][] pheno = new Integer[Data.numBlocks][2];
		
		for (int i = 0; i < Data.numBlocks; i++)
			pheno[i][0] = -1; // If the X axis value is -1, the block remains unplaced
		
		Iterator<Integer> it = geno.iterator();
		int currentHeigth = 0, currentWidth = 0;
		int maxHeigthInLine = 0;
		while(it.hasNext())
		{
			int nextBlock = it.next();
			
			int sizeX = Data.blockSizes[nextBlock][0];
			int sizeY = Data.blockSizes[nextBlock][0];
			
			pheno[nextBlock][0] = currentWidth;
			pheno[nextBlock][1] = currentHeigth;
			
			if (sizeY > maxHeigthInLine)
				maxHeigthInLine = sizeY;
			
			currentWidth += sizeX;
			if (currentWidth > Data.maxWidth)
			{
				currentWidth = 0;
				currentHeigth += maxHeigthInLine;
				if (currentHeigth > Data.maxHeigth)
					break;
			}
		}
		
		return pheno;
	}

}
