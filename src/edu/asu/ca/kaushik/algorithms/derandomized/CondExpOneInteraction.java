package edu.asu.ca.kaushik.algorithms.derandomized;
import edu.asu.ca.kaushik.algorithms.structures.Interaction;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;


public class CondExpOneInteraction extends CondExpEntries {
	
	public CondExpOneInteraction(){
		super();
		super.name = new String("Cond Exp One Interaction");
	}

	@Override
	public Integer[] selectRandRow(InteractionGraph ig, int k, int v){
		Integer[] newRow = super.makeStarredRow(k, v);
		
		Interaction firstInteraction = ig.selectInteraction(newRow);
		// time complexity measurement
		super.comp = super.comp + ig.getComp();
		
		newRow = super.updateRow(newRow, firstInteraction);
		return super.fillRow(newRow, ig, v);	
	}
}
