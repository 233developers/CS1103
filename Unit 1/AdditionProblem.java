package unit1.mathquiz;

import java.util.Random;

public class AdditionProblem {

	private int x,y,answer;
	private Random rGen = new Random();
	
	public AdditionProblem() {
		x = rGen.nextInt(50);
		y = rGen.nextInt(30);
		answer = x + y;
	}
	
	public String getProblem() {
		return "Compute the sum: " + x + " + " + y;
	}
	
	public int getAnswer() {
		return answer;
	}
	
	public String getNextProblem() {
		x = rGen.nextInt(50);
		y = rGen.nextInt(30);
		answer = x + y;
		return getProblem();
	}
}