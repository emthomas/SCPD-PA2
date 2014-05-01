package edu.stanford.cs276;

public class UniformCostModel implements EditCostModel {
	private double uniformProb = 0.1;
	
	public void setUniformProb(double value) {
		this.uniformProb = value;
	}
	
	@Override
	public double editProbability(String original, String R, int distance) {
		if(R.equalsIgnoreCase(original)) {
			return 0.95;
		}
		return Math.pow(uniformProb,distance);
		/*
		 * Your code here
		 */
	}
}
