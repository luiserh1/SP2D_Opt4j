package sp2d;

import java.util.Iterator;

import org.opt4j.core.problem.Decoder;

public class SP2DDecoder implements Decoder<SP2DGenotype, BlocksDistribution> {

	@Override
	public BlocksDistribution decode(SP2DGenotype geno) {
		Coords[] phenoCoords = new Coords[Data.numBlocks];
		
		for (int i = 0; i < Data.numBlocks; i++)
			phenoCoords[i] = new Coords();
		
		Iterator<Integer> it = geno.iterator();
		int currentY = 0, currentX = 0;
		int maxHeigthInLine = 0;
		while(it.hasNext())
		{
			int nextBlock = it.next();
			
			int sizeX = Data.blockSizes[nextBlock][0];
			int sizeY = Data.blockSizes[nextBlock][1];
			
			if (Data.verboseDecoder)
				System.out.print("nextBlock=" + nextBlock + " | sizeX=" + sizeX + " | sizeY=" + sizeY + " | currentX=" + currentX +
					" | currentY=" + currentY + " | maxHeigthInLine=" + maxHeigthInLine);
				
			if (currentX + sizeX > Data.maxWidth)
			{
				if (sizeX > Data.maxWidth)
					break;
				
				currentX = 0;				
				currentY += maxHeigthInLine;
				maxHeigthInLine = 0;
			}
			
			if (sizeY > maxHeigthInLine)
			{
				maxHeigthInLine = sizeY;
				if (currentY + sizeY > Data.maxHeight)
					break;
			}
			
			phenoCoords[nextBlock] = new Coords(currentX, currentY);
			
			if (Data.verboseDecoder)
				System.out.print(" || newMaxHeigthInLine=" + maxHeigthInLine);
			
			currentX += sizeX;
			
			if (Data.verboseDecoder)
				System.out.println(" | newX=" + currentX + " | newY=" + currentY + 
						" | coords" + phenoCoords[nextBlock].toString());
			
		}
		
		BlocksDistribution pheno = new BlocksDistribution(phenoCoords);
		if (Data.verboseDecoder)
			System.out.println("\n" + pheno.toString() + "\n==============================");
		
		return pheno;
	}

}
