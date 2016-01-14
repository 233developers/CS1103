package textcollage;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * An object of type DrawText can draw a string in
 * a graphics context, with various effects, such as
 * color, magnification, and rotation  The string
 * is specified in the constructor and cannot be changed.
 * The position and other properties can be changed.
 */
public class DrawTextItem {
	
	private final String string;  // The String that is drawn by this item.
	
	private Font font = null;
	private int x = 0;
	private int y = 0;
	private Color textColor = Color.BLACK;
	private Color background = null;
	private boolean border = false;
	private double rotationAngle = 0;
	private double magnification = 1;
	private double textTransparency = 0;
	private double backgroundTransparency = 0;
	
	/**
	 * Create a DrawTextItem to draw a specified string.  Initially,
	 * the position is set to (0,0) and all properties have their
	 * default values.
	 * @throws NullPointerException if the parameter is null
	 */
	public DrawTextItem(String stringToDraw) {
		this(stringToDraw,0,0);
	}
	
	/**
	 * Create a DrawTextItem to draw a specified string.  Initially,
	 * the position is set to (x,y) and all other properties have their
	 * default values.  Note that x and y give the position of
	 * the center of the string.
	 * @param x the x-coordinate where the string will be drawn.
	 * @param y the y-coordinate where the string will be drawn.
	 * @throws NullPointerException if the parameter is null
	 */
	public DrawTextItem(String stringToDraw, int x, int y) {
		if (stringToDraw == null)
			throw new NullPointerException("String can't be null.");
		string = stringToDraw;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Draw this item's string in the given Graphics context,
	 * applying all of this item's property settings.
	 * @param g the graphics context in which the string will
	 * be drawn.  Note that this is assumed to be actually of
	 * type Graphics2D (which should not be a problem).
	 */
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g.create();
		if (font != null)
			g2.setFont(font);
		FontMetrics fm = g2.getFontMetrics();
		int width = fm.stringWidth(string);
		int height = fm.getAscent() + fm.getDescent();
		g2.translate(x,y);
		if (magnification != 1) {
		    float pixelSize = 1/(float)magnification;
			g2.setStroke( new BasicStroke(pixelSize) ); // magnification won't apply to border
			g2.scale(magnification,magnification);
		}
		if (rotationAngle > 0)
			g2.rotate( -Math.PI * (rotationAngle / 180));
		Color colorToUseForText = textColor;
		if (colorToUseForText == null)
			colorToUseForText = g2.getColor();
		if (background != null) {
			if (backgroundTransparency == 0)
				g2.setColor(background);
			else
				g2.setColor( new Color( background.getRed(), background.getGreen(), background.getBlue(),
						(int)(255*(1-backgroundTransparency))) );
			g2.fillRect(-width/2 - 3, -height/2 - 3, width + 6, height + 6);
		}
		if (textTransparency == 0)
			g2.setColor(colorToUseForText);
		else
			g2.setColor( new Color( colorToUseForText.getRed(), 
					colorToUseForText.getGreen(), colorToUseForText.getBlue(),
					(int)(255*(1-textTransparency)) ) );
		if (border)
			g2.drawRect(-width/2 -3, -height/2 - 3, width + 6, height + 6);
		g2.drawString(string,-width/2, -height/2 + fm.getAscent());
	}

	/**
	 * Returns the string that is drawn by this DrawTextItem.  The
	 * string is set in the constructor and cannot be changed.
	 */
	public String getString() {
		return string;
	}

	/**
	 * Set the background color.  If the value is non-null, then
	 * a rectangle of this color is drawn behind the string.
	 * If the value is null, no background rectangle is drawn.
	 * The default value is null.
	 */
	public void setBackground(Color background) {
		this.background = background;
	}

	/**
	 * Set the level of transparency of the background color, where 0 indicates
	 * no transparency and 1 indicates completely transparent.  If the value
	 * is not 1, then the transparency level of the background color is set to
	 * this value.  If the background color is null, then the value of
	 * backgroundTransparency is ignored.  The default value is zero.
	 * @param backgroundTransparency the background transparency level, in the range 0 to 1
	 * @throws IllegalArgumentException if the parameter is less than 0 or greater than 1
	 */
	public void setBackgroundTransparency(double backgroundTransparency) {
		if (backgroundTransparency < 0 || backgroundTransparency > 1)
			throw new IllegalArgumentException("Transparency must be in the range 0 to 1.");
		this.backgroundTransparency = backgroundTransparency;
	}

	/**
	 * If border is set to true, then a rectangular border is drawn around
	 * the string.  The default value is false.
	 */
	public void setBorder(boolean border) {
		this.border = border;
	}

	/**
	 * Sets the font to be used for drawing the string.  A value of
	 * null indicates that the font that is set for the graphics context
	 * where the string is drawn should be used.  The default value is
	 * null.
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Set a magnification level that is applied when the string is drawn.
	 * A magnification of 2 will double the size; a value of 0.5 will cut
	 * it in half.  Negative values will produce backwards, upside-down text.
	 * Zero is not a legal value, and an attempt to set the magnification to
	 * zero will cause an IllegalArgumentException.  The default value is
	 * 1, Indicating no magnification.
	 */
	public void setMagnification(double magnification) {
		if (magnification == 0)
			throw new IllegalArgumentException("Magnification cannot be 0.");
		this.magnification = magnification;
	}

	/**
	 * Sets the angle of the baseline of the string with respect to the
	 * horizontal.  The angle is specified in degrees.  The default is
	 * 0, which gives normal, unrotated, horizontal text.
	 */
	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	/**
	 * Sets the color to be used for drawing the text (and the border if there is one).
	 * The default value is null, which means that the color that is set in the
	 * graphics context where the string is drawn will be used for drawing the text.
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	/**
	 * Set the level of transparency of the text color, where 0 indicates
	 * no transparency and 1 indicates completely transparent.  If the value
	 * is not 1, then the transparency level of the text color is set to
	 * this value.  This property is also applied to the border, if there is one.
	 * The default value is zero.
	 * @param textTransparency the text transparency level, in the range 0 to 1
	 * @throws IllegalArgumentException if the parameter is less than 0 or greater than 1
	 */
	public void setTextTransparency(double textTransparency) {
		if (textTransparency < 0 || textTransparency > 1)
			throw new IllegalArgumentException("Transparency must be in the range 0 to 1.");
		this.textTransparency = textTransparency;
	}

	/**
	 * Set the x-coordinate where the string is drawn.  This gives the position of
	 * the center of the string.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Set the y-coordinate where the string is drawn.  This gives the position of
	 * the center of the string.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * returns the value of the background color property
	 */
	public Color getBackground() {
		return background;
	}

	/**
	 * returns the value of the background transparency property
	 */
	public double getBackgroundTransparency() {
		return backgroundTransparency;
	}

	/**
	 * returns the value of the border property
	 */
	public boolean getBorder() {
		return border;
	}

	/**
	 * returns the value of the font property
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * returns the value of the magnification property
	 */
	public double getMagnification() {
		return magnification;
	}

	/**
	 * returns the value of the rotation angle property
	 */
	public double getRotationAngle() {
		return rotationAngle;
	}

	/**
	 * returns the value of the text color property
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * returns the value of the text transparency property
	 */
	public double getTextTransparency() {
		return textTransparency;
	}

	/**
	 * returns the x-coord of the center of the string
	 */
	public int getX() {
		return x;
	}

	/**
	 * returns the y-coord of the center of the string
	 */
	public int getY() {
		return y;
	}

}
