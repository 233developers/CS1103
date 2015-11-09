package turing;

//A test program for the Tape class that creates a window containing
//a graphical representation of the tape with an arrow pointing to
//the current cell.  There are buttons for moving the current cell to
//the left and to the right.  A text-input box shows the content of
//the current cell.  This box can be edited, and there is a "Set" button
//that copies the contents of the cell (actually just the first character)
//to the current cell.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestTapeGUI extends JPanel {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Test Tape");
		window.setContentPane( new TestTapeGUI("Test") );
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setLocation(100,100);
		window.setVisible(true);
	}
	
	private Tape tape;
	private TapePanel tapePanel;
	private JButton moveLeftButton, moveRightButton, setContentButton;
	private JTextField contentInput;
	
	private class TapePanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int mid = getWidth() / 2;
			g.drawLine(0,30,getWidth(),30);
			g.drawLine(0,60,getWidth(),60);
			g.drawLine(mid,70,mid,110);
			g.drawLine(mid,70,mid+15,85);
			g.drawLine(mid,70,mid-15,85);
			int ct = (mid / 30) + 1;
			for (int i = 0; i <= ct; i++) {
				g.drawLine(mid + 30*i - 15, 30, mid + 30*i - 15, 59);
				g.drawString("" + tape.getContent(), mid + 30*i - 8, 53);
				tape.moveRight();
			}
			for (int i = 0; i <= ct; i++)
				tape.moveLeft();
			for (int i = 1; i <= ct; i++) {
				g.drawLine(mid - 30*i - 15, 30, mid - 30*i - 15, 59);
				tape.moveLeft();
				g.drawString("" + tape.getContent(), mid - 30*i - 8, 53);
			}
			for (int i = 1; i <= ct; i++)
				tape.moveRight();
		}
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == moveLeftButton)
				tape.moveLeft();
			else if (evt.getSource() == moveRightButton)
				tape.moveRight();
			else {
				String content = contentInput.getText();
				if (content.length() == 0)
					tape.setContent(' ');
				else
					tape.setContent(content.charAt(0));
			}
			contentInput.setText("" + tape.getContent());
			contentInput.selectAll();
			contentInput.requestFocus();
			tapePanel.repaint();
		}
	}
	
	public TestTapeGUI(String initialContent) {
		tape = new Tape();
		if (initialContent != null && initialContent.length() > 0) {
			for (int i = 0; i < initialContent.length(); i++) {
				tape.setContent(initialContent.charAt(i));
				tape.moveRight();
			}
			tape.moveLeft();  // move back over last character written
		}
		ButtonListener listener = new ButtonListener();
		tapePanel = new TapePanel();
		tapePanel.setPreferredSize(new Dimension(500,130));
		tapePanel.setFont(new Font("Serif", Font.PLAIN, 24));
		tapePanel.setBackground(new Color(180,180,255));
		tapePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		moveLeftButton = new JButton("Left");
		moveLeftButton.addActionListener(listener);
		moveRightButton = new JButton("Right");
		moveRightButton.addActionListener(listener);
		setContentButton = new JButton("Set");
		setContentButton.addActionListener(listener);
		contentInput = new JTextField(1);
		contentInput.setText("" + tape.getContent());
		contentInput.setFont(new Font("Serif", Font.PLAIN, 18));
		contentInput.addActionListener(listener);
		JPanel bottom = new JPanel();
		bottom.add(moveLeftButton);
		bottom.add(moveRightButton);
		bottom.add(Box.createHorizontalStrut(15));
		bottom.add(new JLabel("Content:"));
		bottom.add(contentInput);
		bottom.add(setContentButton);
		setLayout(new BorderLayout());
		add(tapePanel,BorderLayout.CENTER);
		add(bottom,BorderLayout.SOUTH);
	}
}