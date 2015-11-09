package minesweeper;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * This program is a MineSweeper game in which the user starts
 * in the upper left corner tries to get home (the lower right
 * corner) without stepping on a mine.  The game can be ended
 * by clicking the window's close box, in the title bar.
 */
public class MineSweeper {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("MineSweeper");
		MineField board = new MineField();
		window.setContentPane(board);
		window.setJMenuBar( new MineMenus(board) );
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation( (screenSize.width - window.getWidth()) / 2, 
				(screenSize.height - window.getHeight()) / 2 );
		window.setVisible(true);
	}

}
