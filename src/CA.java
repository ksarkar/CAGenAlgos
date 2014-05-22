import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CA {
	private int rowLen;
	private List<Integer[]> ca;
	private List<Integer> comp;
	private List<Integer> coverage;
	
	public CA(int rowLen) {
		this.rowLen = rowLen;
		this.ca = new ArrayList<Integer[]>();
		// time complexity measurement
		this.comp = new ArrayList<Integer>();
		this.coverage = new ArrayList<Integer>();
	}
	
	public boolean addRow(Integer[] newRow) {
		assert(newRow.length == this.rowLen);
		/*if (newRow.length != this.rowLen) {
			return false;
		}*/
		
		this.ca.add(newRow);
		return true;
	}
	
	public int getNumRows(){
		return ca.size();
	}
	
	public Iterator<Integer[]> iterator() {
		return ca.iterator();
	}

	@Override
	public String toString() {
		String s =  this.getNumRows() + "\nCA [";
		for (Integer[] a : this.ca){
			for (Integer i : a){
				s = s + i.toString() + ", ";
			}
			s = s + "\n    ";
		}
		return s + "]";
	}

	public void addComp(int comp) {
		this.comp.add(new Integer(comp));
	}

	public List<Integer> getComp() {
		return this.comp;
	}

	public void addCoverage(int coverage) {
		this.coverage.add(new Integer(coverage));		
	}

	public List<Integer> getCoverage() {
		return this.coverage;
	}


}
