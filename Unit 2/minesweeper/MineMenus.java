package minesweeper;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

/**
 * Represents a menu bar to be used with the MineSweeper game.
 */
public class MineMenus extends JMenuBar {
	
	private MineField mineField;
	
	/**
	 * Create a menu bar containing one menu that has commands for starting
	 * new games of MineSweeper, with varying numbers of mines. The
	 * first command in the menus is "New Game", which starts a game using
	 * the same number of mines as the current game.
	 * @param board The board on which the games are played.  When a New Game
	 *    command is selected, a new game is started on this board.
	 */
	public MineMenus( MineField board ) {
		mineField = board;
		JMenu menu = new JMenu("Game");
		add(menu);
		setupGameMenu(menu);
	}	
	
	/**
	 * Adds the various New Game commands to the menu.
	 */
	private void setupGameMenu(JMenu menu) {
		
		AbstractAction newGameAction =  new AbstractAction("New Game") {
			public void actionPerformed(ActionEvent evt) {
				int currentMineCount = mineField.getMineCount();
				mineField.startGame(currentMineCount);
			}
		};
		if (System.getProperty("mrj.version") == null)  // test whether this is Mac OS
			newGameAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
		else
			newGameAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta N"));
		menu.add(newGameAction);
		
		menu.addSeparator();
		
		menu.add( new AbstractAction("New Game / 30 Mines"){
			public void actionPerformed(ActionEvent evt) {
				mineField.startGame(30);
			}
		}); 
		
		menu.add( new AbstractAction("New Game / 40 Mines"){
			public void actionPerformed(ActionEvent evt) {
				mineField.startGame(40);
			}
		}); 
		
		menu.add( new AbstractAction("New Game / 50 Mines"){
			public void actionPerformed(ActionEvent evt) {
				mineField.startGame(50);
			}
		}); 
		
		menu.add( new AbstractAction("New Game / 60 Mines"){
			public void actionPerformed(ActionEvent evt) {
				mineField.startGame(60);
			}
		}); 
		
		menu.add( new AbstractAction("New Game / 75 Mines"){
			public void actionPerformed(ActionEvent evt) {
				mineField.startGame(75);
			}
		}); 
		
		menu.add( new AbstractAction("New Game / 100 Mines"){
			public void actionPerformed(ActionEvent evt) {
				mineField.startGame(100);
			}
		}); 
		
	}

}