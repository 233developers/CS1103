package textcollage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * A panel that contains a large drawing area where strings
 * can be drawn.  The strings are represented by objects of
 * type DrawTextItem.  An input box under the panel allows
 * the user to specify what string will be drawn when the
 * user clicks on the drawing area.
 * 
 * @author Ryan Coon
 */
public class DrawTextPanel extends JPanel  {
	
	// Variable to hold the objects drawn in the user's collage.	
	private ArrayList<DrawTextItem> strings = new ArrayList<>();
	
	private Color currentTextColor = Color.BLACK;  // Color applied to new strings.
	
	// Combo boxes for string font options.
	private JComboBox<String> fontChoices;
	private JComboBox<String> fontStyles;
	private JComboBox<String> fontSizes;

	private Canvas canvas;  // the drawing area.
	private JTextField input;  // where the user inputs the string that will be added to the canvas
	private JPanel fontOptions; // Container for multiple combo boxes to adjust font options
	private SimpleFileChooser fileChooser;  // for letting the user select files
	private JMenuBar menuBar; // a menu bar with command that affect this panel
	private MenuHandler menuHandler; // a listener that responds whenever the user selects a menu command
	private JMenuItem undoMenuItem;  // the "Remove Item" command from the edit menu
	
	
	/**
	 * An object of type Canvas is used for the drawing area.
	 * The canvas simply displays all the DrawTextItems that
	 * are stored in the ArrayList, strings.
	 */
	private class Canvas extends JPanel {
		Canvas() {
			setPreferredSize( new Dimension(800,600) );
			setBackground(Color.LIGHT_GRAY);
			setFont( new Font( "Serif", Font.BOLD, 24 ) );  // new Font( "Serif", Font.BOLD, 24 )
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			if (strings != null) {
				for (DrawTextItem item : strings) {
					item.draw(g);
				}
			}
		}
	}
	
