package turing;

import java.util.ArrayList;

/**
 * This class represents Turing machines.  A Turing machine is a simple
 * computing device that moves back and forth along a Tape
 * (see {@link Tape}), reading and writing characters.  The machine
 * has a state, which is represented as an integer.  It also has
 * a program, which consists of a list of rules (@see {@link Rule}).
 */
public class TuringMachine {
	
	private ArrayList<Rule> rules = new ArrayList<Rule>();  // This machine's program.
	
	/**
	 * Adds one rule to the machine's program.
	 * @param rule A non-null rule to be added to the program.  A null
	 * value will not cause an immediate error, but will produce NullPointExceptions
	 * when run() method is called.
	 */
	public void addRule(Rule rule) {
		rules.add(rule);
	}
	
	/**
	 * A convenience method for adding an entire array of rules to this machine's program.
	 * This method simply calls {@link #addRule(Rule)} for each rule.
	 * @param rules  The array of rules that are to be added to the program.
	 * Each rule should be non-null.
	 */
	public void addRules(Rule[] rules) {
		for (Rule rule : rules)
			addRule(rule);
	}
	
	/**
	 * Run this Turing machine on a given Tape.  The machine starts in state
	 * zero and continues as long as the state is greater than or equal to
	 * zero.  At each step of the computation, it finds the applicable rule
	 * in its program (the one whose currentState variable matches the machine's
	 * current state and whose currentContent value matches the content of
	 * the current Cell on the tape), and it executes the action part of the
	 * rule (by setting the state of the machine to the rule's newState
	 * variable, and setting the content of the current Cell to the rule's
	 * newContent variable, and moving either left or right, depending on the
	 * value of the rule's moveLeft variable.  (See {@link Rule}.)
	 * Note that it is possible for this method to run forever, if the
	 * state of the machine never becomes negative.
	 * @param tape  The tape on which this machine will run.  The position of
	 * the current cell on the tape and the content of all the cells on the 
	 * tape when this method is called constitute the input to the machine's
	 * computation.
	 * @return the content of the tape at the end of the computation.  The return
	 * value is obtained by calling {@link Tape#getTapeContents()}.
	 * @throws IllegalStateException if at any point during the computation,
	 * no applicable rule can be found.  This is taken to indicate a bug in the
	 * machine's program.
	 */
	public String run(Tape tape) throws IllegalStateException {
		int currentState = 0;
		while (currentState >= 0) {
			char currentContent = tape.getContent();
			Rule applicableRule = null;
			for (Rule rule : rules) {
				if (rule.currentContent == currentContent && rule.currentState == currentState) {
					applicableRule = rule;
					break;
				}
			}
			if (applicableRule == null)
				throw new IllegalStateException("Cannot find an applicable rule; tape contents = " 
						+ tape.getTapeContents());
			currentState = applicableRule.newState;
			tape.setContent(applicableRule.newContent);
			if (applicableRule.moveLeft)
				tape.moveLeft();
			else
				tape.moveRight();
//			System.out.println(applicableRule.currentState + " "+ applicableRule.currentContent 
//					+ " " +applicableRule.newState + " " +applicableRule.newContent 
//					+ " " +applicableRule.moveLeft);  // for testing.
		}
		return tape.getTapeContents();
	}	
}