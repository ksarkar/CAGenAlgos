
public class CAGeneration {
	
	private CAGenAlgo algo;
	
	public CAGeneration() {
	}

	public CAGeneration(CAGenAlgo a) {
		this.algo = a;
	}
	
	public void setCAGenAlgo(CAGenAlgo a) {
		this.algo = a;
	}
	
	public void setNumSamples(int numSamples) {
		this.algo.setNumSamples(numSamples);
	}
	
	public CA generateCA(int t, int k, int v) {
		return this.algo.generateCA(t, k, v);
	}
	
	private String getAlgoName() {
		return this.algo.getName();
	}	
	
	public static void main(String args[]) {
		CAGeneration c = new CAGeneration();
		
		// UniformRandom, PropRand, RepeatedRand, 
		// CondExpEntries, CondExpOneInteraction, CondExpIteration, Hybrid, DensityMultCandidateRow
		c.setCAGenAlgo(new DensityMultCandidateRow());
		c.setNumSamples(100);
		int k = 4;
		CA ca = c.generateCA(2, k, 3);
		System.out.println(ca.getNumRows());
		System.out.println(c.getAlgoName());
		//System.out.println(ca.getComp());
		//System.out.println(ca.getCoverage());
	}

}
