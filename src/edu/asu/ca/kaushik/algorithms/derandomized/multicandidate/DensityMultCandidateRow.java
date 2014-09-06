package edu.asu.ca.kaushik.algorithms.derandomized.multicandidate;
import java.util.Arrays;
import java.util.List;

import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;


public class DensityMultCandidateRow extends CondExpEntries {
	private int numCandidates;

	public DensityMultCandidateRow() {
		super();
		
		super.name = new String("DensityMultCandidateRow");
		this.numCandidates = 3;
	}
	
	@Override
	public Integer[] selectRandRow(InteractionGraph ig, int k, int v) {
		Integer[] bestRow = null;
		int bestCoverage = 0;
		
		for (int i = 0; i < this.numCandidates; i++) {
			Integer[] newRow = super.fillRow(super.makeStarredRow(k, v), ig, v);
			int coverage = ig.getCoverage(newRow);
			if (coverage > bestCoverage) {
				bestCoverage = coverage;
				bestRow = newRow;
			}
		}
		
		return bestRow;
	}
	
	@Override 
	protected Integer chooseSymbol(Integer[] newRow, int index, int v, InteractionGraph ig) {
		Integer[] row = Arrays.copyOf(newRow, newRow.length);
		
		List<Integer> symbList = super.makeIndexList(super.makeStarredRow(v, v), v);
		
		double maxCoverage = 0;
		int optSymb = 0;
		for (int i = 0; i < v; i++){
			int symb = super.selectIndexUniRandWORep(symbList);
			row[index] = new Integer(symb);
			double coverage = super.computeExpCoverageSymb(row, index, v, ig);
			if (coverage > maxCoverage){
				maxCoverage = coverage;
				optSymb = symb;
			}
		}
		return new Integer(optSymb);
	}
		

}
