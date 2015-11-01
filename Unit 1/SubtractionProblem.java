package unit1.mathquiz;

import java.util.Random;

public class SubtractionProblem {

	private int x,y,answer;
	private Random rGen = new Random();
	
	public SubtractionProblem() {
		x = rGen.nextInt(50) + 1;
		y = rGen.nextInt(x);
		answer = x - y;
	}
	
	public String getProblem() {
		return "Compute the difference: " + x + " - " + y;
	}
	
	public int getAnswer() {
		return answer;
	}
	
	public String getNextProblem() {
		x = rGen.nextInt(50) + 1;
		y = rGen.nextInt(x);
		answer = x - y;
		return getProblem();
	}
}