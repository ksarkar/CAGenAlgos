package edu.asu.ca.kaushik.algorithms.derandomized.multicandidate.bestofk;
import edu.asu.ca.kaushik.algorithms.CAGenAlgo;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpEntries;
import edu.asu.ca.kaushik.algorithms.derandomized.CondExpIteration;
import edu.asu.ca.kaushik.algorithms.structures.CA;
import edu.asu.ca.kaushik.algorithms.structures.InteractionGraph;


public class BestOfKDensityHybrid implements CAGenAlgo {
	//private Random rand;
	protected String name;
	
	//time complexity computation
	protected int comp;
	
	protected CondExpEntries density;
	private CondExpIteration interaction;
	
	//private long cutOff;
	private int factor;
	private int numSamples;
	
	public BestOfKDensityHybrid(){
		super();
		//this.rand = new Random(1234L);
		this.name = new String("BestOfKDensityHybrid");
		
		//time complexity computation
		this.comp = 0;
		
		this.density = new CondExpEntries();
		this.interaction = new CondExpIteration();
		
		this.factor = 100;
		
		this.numSamples = 10;
	}

	@Override
	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;

	}

	@Override
	public CA generateCA(int t, int k, int v) {
		CA bestCa = new CA(k);
		long bestNumInteraction = Long.MAX_VALUE;
		//this.cutOff = Math.round(numInteraction * 0.20);
		long cutOff = this.setCutoff(t, k, v, this.factor);
		
		InteractionGraph bestIg = null;
		
		// best of K density sampling part
		for (int i = 0 ; i < this.numSamples; i++) {
			CA tempCa = new CA(k);
			InteractionGraph ig = new InteractionGraph(t, k, v);
			long numInteraction = ig.getNumInt();
			while (numInteraction > cutOff){
				this.density.setComp(0);
				Integer[] newRandRow = this.density.selectRandRow(ig, k, v);
				this.comp = this.density.getComp();
				
				int coverage = ig.deleteInteractions(newRandRow);
				tempCa.addRow(newRandRow);	
				
				numInteraction = numInteraction - coverage;
				
				// time complexity measurement
				tempCa.addComp(this.comp);
				tempCa.addCoverage(coverage);
			}
			
			int bestCaSize = bestCa.getNumRows();
			int tempCaSize = tempCa.getNumRows();
			
			System.out.println("size of CA: " + tempCaSize + ", i = " + i );
			
			if ((bestCaSize == 0) || (tempCaSize <= bestCaSize)){
				if ((tempCaSize < bestCaSize) || (numInteraction < bestNumInteraction)){
					bestCa = tempCa;
					bestIg = ig;
					bestNumInteraction = numInteraction;
				}
			}
		}
		
		// interaction algorithm part
		while(!bestIg.isEmpty()) {
			this.interaction.setComp(0);
			Integer[] newRandRow = this.interaction.selectRandRow(bestIg, k, v);
			this.comp = this.interaction.getComp();
			
			int coverage = bestIg.deleteInteractions(newRandRow);
			bestCa.addRow(newRandRow);	
			
			bestNumInteraction = bestNumInteraction - coverage;
			
			// time complexity measurement
			bestCa.addComp(this.comp);
			bestCa.addCoverage(coverage);
		}
		
		return bestCa;
	}
	
	private long setCutoff(int t, int k, int v, int factor) {
		double cutOffSq = factor * v * this.choose(k - 1, t - 1) * Math.pow(v, t - 1);
		return Math.round(Math.sqrt(cutOffSq));
	}

	private long choose(int n, int k) {
		assert((n >= 0) && (k >= 0) && (k <= n));
		
		if (k == 0) {
			return 1;
		} else if (n == k) {
			return 1;
		} else {
			return this.choose(n - 1, k - 1) + this.choose(n - 1, k);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

}
