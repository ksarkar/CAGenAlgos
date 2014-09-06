package edu.asu.ca.kaushik.algorithms.structures;


public class Interaction {
	ColGroup cols;
	SymTuple syms;
	public Interaction(ColGroup cols, SymTuple syms) {
		super();
		this.cols = cols;
		this.syms = syms;
	}
	public ColGroup getCols() {
		return cols;
	}
	public void setCols(ColGroup cols) {
		this.cols = cols;
	}
	public SymTuple getSyms() {
		return syms;
	}
	public void setSyms(SymTuple syms) {
		this.syms = syms;
	}
	@Override
	public String toString() {
		return "Interaction [cols=" + cols + ", syms=" + syms + "]";
	}
	
	

}