	/**
	 * An object of type MenuHandler is registered as the ActionListener
	 * for all the commands in the menu bar.  The MenuHandler object
	 * simply calls doMenuCommand() when the user selects a command
	 * from the menu.
	 */
	private class MenuHandler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			doMenuCommand( evt.getActionCommand());
		}
	}

	/**
	 * Creates a DrawTextPanel.  The panel has a large drawing area,
	 * a text input box where the user can specify a string, and an area
	 * to choose font options.  When the user clicks the drawing area,
	 * the string is added to the drawing area at the point where the
	 * user clicked.
	 */
	public DrawTextPanel() {
		fileChooser = new SimpleFileChooser();
		undoMenuItem = new JMenuItem("Remove Item");
		undoMenuItem.setEnabled(false);
		menuHandler = new MenuHandler();
		setLayout(new BorderLayout(3,3));
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		bottom.add(new JLabel("Text to add: "));
		input = new JTextField("Hello World!", 40);
		bottom.add(input);
		add(bottom, BorderLayout.SOUTH);
		
		fontOptions = new JPanel();
		fontOptions.setLayout(new BoxLayout(fontOptions, BoxLayout.Y_AXIS));
		JPanel titleBar = new JPanel();
		titleBar.setMaximumSize(new Dimension(200, 75));
		JLabel fontTitle = new JLabel("Font Options");
		fontTitle.setFont(new Font("Serif", Font.BOLD, 24));
		titleBar.add(fontTitle);
		fontOptions.add(titleBar);
		
		// Used to center the text in a combo box
		DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
		dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		
		fontChoices = new JComboBox<String>();
		fontChoices.addItem("Serif");
		fontChoices.addItem("SansSerif");
		fontChoices.addItem("Dialog");
		fontChoices.addItem("Monospaced");
		fontChoices.setPreferredSize(new Dimension(100, 25));
		fontChoices.setRenderer(dlcr);
		
		JPanel fontTypeSelection = new JPanel();
		fontTypeSelection.add(new JLabel("Type"));
		fontTypeSelection.add(fontChoices);
		fontTypeSelection.setMaximumSize(new Dimension(150, 50));
		fontOptions.add(fontTypeSelection);
		add(fontOptions, BorderLayout.EAST);
		
		fontStyles = new JComboBox<String>();
		fontStyles.addItem("Plain");
		fontStyles.addItem("Bold");
		fontStyles.addItem("Italic");
		fontStyles.addItem("Bold+Italic");
		fontStyles.setPreferredSize(new Dimension(100, 25));
		fontStyles.setRenderer(dlcr);
		
		JPanel fontStyleSelection = new JPanel();
		fontStyleSelection.add(new JLabel("Style"));
		fontStyleSelection.add(fontStyles);
		fontStyleSelection.setMaximumSize(new Dimension(150, 50));
		fontOptions.add(fontStyleSelection);
		add(fontOptions, BorderLayout.EAST);
		
		fontSizes = new JComboBox<String>();
		fontSizes.addItem("12");
		fontSizes.addItem("16");
		fontSizes.addItem("24");
		fontSizes.addItem("36");
		fontSizes.addItem("48");
		fontSizes.addItem("72");
		fontSizes.setPreferredSize(new Dimension(100, 25));
		fontSizes.setRenderer(dlcr);
		
		JPanel fontSizeSelection = new JPanel();
		fontSizeSelection.add(new JLabel("Size"));
		fontSizeSelection.add(fontSizes);
		fontSizeSelection.setMaximumSize(new Dimension(150, 50));
		fontOptions.add(fontSizeSelection);
		add(fontOptions, BorderLayout.EAST);
		
		canvas.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doMousePress( e );
			}
		});
	}
		
	/**
	 * This method is called when the user clicks the drawing area.
	 * A new string is added to the drawing area.  The center of
	 * the string is at the point where the user clicked.
	 * @param e the mouse event that was generated when the user clicked
	 */
	public void doMousePress( MouseEvent e ) {
		String text = input.getText().trim();
		if (text.length() == 0) {
			input.setText("Hello World!");
			text = "Hello World!";
		}
		DrawTextItem s = new DrawTextItem( text, e.getX(), e.getY() );
		s.setTextColor(currentTextColor);  // Default is null, meaning default color of the canvas (black).
		
//   SOME OTHER OPTIONS THAT CAN BE APPLIED TO TEXT ITEMS:
		
		// Set the font to the values selected in the combo boxes.
		s.setFont( getCurrentlySelectedFont());
		
//		s.setMagnification(3);  // Default is 1, meaning no magnification.
//		s.setBorder(true);  // Default is false, meaning don't draw a border.
//		s.setRotationAngle(25);  // Default is 0, meaning no rotation.
//		s.setTextTransparency(0.3); // Default is 0, meaning text is not at all transparent.
//		s.setBackground(Color.BLUE);  // Default is null, meaning don't draw a background area.
//		s.setBackgroundTransparency(0.7);  // Default is 0, meaning background is not transparent.
		
		strings.add(s);
		undoMenuItem.setEnabled(true);
		canvas.repaint();
	}
	
	/**
	 * Returns a font based on the values selected in the Font
	 * Options combo boxes.
	 * 
	 * @return A new Font created from the combo box values.
	 */
	private Font getCurrentlySelectedFont() {
		
		String fontName = (String)fontChoices.getSelectedItem();
		int fontStyle = fontStyles.getSelectedIndex();
		int fontSize = 24;
		
		try {
			fontSize = Integer.parseInt((String)fontSizes.getSelectedItem());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,
				"Sorry, but an error occurred while trying to select the font size:\n" + e);
		}
		
		Font currentFont = new Font(fontName, fontStyle, fontSize);
		return currentFont;
	}
	
	/**
	 * Returns a menu bar containing commands that affect this panel.  The menu
	 * bar is meant to appear in the same window that contains this panel.
	 */
	public JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			
			String commandKey; // for making keyboard accelerators for menu commands
			if (System.getProperty("mrj.version") == null)
				commandKey = "control ";  // command key for non-Mac OS
			else
				commandKey = "meta ";  // command key for Mac OS
			
			JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);
			JMenuItem saveItem = new JMenuItem("Save...");
			saveItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "N"));
			saveItem.addActionListener(menuHandler);
			fileMenu.add(saveItem);
			JMenuItem openItem = new JMenuItem("Open...");
			openItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "O"));
			openItem.addActionListener(menuHandler);
			fileMenu.add(openItem);
			fileMenu.addSeparator();
			JMenuItem saveImageItem = new JMenuItem("Save Image...");
			saveImageItem.addActionListener(menuHandler);
			fileMenu.add(saveImageItem);
			
			JMenu editMenu = new JMenu("Edit");
			menuBar.add(editMenu);
			undoMenuItem.addActionListener(menuHandler); // undoItem was created in the constructor
			undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "Z"));
			editMenu.add(undoMenuItem);
			editMenu.addSeparator();
			JMenuItem clearItem = new JMenuItem("Clear");
			clearItem.addActionListener(menuHandler);
			editMenu.add(clearItem);
			
			JMenu optionsMenu = new JMenu("Options");
			menuBar.add(optionsMenu);
			JMenuItem colorItem = new JMenuItem("Set Text Color...");
			colorItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "T"));
			colorItem.addActionListener(menuHandler);
			optionsMenu.add(colorItem);
			JMenuItem bgColorItem = new JMenuItem("Set Background Color...");
			bgColorItem.addActionListener(menuHandler);
			optionsMenu.add(bgColorItem);
			
		}
		return menuBar;
	}
	
	/**
	 * Carry out one of the commands from the menu bar.
	 * @param command the text of the menu command.
	 */
	private void doMenuCommand(String command) {
		if (command.equals("Save...")) { // save all the string info to a file
			saveAsTextFile();
		}
		else if (command.equals("Open...")) { // read a previously saved file, and reconstruct the list of strings
			openAsTextFile();  // repaint called at end of openAsTextFile.
		}
		else if (command.equals("Clear")) {  // remove all strings
			strings.clear();
			undoMenuItem.setEnabled(false);
			canvas.repaint();
		}
		else if (command.equals("Remove Item")) { // remove the most recently added string
			strings.remove(strings.size() - 1);
			//undoMenuItem.setEnabled(false);
			canvas.repaint();
		}
		else if (command.equals("Set Text Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Text Color", currentTextColor);
			if (c != null)
				currentTextColor = c;
		}
		else if (command.equals("Set Background Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Background Color", canvas.getBackground());
			if (c != null) {
				canvas.setBackground(c);
				canvas.repaint();
			}
		}
		else if (command.equals("Save Image...")) {  // save a PNG image of the drawing area
			File imageFile = fileChooser.getOutputFile(this, "Select Image File Name", "textimage.png");
			if (imageFile == null)
				return;
			try {
				// Because the image is not available, I will make a new BufferedImage and
				// draw the same data to the BufferedImage as is shown in the panel.
				// A BufferedImage is an image that is stored in memory, not on the screen.
				// There is a convenient method for writing a BufferedImage to a file.
				BufferedImage image = new BufferedImage(canvas.getWidth(),canvas.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.setFont(canvas.getFont());
				canvas.paintComponent(g);  // draws the canvas onto the BufferedImage, not the screen!
				boolean ok = ImageIO.write(image, "PNG", imageFile); // write to the file
				if (ok == false)
					throw new Exception("PNG format not supported (this shouldn't happen!).");
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Sorry, an error occurred while trying to save the image:\n" + e);
			}
		}
	}
	
	/**
     * Save the user's collage to a file in human-readable text format.
     * Files created by this method can be read back into the program
     * using the openAsTextFile() method.
     */
	private void saveAsTextFile() {
		
		File selectedFile = fileChooser.getOutputFile(this, "Select File Name", "collage.txt");
		if (selectedFile == null) {
			return;
		}
			
		PrintWriter out;
		
		try {
			FileWriter stream = new FileWriter(selectedFile);
			out = new PrintWriter(stream);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
				"Sorry, but an error occurred while trying to open the file:\n" + e);
			return;
		}
		
		try {
			out.println("TextCollage 1.0"); // Version number.
			Color bgColor = canvas.getBackground();
			out.println("background " + bgColor.getRed() + " " +
				bgColor.getGreen() + " " + bgColor.getBlue());
			for (DrawTextItem item : strings) {
				out.println();
				out.println("startstring");
				out.println("  text " + item.getString());
				
				Color textColor = item.getTextColor();
				
				out.println("  color " + textColor.getRed() + " " +
					textColor.getGreen() + " " + textColor.getBlue());
				out.println("  coords " + item.getX() + " " + item.getY());
				
				Font textFont = item.getFont();
				String fontName = textFont.getName();
				int fontStyle = textFont.getStyle();
				int fontSize = textFont.getSize();					
				out.println("  font " + fontName + " " + fontStyle + " " + fontSize);
				
				out.println("endstring");
			}
			out.close();
			if (out.checkError()) {
				throw new IOException("Output error");
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
				"Sorry, but an error occurred while trying to write the text:\n" + e);
		}
	}  // end saveAsTextFile
	
    /**
     * Read collage data from a file into the drawing area.  The format
     * of the file must be the same as that used in the saveAsTextFile()
     * method.
     */
	private void openAsTextFile() {
		
		File selectedFile = fileChooser.getInputFile(this, "Select File to be Opened");
		if (selectedFile == null) {
			return;
		}
		
		Scanner scanner;
		
		try {
			Reader stream = new BufferedReader(new FileReader(selectedFile));
			scanner = new Scanner(stream);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
				"Sorry, but an error occurred while trying to open the file:\n" + e);
            return;
		}
		
		try {
			String programName = scanner.next();
			if (!programName.equals("TextCollage")) {
				throw new IOException("File is not a TextCollage data file.");
			}
			double version = scanner.nextDouble();
			if (version > 1.0) {
				throw new IOException("File requires a newer version of TextCollage.");
			}
			
			Color newBgColor = Color.WHITE;
			ArrayList<DrawTextItem> newStrings = new ArrayList<>();
			
			while (scanner.hasNext()) {
				
				String itemName = scanner.next();
				if (itemName.equalsIgnoreCase("background")) {
					int red = scanner.nextInt();
					int green = scanner.nextInt();
					int blue = scanner.nextInt();
					newBgColor = new Color(red, green, blue);
				} else if (itemName.equalsIgnoreCase("startstring")) {
					scanner.next();
					String text = scanner.nextLine();
					DrawTextItem entry = new DrawTextItem(text);
					
					itemName = scanner.next();
					while (! itemName.equalsIgnoreCase("endstring")) {
						if (itemName.equalsIgnoreCase("color")) {
							int r = scanner.nextInt();
							int g = scanner.nextInt();
							int b = scanner.nextInt();
							entry.setTextColor(new Color(r, g, b));
						} else if (itemName.equalsIgnoreCase("coords")) {
							int x = scanner.nextInt();
							int y = scanner.nextInt();
							entry.setX(x);
							entry.setY(y);
						} else if (itemName.equalsIgnoreCase("font")) {
							String fontName = scanner.next();
							int fontStyle = scanner.nextInt();
							int fontSize = scanner.nextInt();
							Font newFont = new Font(fontName, fontStyle, fontSize);
							entry.setFont(newFont);
						} else {
							throw new Exception("Unknown term in input during strings.");
						}
						itemName = scanner.next();
					}  // end while (! "endstring")
					newStrings.add(entry);
				} else {
					throw new Exception("Unknown term in input between background.");
				}
			}  // end while (Scanner.hasNext())
			
			canvas.setBackground(newBgColor);
			strings = newStrings;
			repaint();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
				"Sorry, but an error occurred while trying to read the data:\n" + e);
		} finally {
			if (scanner != null) {
				scanner.close();					
			}
		}
	}
}