package turing;

/**
 * This class represents a Turing Machine Tape.
 * 
 * A Turing machine works on a "tape" that is used for both input and output.
 * The tape is made up of little squares called cells lined up in a horizontal
 * row that stretches, conceptually, off to infinity in both directions. Each
 * cell can hold one character. Initially, the content of a cell is a blank
 * space. One cell on the tape is considered to be the current cell. This is
 * the cell where the machine is located. As a Turing machine computes, it
 * moves back and forth along the tape, and the current cell changes.
 * 
 * @author Ryan Coon
 */
public class Tape {

	private Cell currentCell;  // Pointer to the current cell.
	
	/**
	 * Constructor for Tape class creates a tape initially consisting
	 * of one cell.  The content of the cell is set to a blank space
	 * and the Tape class' currentCell pointer is set to point to
	 * the cell.
	 */
	public Tape() {
		Cell cell = new Cell();
		cell.content = ' ';
		cell.prev = null;
		cell.next = null;
		currentCell = cell;
	}
	
	/**
	 * Returns the pointer that points to the current cell.
	 * 
	 * @return Pointer to the current cell.
	 */
	public Cell getCurrentCell() {
		return currentCell;
	}
	
	/**
	 * Returns the char from the current cell.
	 * 
	 * @return The char from the current cell.
	 */
	public char getContent() {
		return currentCell.content;
	}
	
	/**
	 * Changes the char in the current cell to ch.
	 * 
	 * @param ch Char to change the current cell to contain.
	 */
	public void setContent(char ch) {
		currentCell.content = ch;
	}
	
	/**
	 * Moves the current cell one position to the left along the tape.
	 */
	public void moveLeft() {
		if (currentCell.prev == null) {
			Cell newCell = new Cell();
			newCell.content = ' ';
			newCell.prev = null;
			currentCell.prev = newCell;
			newCell.next = currentCell;
			currentCell = newCell;
		} else {
			currentCell = currentCell.prev;
		}
	}
	
	/**
	 * Moves the current cell one position to the right along the tape.
	 */
	public void moveRight() {
		if (currentCell.next == null) {
			Cell newCell = new Cell();
			newCell.content = ' ';
			newCell.next = null;
			currentCell.next = newCell;
			newCell.prev = currentCell;
			currentCell = newCell;
		} else {
			currentCell = currentCell.next;
		}
	}
	
	/**
	 * Returns a String consisting of the chars from all the cells on the
	 * tape, read left to right.  Trailing blank characters are discarded.
	 * 
	 * @return String of all the chars in the cells of the tape read from
	 *     left to right.
	 */
	public String getTapeContents() {
		
		Cell runner;  // A pointer to traverse the list.
		
		// Start with the runner pointing to the current cell.
		runner = currentCell;
		
		// After the loop, runner should point to the farthest left cell.
		while (runner.prev != null) {
			runner = runner.prev;
		}
		
		StringBuilder sb = new StringBuilder();
		
		while (runner != null) {
			sb.append(runner.content);
			runner = runner.next;
		}
		
		// Returns a string of all the characters in the tape with
		// leading and trailing blank spaces discarded.
		return sb.toString().trim();
	}
}