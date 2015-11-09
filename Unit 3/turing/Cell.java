package turing;

/**
 * Represents one cell on a Turing Machine tape.
 */
public class Cell {
	
	public char content;  // The character in this cell.
	public Cell next;     // Pointer to the cell to the right of this one.
	public Cell prev;     // Pointer to the cell to the left of this one.
	
}