package guidemo.guidemo;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A panel that can display a background image, a gradient over the image that changes
 * from almost transparent at the top to almost opaque at the bottom, a multiline text,
 * and a list of small images on top of everything else.  The small images are placed by
 * clicking with the mouse.  The image that is placed is determined by the currentDrawImage
 * property; if this property is null, then clicking an existing image with the mouse will
 * remove that image.  
 */
public class DrawPanel extends JPanel {
	
	private TextItem text = new TextItem(); // The TextItem displayed in this image.
	                                        // It can be retrieved with getTextItem but can't be set.
	
	private Image backgroundImage = null;  // Seven properties that have "get" and "set" methods.
	private Color borderColor = Color.DARK_GRAY;
	private int borderThickness = 3;
	private Color gradientOverlayColor = Color.WHITE;
	private boolean horizontalOverlay = false;
	private BufferedImage currentDrawImage;
	
	private ArrayList<ImageItem> images = new ArrayList<ImageItem>();  // three objects for internal use only

	public DrawPanel() {
		setPreferredSize(new Dimension(800,600));
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(borderColor, borderThickness));
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		addMouseListener( new MouseAdapter() {
			AudioClip clink = Util.getSound("guidemo/resources/sounds/clink.wav");
			AudioClip lase = Util.getSound("guidemo/resources/sounds/lase.wav");
			public void mousePressed(MouseEvent evt) {
				int x = evt.getX();
				int y = evt.getY();
				if (currentDrawImage != null) {
					if (clink != null)
						clink.play();
					images.add( new ImageItem(currentDrawImage, x, y));
					repaint();
				}
				else {
					for (int i = images.size()-1; i >= 0; i--)
						if (images.get(i).contains(x,y)) {
							if (lase != null)
								lase.play();
							images.remove(i);
							repaint();
							break;
						}
				}
			}
		});
	}
	
	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		Graphics2D g2 = (Graphics2D)g1;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (backgroundImage != null)
			g2.drawImage(backgroundImage,0,0,getWidth(),getHeight(),this);
		if (gradientOverlayColor != null) {
			int r = gradientOverlayColor.getRed();
			int b = gradientOverlayColor.getBlue();
			int g = gradientOverlayColor.getGreen();
			Color startColor = new Color(r,g,b,50);
			Color endColor = new Color(r,g,b,200);
			if (horizontalOverlay)
				g2.setPaint(new GradientPaint(0,0,startColor,getWidth(),0,endColor,false));
			else
				g2.setPaint(new GradientPaint(0,0,startColor,0,getHeight(),endColor,false));
			g2.fillRect(0,0,getWidth(),getHeight());
		}
		text.draw(g2, getWidth()/2, getHeight()/2);
		for (ImageItem img : images)
			img.draw(g2);
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
		repaint();
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		setBorder(BorderFactory.createLineBorder(borderColor, borderThickness));
		repaint();
	}

	public int getBorderThickness() {
		return borderThickness;
	}

	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		setBorder(BorderFactory.createLineBorder(borderColor, borderThickness));
		repaint();
	}

	public Color getGradientOverlayColor() {
		return gradientOverlayColor;
	}

	public void setGradientOverlayColor(Color gradientOverlayColor) {
		this.gradientOverlayColor = gradientOverlayColor;
		repaint();
	}

	public boolean isHorizontalOverlay() {
		return horizontalOverlay;
	}

	public void setHorizontalOverlay(boolean horizontalOverlay) {
		this.horizontalOverlay = horizontalOverlay;
		repaint();
	}

	public BufferedImage getCurrentDrawImage() {
		return currentDrawImage;
	}

	public void setCurrentDrawImage(BufferedImage currentDrawImage) {
		this.currentDrawImage = currentDrawImage;
	}

	public TextItem getTextItem() {
		return text;
	}
	
	/**
	 * Create and return a BufferedImage containing the same picture that is
	 * shown in this panel.
	 */
	public BufferedImage copyImage() {
		BufferedImage copy = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics g = copy.createGraphics();
		paintComponent(g);
		g.dispose();
		return copy;
	}
	
	/**
	 * Return this panel to its default state.  (The text will be "Hello World", on a gray
	 * background.)
	 */
	public void clear() {
		text = new TextItem();
		backgroundImage = null;
		setBackground(Color.DARK_GRAY);
		gradientOverlayColor = Color.WHITE;
		horizontalOverlay = false;
		borderThickness = 3;
		setBorderColor(Color.DARK_GRAY);
		images.clear();
		repaint();
	}
	
}
