package unit1.mathquiz;

import java.util.Random;

public class DivisionProblem {

	private int x,y,answer;
	private Random rGen = new Random();
	
	public DivisionProblem() {
		y = rGen.nextInt(10) + 1;
		answer = rGen.nextInt(10) + 1;
		x = y * answer;
	}
	
	public String getProblem() {
		return "Compute the quotient: " + x + " / " + y;
	}
	
	public int getAnswer() {
		return answer;
	}
	
	public String getNextProblem() {
		y = rGen.nextInt(10) + 1;
		answer = rGen.nextInt(10) + 1;
		x = y * answer;
		return getProblem();
	}
}