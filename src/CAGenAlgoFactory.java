
public class CAGenAlgoFactory {

	public CAGenAlgo getAlgo(String s) {
		CAGenAlgo algo = null;
		
		if (s.equals("CondExpEntries")){
			algo = new CondExpEntries();
		} else if (s.equals("CondExpIteration")){
			algo = new CondExpIteration();
		} else if (s.equals("CondExpOneInteraction")){
			algo = new CondExpOneInteraction();
		} else if (s.equals("Hybrid")){
			algo = new Hybrid();
		} else if (s.equals("BestOfKDensityHybrid")){
			algo = new BestOfKDensityHybrid();
		} else if (s.equals("DensityMultCandidateRow")){
			algo = new DensityMultCandidateRow();
		} else if (s.equals("HybridMultiCandidate")){
			algo = new HybridMultiCandidate();
		} else if (s.equals("BestOfKDensityMultiCandidateHybrid")){
			algo = new BestOfKDensityMultiCandidateHybrid();
		} else {
			System.err.println("Algo name string does not match any existing algo");
		}
		
		return algo;
	}

}
