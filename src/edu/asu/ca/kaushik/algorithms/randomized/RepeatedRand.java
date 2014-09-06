package edu.asu.ca.kaushik.algorithms.randomized;
import java.util.Random;

import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;


public class RepeatedRand extends RandCAGenAlgo {
	
	private Random rand;
	
	public RepeatedRand() {
		super();
		super.name = new String("Repeated Random");
		this.rand = new Random(1234L);
	}

	@Override
	protected Integer[] createRandRow(InteractionGraph ig, int k, int v) {
		InteractionGraph igCopy = new InteractionGraph(ig);
		
		Integer[] newRow = new Integer[k];
		for (int i = 0; i < newRow.length; i++){
			newRow[i] = new Integer(v);
		}
		
		//System.out.println("starting:\n" + igCopy.toString() + "\n\n");
		
		while(!this.terminateP(newRow, v, igCopy)) {
			Interaction interaction = igCopy.getPropRandInteraction(newRow);
			newRow = this.updateRow(newRow, interaction);
			
			igCopy.deleteFullyDeterminedInteractions(newRow);
			igCopy.deletIncompatibleInteractions(interaction);	
			
			//System.out.println(interaction.toString());
			//System.out.println("row:\n" + getStringRow(newRow));
			//System.out.println(igCopy.toString() + "\n");
		}
		
		this.fixAllCols(newRow, v);
		return newRow;
	}


	private Integer[] updateRow(Integer[] newRow, Interaction interaction) {
		int[] fixCols = interaction.getCols().getCols();
		int[] fixSyms = interaction.getSyms().getSyms();
		for (int i = 0; i < fixCols.length; i++) {
			newRow[fixCols[i]] = new Integer(fixSyms[i]);
		}
		return newRow;
	}

	private void fixAllCols(Integer[] newRow, int v) {
		for (int i = 0; i < newRow.length; i++) {
			if (newRow[i].intValue() == v){
				newRow[i] = new Integer(this.rand.nextInt(v));
			}
		}
	}

	private boolean terminateP(Integer[] newRow, int v, InteractionGraph igCopy) {
		return igCopy.isEmpty() || this.allColsFixedP(newRow, v);
	}

	private boolean allColsFixedP(Integer[] newRow, int v) {
		for (Integer i : newRow) {
			if (i.intValue() == v){
				return false;
			}
		}
		return true;
	}
	
}
