package guidemo.guidemo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Contains a set of Actions that can be used to select images that can
 * be added to a DrawPanel (in the form of ImageItems).  Can create a
 * toolbar containing a button for each Action in the set.  A button
 * shows an ImageIcon with the image that is selected by that button.
 * Clicking one of the buttons also sets the cursor in the DrawPanel
 * to be a (rough) copy of the image.
 */
public class IconSupport {
	
	private DrawPanel panel;
	private ArrayList<BufferedImage> iconImages = new ArrayList<BufferedImage>();
	private ArrayList<Action> actions = new ArrayList<Action>();

	public IconSupport(DrawPanel owner) {
		panel = owner;
		String[] iconNames = {"bell", "camera", "flower", "star", "check", "crossout", 
				 "tux", "bomb", "keyboard","lightbulb", "tv"};
		for (String name : iconNames) {
			BufferedImage img = Util.getBufferedImageResource("guidemo/resources/icons/" + name + ".png");
			if (img != null) {
				iconImages.add(img);
				actions.add(new SelectIconAction(name,iconImages.size()-1));
			}
		}
		actions.add(new NoIconAction());
	}
	
	/**
	 * Return a toolbar containing buttons representing the images that can be added
	 * to the DrawPanel.
	 * @param horizontal  a value of JToolBar.HORIZONTAL or JToolBar.VERTICAL tells
	 * whether the toolbar is meant to have horizontal or vertical orientation.
	 */
	public JToolBar createToolbar(boolean horizontal) {
		JToolBar tbar = new JToolBar( horizontal? JToolBar.HORIZONTAL : JToolBar.VERTICAL);
		for (int i = 0; i < actions.size() - 1; i++)
			tbar.add(actions.get(i));
		tbar.addSeparator(new Dimension(15,0));
		tbar.add(actions.get(actions.size()-1));
		return tbar;
	}
	
	/**
	 * Return a menu containing actions representing the stamps that can be added
	 * to the DrawPanel.
	 */
	public JMenu createMenu() {
		
		JMenu stamper = new JMenu("Stamper");
		for (int i = 0; i < actions.size() - 1; i++) {
			stamper.add(actions.get(i));
		}
		stamper.addSeparator();
		stamper.add(actions.get(actions.size() - 1));
		return stamper;
	}
	
	private class NoIconAction extends AbstractAction {
		NoIconAction() {
			super("Eraser");
			BufferedImage del = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
			Graphics g = del.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0,0,32,32);
			g.setColor(Color.RED);
			g.drawString("DEL",5,20);
			g.dispose();
			putValue(Action.SMALL_ICON, new ImageIcon(del));
			putValue(Action.SHORT_DESCRIPTION, "Use Mouse to Erase Icons"); // tooltip			
		}
		public void actionPerformed(ActionEvent evt) {
			panel.setCurrentDrawImage(null);
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
	
	private class SelectIconAction extends AbstractAction {
		int iconNumber;
		SelectIconAction(String name, int n) {
			   // Note: The name is surpressed in toolbars, but not in menus.
			super(name,new ImageIcon(iconImages.get(n)));
			iconNumber = n;
			putValue(Action.SHORT_DESCRIPTION, "Use Mouse to Stamp this Icon"); // tooltip
		}
		public void actionPerformed(ActionEvent evt) {
			BufferedImage image = iconImages.get(iconNumber);
			panel.setCurrentDrawImage(image);
			Cursor c = Util.createImageCursor(image, image.getWidth()/2, image.getHeight()/2);
			panel.setCursor(c);
		}
	}
}