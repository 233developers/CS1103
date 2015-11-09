package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class MineField extends JPanel {
	
	private final static int ROWS = 20;  // number of rows on the board
	private final static int COLS = 20;  // number of columns on the board
	
	private final static Color LIGHT_GREEN = new Color(180,255,180);  // color for unvisited squares
	
	private final static int STATE_UNVISITED = 0;  // constant for use in state array
	private final static int STATE_FLAGGED = 1;    // constant for use in state array
	private final static int STATE_VISITED = 2;    // constant for use in state array
	
	private int[][] state;  // This array holds one of the values STATE_UNVISITED, STATE_FLAGGED,
	                        // or STATE_VISITED for each sqaure on the board.  The initial value
	                        // is STATE_UNVISITED.  If the user drops a "flag" on the square, indicating
	                        // that the user thinks there is a bomb there, the state is changed to
	                        // STATE_FLAGGED.  When the user visits a square by clicking on it (and
	                        // doesn't get blown up), the state is changed to STATE_VISITED.
	                        // THIS VERY IMPORTANT ARRAY IS USED THROUGHOUT THIS CLASS.
	
	private boolean[][] mined; // mined[r][c] is true if there is a mine in square (r,c)
	
	private int mineCount;     // number of mines on the board, set when game is started
	
	private boolean gameInProgress;  // set to true while a game is in progress
	
	private boolean userWon;  // when gameInprogress == false, this tells whether user won
	
	/**
	 * Create a board that has 50 mines scattered in it at random positions,
	 * and start the first game.  This constructor just calls {@link #MineField(int)}
	 * with a parameter value of 50.
	 */
	public MineField() {
		this(50);
	}
	
	/**
	 * Create a board that has a specified number of mines scattered in it at 
	 * random positions, and start the first game.
	 * @param mineCount the number of mines that are to be placed on the board.
	 *   The value should be "reasonable" (not too large or too small), but no
	 *   error checking is done.  Values that are too large or too small might
	 *   cause infinite loops as the program tries to find a legal board with
	 *   the specified number of mines.
	 */
	public MineField(int mineCount) {
		setPreferredSize( new Dimension(2 + 24*COLS, 2 + 24*ROWS ));
		startGame(mineCount);
		addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				doMouseClick(evt);
			}
		});
	}
	
	/**
	 * Returns the number of mines on the board.  This number is set when a
	 * game begins, and does not change until a new game is started.
	 */
	public int getMineCount() {
		return mineCount;
	}
	
	/**
	 * This method processes the event that occurs when the user clicks the board.
	 */
	private void doMouseClick(MouseEvent evt) {
		int row = (evt.getY() - 1) / 24;  // row where the user clicked
		int col = (evt.getX() - 1) / 24;  // column where the user clicked
		if ( row < 0 || row >= ROWS || col < 0 || col >= COLS)
			return;  // ?? click is not actually on the board.
		if (state[row][col] == STATE_VISITED)
			return;  // The user has already visited the square, so click has no effect.
		if (! hasVisitedNeighbor(row,col))
			return;  // The user can only move horizontally or vertically from an already visited square
		if (evt.isMetaDown() || evt.isShiftDown()) {
			   // When the user right-clicks or shift-clicks, it means that the user wants
			   // to flag the square as containing a bomb, or, if the square is already flagged,
			   // to remove the flag.  (Note that the user might be wrong!)
			if (state[row][col] == STATE_UNVISITED)
				state[row][col] = STATE_FLAGGED;
			else // The state must be STATE_FLAGGED, since STATE_VISITED has already been ruled out.
				state[row][col] = STATE_UNVISITED;
		}
		else {
			if (state[row][col] == STATE_FLAGGED)
				return; // The user is protected from accidently visiting a flagged square.
			visit(row, col);  // Mark the square (and maybe some other squares) as visited.
		}
		repaint();  // Redraw the board to show its changed status.
	}
	
	/**
	 * Draw the board, based on the current state of all the squares and on whether or
	 * not the game is in progress.  This method is called by the system and is not meant
	 * to be called directly.
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int r = 0; r < ROWS; r++)
			for (int c = 0; c < COLS; c++) {
				if (mined[r][c] && !gameInProgress)
					g.setColor(Color.RED);
				else if (state[r][c] == STATE_FLAGGED)
					g.setColor(Color.PINK);
				else
					g.setColor(LIGHT_GREEN);
				if (state[r][c] == STATE_VISITED)
					g.fill3DRect( 2+24*c, 2+24*r, 23, 23, false );
				else
					g.fill3DRect( 2+24*c, 2+24*r, 23, 23, true );
				g.setColor(Color.BLACK);
				if (state[r][c] == STATE_FLAGGED)
					g.drawString("B", 6+24*c, 15+24*r);
				else if (state[r][c] == STATE_VISITED) {
					int bombs = bombCount(r,c);
					if (bombs > 0)
						g.drawString("" + bombs, 6+24*c, 15+24*r);
				}
			}
		if (!gameInProgress) {
			g.setColor(Color.BLUE);
			g.setFont(new Font("SERIF", Font.BOLD, 36));
			g.drawString("Game Over.", 30, 60);
			if (userWon)
				g.drawString("YOU WIN!", 30, 120);
			else
				g.drawString("YOU LOSE!", 30, 120);
		}
	}

	/**
	 * Begin a new game with a specified number of mines.  If a game is
	 * in progress, that game is aborted and no warning or error message is
	 * given.
	 * @param mineCount The number of mines to place on the board.  The value
	 *   should be "reasonable"; see the comments on the constructor {@link #MineField(int)}.
	 */
	public void startGame(int mineCount) {
		System.out.println("Start a game with " + mineCount + " mines.");
		this.mineCount = mineCount;
		while (true) { // This loop ends when a valid board is created.
			gameInProgress = true;
			mined = new boolean[ROWS][COLS]; // All values are initially false.
			state = new int[ROWS][COLS];     // All values are initially STATE_UNVISITED.
			for (int i = 0; i < mineCount; i++) { // Place a mine at a random position.
				int r,c;
				while (true) {
					r = (int)(ROWS * Math.random());  // randomly selected row number
					c = (int)(COLS * Math.random());  // randomly selected column number
					if ( (r + c > 2) &&  (r < ROWS-1 || c < COLS-1) && ! mined[r][c] ) {
						   // End the loop if the randomly selected position is OK, otherwise,
						   // try again with a different position.  The test "r + c > 2" ensures
						   // that positions (0,0), (1,0), (0,1), (1,1), (0,2), and (2,0) are
						   // not mined, and in particular that the upper-left corner is not
						   // mined and has no neighbors that are mined.  The other two tests
						   // ensure that the lower-right "home" position is not mined and
						   // that there is not already a mine in the selected square.
						break;
					}
				}
				mined[r][c] = true;
			}
			visit(0,0);  // Get the user started by automatically visiting the upper-left square.
			if (configOK()) // Checks whether the board is valid.
				break;
		}
		repaint();
	}
	
	/**
	 * The user can only visit squares that are next to already visited (or flagged)
	 * squares.  This method checks that a given square is in fact next to a visited square.
	 */
	private boolean hasVisitedNeighbor(int row, int col) {
		if (row > 0 && state[row-1][col] > STATE_UNVISITED)
			return true;
		if (row < ROWS-1 && state[row+1][col] > STATE_UNVISITED)
			return true;
		if (col > 0 && state[row][col-1] > STATE_UNVISITED)
			return true;
		if (col < COLS-1 && state[row][col+1] > STATE_UNVISITED)
			return true;
		return false;
	}
	
	/**
	 * This method is called when a square is visited by the user.
	 */
	private void visit( int row, int col ) {
		if (mined[row][col]) {  // The user has stepped on a mine and gets blown up.
			gameInProgress = false;
			userWon = false;
		}
		else { // It's OK for the user to step on this square.  Mark it as visited.
			mark(row,col);
			if (state[ROWS - 1][COLS -1] == STATE_VISITED) { // User has reached the home square!
				gameInProgress = false;
				userWon = true;
			}
		}
	}
	
	/**
	 * Counts the bombs in the squares that neighbor position (row,col).
	 */
	private int bombCount(int row, int col) {
		int ct = 0;
		if (row > 0) {
			if (col > 0 && mined[row-1][col-1])
				ct++;
			if (mined[row-1][col])
				ct++;
			if (col < COLS-1 && mined[row-1][col+1])
				ct++;
		}
		if (col > 0 && mined[row][col-1])
			ct++;
		if (col < COLS-1 && mined[row][col+1])
			ct++;
		if (row < ROWS-1) {
			if (col > 0 && mined[row+1][col-1])
				ct++;
			if (mined[row+1][col])
				ct++;
			if (col < COLS-1 && mined[row+1][col+1])
				ct++;
		}
		return ct;
	}
	
	/**
	 * Tests whether a new game board is valid.  This is called by startGame() to
	 * decide whether to use a random board that it has created, or to discard
	 * the board and try again.
	 */
	private boolean configOK() {
		if ( state[ROWS-1][COLS-1] == STATE_VISITED ) {
			System.out.println("Already solved.");
			return false;
		}
		return true;
	}
	

	/**
	 * Marks the square in position (row, col) as visited.
	 */
	private void mark(int row, int col) {
		if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
			return;
		}
		if (state[row][col] == STATE_VISITED) {
			return;
		}
		
		state[row][col] = STATE_VISITED;
		
		if (bombCount(row, col) > 0) {
			return;
		} else {
		    mark(row - 1, col);
		    mark(row + 1, col);
	    	mark(row, col - 1);
	    	mark(row, col + 1);
	    	mark(row - 1, col - 1);
	    	mark(row - 1, col + 1);
	    	mark(row + 1, col - 1);
	    	mark(row + 1, col + 1);
		}
	}
}