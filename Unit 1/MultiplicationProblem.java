package unit1.mathquiz;

import java.util.Random;

public class MultiplicationProblem {

	private int x,y,answer;
	private Random rGen = new Random();
	
	public MultiplicationProblem() {
		x = rGen.nextInt(11);
		y = rGen.nextInt(11);
		answer = x * y;
	}
	
	public String getProblem() {
		return "Compute the product: " + x + " * " + y;
	}
	
	public int getAnswer() {
		return answer;
	}
	
	public String getNextProblem() {
		x = rGen.nextInt(11);
		y = rGen.nextInt(11);
		answer = x * y;
		return getProblem();
	}
}