package edu.asu.ca.kaushik.outputformatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.asu.ca.kaushik.Runner;
import edu.asu.ca.kaushik.algorithms.structures.CA;


public class TableOutputFormatter implements OutputFormatter {
	
	private List<String> algoNames;
	String dirName = "data\\out\\";
	String filename = null;
	
	
	@Override
	public void setAlgoNames(List<String> algoNames) {
		this.algoNames = algoNames;
	}

	@Override
	public void preprocess(int t, int v) throws IOException {
		File newDir = new File(this.dirName);
		newDir.mkdirs();
		
		this.writeHeader(t, v);
	}
	
	private void writeHeader(int t, int v) throws IOException {
		String fileName = this.getFileName(t, v);
		String header = this.getHeader();
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, true));
			out.println(header);
		} finally {
			if (out != null){
				out.close();
			}
		}
		
	}
	
	private String getFileName(int t, int v) {
		this.filename = this.dirName + t + "-k-" + v;
		return this.filename;
	}
	
	private String getFileName() {
		return this.filename;
	}

	private String getHeader() {
		String header = "k";
		for (int i = 0; i < this.algoNames.size(); i++){
			header = header + ", " + this.algoNames.get(i);
		}
		return header;
	}

	@Override
	public void output(int k, List<CA> results) throws IOException {
		this.writeToFile(k, results);

	}
	
	private void writeToFile(int k, List<CA> results) throws IOException {
		String fileName = this.getFileName();
		String row = this.getRowString(k, results);
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileName, true));
			out.println(row);
		} finally {
			if (out != null){
				out.close();
			}
		}
	}


	private String getRowString(int k, List<CA> results) {
		String row = k + "";
		for (CA ca : results){
			row = row + ", " + ca.getNumRows();
		}
		return row;
	}
	
	public static void main(String[] args) throws IOException {		
		//List<String> algoList = getAllDetAlgos();
		
		List<String> algoList = new ArrayList<String>();
		
		//algoList.add("CondExpEntries");
		//algoList.add("DensityMultCandidateRow");
		//algoList.add("HybridMultiCandidate");
		algoList.add("BestOfKDensityMultiCandidateHybrid");
		//algoList.add("BestOfKDensityHybrid");
		//algoList.add("Hybrid");
		//algoList.add("CondExpIteration");
		
		OutputFormatter formatter = new TableOutputFormatter();
		
		Runner runner = new Runner(formatter);
		runner.setAlgo(algoList);
		
		//runner.setParam(4, 3, 5, 15);
		//runner.run();
		
		//runner.setParam(5, 3, 6, 10);
		//runner.run();
		
		runner.setParam(6, 3, 15, 16);
		runner.run();
	}

}
