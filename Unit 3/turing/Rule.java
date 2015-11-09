package turing;

/**
 * Represents one of the rules of a Turing Machine.
 * The rule applies when the machine's state is equal to currentState and
 * the character in the current cell on the tape is equal to currentContent.
 * The rule says that the machine will change to state newState, will
 * write newContent into the current cell on the tape, and will then move
 * either to the left or to the right on the tape, depending on whether
 * the value of moveLeft is true or false.
 */
public class Rule {
	
	public int currentState;
	public char currentContent;
	public int newState;
	public char newContent;
	public boolean moveLeft;
	
	/**
	 * Create a rule with default values for the instance variables.
	 */
	public Rule() {
	}

	/**
	 * Create a rule with specified values for the instance variables.
	 */
	public Rule(int currentState, char currentContent, int newState, char newContent, boolean moveLeft) {
		this.currentState = currentState;
		this.currentContent = currentContent;
		this.newState = newState;
		this.newContent = newContent;
		this.moveLeft = moveLeft;
	}

}