package edu.asu.ca.kaushik.algorithms.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;

public class Helper {
	public static List<SymTuple> createAllSymTuples(int t, int v) {
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
	
	public static List<ColGroup> createAllColGroups(int t, int k) {
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

	private static long[] overlapCountA = null;
	private static int st = 0;
	private static int sk = 0;
	private static int sv = 0;
	
	public static long[] getNewOverlapCountA(int t, int k, int v) {
		long[] a;
		if ((overlapCountA == null) || (st != t) || (sk != k) || (sv != v)) {
			overlapCountA = new long[t];
			for (int i = 0; i < t; i++) {
				overlapCountA[i] = getOverlapCount(t, k, v, i);
			}
		}
		a = Arrays.copyOf(overlapCountA, t);
		return a;
	}

	private static long getOverlapCount(int t, int k, int v, int i) {
		return CombinatoricsUtils.binomialCoefficient(t, i) 
				* CombinatoricsUtils.binomialCoefficient(k - t, t - i)
				* ArithmeticUtils.pow(v, t - i);
	}
	
}
