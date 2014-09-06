package edu.asu.ca.kaushik.outputformatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.Runner;
import edu.asu.ca.kaushik.algorithms.structures.CA;


public class ComputeCoverageOutputFormatter implements OutputFormatter {
	private List<String> algoNames;
	private String dir = "data\\out\\comp-cov\\hybrid-compute-cutoff\\";
	private String newDirName;
	private int t;
	private int v;

	@Override
	public void setAlgoNames(List<String> algoNames) {
		this.algoNames = algoNames;
	}

	@Override
	public void preprocess(int t, int v) throws IOException {
		this.t = t;
		this.v = v;
		
		this.newDirName = "" + t + "-" + "k" + "-" + v;
		
		File newDir = new File(this.dir + this.newDirName);
		newDir.mkdirs();
	}

	@Override
	public void output(int k, List<CA> results) throws IOException {
		for (int i = 0; i < results.size(); i++){
			this.writeToFile(k, this.algoNames.get(i), results.get(i));
		}

	}

	private void writeToFile(int k, String algoName, CA ca) throws IOException {
		String filename = this.getFileName(k, algoName);
		String header = this.getHeader();
		
		List<Integer> comp = ca.getComp();
		List<Integer> cov = ca.getCoverage();
		int numRows = ca.getNumRows();
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(filename, true));
			
			out.println(header);
			for (int i = 0; i < numRows; i++){
				out.println(this.getRowString((i+1), comp.get(i).toString(), cov.get(i).toString()));
			}
			
		} finally {
			if (out != null){
				out.close();
			}
		}
		
	}
	
	String getRowString(int i, String compute, String cov){
		return "" + i + "," + compute + "," + cov + ",";
	}

	private String getHeader() {
		return "row_number, computation, coverage";
	}

	private String getFileName(int k, String algoName) {
		//return dir + "\\" + this.t + "-" + "k" + "-" + this.v + "\\" + this.t + "-" + k + "-" + this.v + "-" + algoName + ".csv";
		return dir + newDirName + "\\" +  this.t + "-" + k + "-" + this.v + "-" + algoName + ".csv";
	}
	
	public static void main(String[] args) throws IOException {
		int t = 6;
		int v = 3;
		
		int k1 = 7;
		int k2 = 17;
		
		List<String> algoList = new ArrayList<String>();
		
		algoList.add("CondExpEntries");
		algoList.add("Hybrid");
		//algoList.add("CondExpIteration");
		
		OutputFormatter formatter = new ComputeCoverageOutputFormatter();
		
		Runner runner = new Runner(formatter);
		runner.setParam(t, v, k1, k2);
		runner.setAlgo(algoList);
		runner.run();
	}

}
