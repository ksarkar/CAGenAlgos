package edu.asu.ca.kaushik.algorithms.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class InteractionGraph {
	private int t;
	private int k;
	private int v;
	private Map<ColGroup, Set<SymTuple>> graph; 
	private long numInt;
	
	private List<ColGroup> allColGroups;
	
	private Random rand;
	
	// time complexity measurement
	private int comp;
	
	public InteractionGraph(InteractionGraph ig) {
		this.t = ig.t;
		this.k = ig.k;
		this.v = ig.v;
		this.rand = ig.rand;
		this.numInt = ig.numInt;
		// need to make a separate copy of the interaction graph
		this.graph = new HashMap<ColGroup, Set<SymTuple>>(ig.graph);
		// no need to make a separate copy of allColGroups, since it will used read-only
		this.allColGroups = ig.allColGroups;
	}

	public InteractionGraph(int t, int k, int v){
		this.t = t;
		this.k = k;
		this.v = v;
		
		this.graph = createFullGraph(t, k, v);
		this.rand = new Random(1234L);
	}
	
	public List<ColGroup> getAllColGroups(){
		return this.allColGroups;
	}
	
	public int getComp() {
		return this.comp;
	}
	
	public long getNumInt() {
		return this.numInt;
	}
	

	private Map<ColGroup, Set<SymTuple>> createFullGraph(int t, int k, int v) {
		this.allColGroups = createAllColGroups(t, k);
		List<SymTuple> symTuples = createAllSymTuples(t, v);
		
		this.numInt = this.allColGroups.size() * symTuples.size();
		
		Map<ColGroup, Set<SymTuple>> graph = new HashMap<ColGroup, Set<SymTuple>>();
		
		for (ColGroup cols : this.allColGroups) {
			graph.put(cols, new HashSet<SymTuple>(symTuples));
		}
		
		return graph;
	}

	public List<SymTuple> createAllSymTuples(int t, int v) {
		if (t == 0){
			List<SymTuple> list = new ArrayList<SymTuple>();
			list.add(new SymTuple(new int[0]));
			return list;
		}
		else {
			List<SymTuple> small = createAllSymTuples(t-1, v);
			
			List<SymTuple> big = new ArrayList<SymTuple>();
			for (SymTuple tuple : small) {
				for (int i = 0; i < v; i++) {
					big.add(new SymTuple(tuple, i));
				}
			}
			return big;
		}
	}

	private List<ColGroup> createAllColGroups(int t, int k) {
		if (t == 0){
			List<ColGroup> list = new ArrayList<ColGroup>();
			list.add(new ColGroup(new int[0]));
			return list;
		}
		else {
			List<ColGroup> small = createAllColGroups(t-1, k);
			
			List<ColGroup> big = new ArrayList<ColGroup>();
			for (ColGroup tuple : small) {
				for (int i = 0; i < k; i++) {
					if (shouldInclude(tuple.getCols(), i)) {
						big.add(new ColGroup(tuple, i));
					}
				}
			}
			return big;
		}
	}
	
	private static boolean shouldInclude(int[] a, int i){
		for (int j : a) {
			if (i <= j)
				return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return this.graph.isEmpty();
	}

	public int deleteInteractions(Integer[] newRandRow) {
		// time complexity measurement
		int coverage = 0;
		
		Iterator<ColGroup>	colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			int[] indices = colGr.getCols();
			
			int[] syms = new int[this.t];
			for (int i = 0; i < this.t; i++) {
				syms[i] = newRandRow[indices[i]].intValue();
			}
			SymTuple tuple = new SymTuple(syms);
			
			Set<SymTuple> set = this.graph.get(colGr);
			if (set.contains(tuple)) {
				// time complexity measurement
				coverage += 1;
				
				set.remove(tuple);
				if (set.isEmpty()) {
					colGrIt.remove();
				}
			}
		}
		
		return coverage;
	}
	
	public int getCoverage(Integer[] newRandRow) {
		int coverage = 0;
		
		Iterator<ColGroup>	colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()) {
			ColGroup colGr = colGrIt.next();
			int[] indices = colGr.getCols();
			
			int[] syms = new int[this.t];
			for (int i = 0; i < this.t; i++) {
				syms[i] = newRandRow[indices[i]].intValue();
			}
			SymTuple tuple = new SymTuple(syms);
			
			Set<SymTuple> set = this.graph.get(colGr);
			if (set.contains(tuple)) {
				coverage += 1;
			}
		}
		
		return coverage;
	}
	
	public Interaction getUniformRandInteraction() {
		assert !this.isEmpty();
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>> set = this.graph.entrySet();

		int M = 0;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			Set<SymTuple> tupleSet = entry.getValue();
			M = M + tupleSet.size();
		}
		
		int chosen = this.rand.nextInt(M);
		
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			ColGroup cols = entry.getKey();
			Set<SymTuple> tupleSet = entry.getValue();
			for (SymTuple tuple : tupleSet){
				if (chosen == 0){
					return new Interaction(cols, tuple);
				}
				else {
					chosen = chosen - 1;
				}
			}
		}
		
		assert false; // control will never reach this statement
		return null;
	}
	
	public Interaction getPropRandInteraction(Integer[] newRow) {
		assert !this.isEmpty();
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>> set = this.graph.entrySet();
		
		List<Integer> N = new ArrayList<Integer>();
		
		int M = 0;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			ColGroup cols = entry.getKey();
			int n = this.getNValue(cols, newRow);
			Set<SymTuple> tupleSet = entry.getValue();
			int numEl = tupleSet.size();
			M = M + (numEl * n);
			for(int i = 0; i < numEl; i++) {
				N.add(new Integer(n));
			}
		}
		
		int randInt = this.rand.nextInt(M);
		
		int i = 0;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set){
			ColGroup cols = entry.getKey();
			Set<SymTuple> tupleSet = entry.getValue();
			for (SymTuple tuple : tupleSet){
				if (randInt < N.get(i).intValue()){
					return new Interaction(cols, tuple);
				}
				else {
					randInt = randInt - N.get(i).intValue();
					i++;
				}
			}
		}
		
		assert false; // control will never reach this statement
		return null;
	}
	
	
	
	private int getNValue(ColGroup cols, Integer[] newRow) {
		int[] row = new int[newRow.length];
		for (int i = 0; i < newRow.length; i++){
			row[i] = newRow[i].intValue();
		}
		
		int[] columns = cols.getCols();
		for (int i : columns){
			row[i] = 0;
		}
		
		int n = 0;
		for (int i : row){
			if (i == this.v){
				n++;
			}
		}
		return (int)Math.pow(this.v, n);
	}
	
	public void deletIncompatibleInteractions(Interaction interaction) {
		ColGroup fixColGr = interaction.getCols();	
		Set<ColGroup> colGrSet = this.graph.keySet();
		Set<ColGroup> intersectColGrs = new HashSet<ColGroup>();
		for (ColGroup colGr : colGrSet){
			if (fixColGr.intersects(colGr)){
				intersectColGrs.add(colGr);
			}
		}
		
		for (ColGroup colGr : intersectColGrs){
			this.delIncompatibleInteractions(colGr, interaction);
		}
	}

	private void delIncompatibleInteractions(ColGroup colGr, Interaction interaction) {
		
		int[] intersectSyms = colGr.getIntersectCols(interaction, this.v);
		
		Set<SymTuple> set = this.graph.remove(colGr);
		Set<SymTuple> newVal = new HashSet<SymTuple>();
		for (SymTuple tuple : set) {
			int[] syms = tuple.getSyms();
			if (!this.incompatible(intersectSyms, syms)){
				newVal.add(tuple);
			}
		}
		
		if (!newVal.isEmpty()){
			this.graph.put(colGr, newVal);
		}
	}

	private boolean incompatible(int[] intersectSyms, int[] syms) {
		for (int i = 0; i < intersectSyms.length; i++){
			if (intersectSyms[i] != this.v){
				if (intersectSyms[i] != syms[i]){
					return true;
				}
			}
		}
		return false;
	}
	
	public void deleteFullyDeterminedInteractions(Integer[] newRow) {
		Iterator<ColGroup> colGrIt = this.graph.keySet().iterator();
		while (colGrIt.hasNext()){
			if (colGrIt.next().isFullyDetermined(newRow, this.v)){
				colGrIt.remove();
			}
		}
	}
	
	public boolean contains(Interaction interaction) {
		ColGroup colGr = interaction.getCols();
		SymTuple tuple = interaction.getSyms();
		
		if (this.graph.containsKey(colGr)){
			Set<SymTuple> tuples = this.graph.get(colGr);
			if (tuples.contains(tuple)){
				return true;
			}
		}
		return false;
	}
	
	public Interaction selectInteraction(Integer[] row) {
		// time complexity measurement
		this.comp = 0;
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>>	entries = this.graph.entrySet();
		
		double maxExpCoverage = 0.0d;
		Interaction bestInteraction = null;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : entries){
			ColGroup colGr = entry.getKey();
			Set<SymTuple> tuples = entry.getValue();
			for (SymTuple tuple : tuples) {
				// time complexity measurement
				this.comp += 1;
				
				Interaction interaction = new Interaction(colGr, tuple);
				double expCoverage = this.computeInteractionCoverage(interaction, row);
				if (expCoverage > maxExpCoverage){
					maxExpCoverage = expCoverage;
					bestInteraction = interaction;
					
				}
			}
		}
		
		// time complexity measurement
		this.comp = this.comp * this.comp;
		
		return bestInteraction;
	}

	private double computeInteractionCoverage(Interaction interaction, Integer[] row) {
		InteractionGraph igCopy = new InteractionGraph(this);
		igCopy.deletIncompatibleInteractions(interaction);
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>>	entries = igCopy.graph.entrySet();
		
		double expCoverage = 0.0d;
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : entries){
			ColGroup colGr = entry.getKey();
			Set<SymTuple> tuples = entry.getValue();
			expCoverage = expCoverage + tuples.size() 
					* calculateNumCoveringRows(row, colGr, interaction.getCols(), igCopy.v);
		}
		
		expCoverage = expCoverage 
				/ calculateNumCoveringRows(row, interaction.getCols(), interaction.getCols(), igCopy.v);
		
		return expCoverage;
	}

	private static double calculateNumCoveringRows(Integer[] row, ColGroup colGr1,
			ColGroup colGr2, int v) {
		Integer[] rowCopy = Arrays.copyOf(row, row.length);
		rowCopy = markCols(rowCopy, colGr1);
		rowCopy = markCols(rowCopy, colGr2);
		
		double exp = 0;
		for (Integer i : rowCopy){
			if (i.intValue() == v){
				exp = exp + 1.0d;
			}
		}
		
		return Math.pow(v, exp);
	}

	private static Integer[] markCols(Integer[] rowCopy, ColGroup colGr) {
		int[] cols = colGr.getCols();
		for (int i : cols){
			rowCopy[i] = new Integer(0);
		}
		return rowCopy;
	}

	@Override
	public String toString() {
		String s =  "InteractionGraph [";
		
		Set<Map.Entry<ColGroup, Set<SymTuple>>> set = this.graph.entrySet();
		for (Map.Entry<ColGroup, Set<SymTuple>> entry : set) {
			s = s + entry.getKey() + " -> " + entry.getValue() + "\n                  ";
		}
		
		return s + "]";
	}

	public static void main(String[] args){
		/*InteractionGraph ig = new InteractionGraph(2,3,2);
		System.out.println(ig);
		int[] row = {0, 2, 2};
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row1 = {0, 0, 0};
		ig.deleteInteractions(newRow(row1));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row2 = {1, 0, 1};
		ig.deleteInteractions(newRow(row2));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row3 = {0, 1, 1};
		ig.deleteInteractions(newRow(row3));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		int[] row4 = {1, 1, 0};
		ig.deleteInteractions(newRow(row4));
		System.out.println(ig);
		System.out.println(ig.getPropRandInteraction(newRow(row)));
		
		System.out.println(ig.isEmpty());
		*/
		
		
		//List<SymTuple> test = ig.createAllSymTuples(3, 2);
		//System.out.print(test.toString());
		
		//List<ColGroup> test1 = ig.createAllColGroups(2, 4);
		//System.out.println(test1);
		
		InteractionGraph ig = new InteractionGraph(4,13,18);
		System.out.println(ig);
		
	}

	private static Integer[] newRow(int[] row) {
		Integer[] a = new Integer[row.length];
		for (int i = 0; i < row.length; i++) {
			a[i] = new Integer(row[i]);
		}
		return a;
	}

}
