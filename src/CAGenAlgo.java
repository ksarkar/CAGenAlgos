
public interface CAGenAlgo {
	
	/*
	 * Interface for strategy pattern implementation.
	 */
	
	public void setNumSamples(int numSamples);
	public CA generateCA(int t, int k, int v);
	public String getName();

}
