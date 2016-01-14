package guidemo.guidemo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a multi-line text, with various properties that can be
 * set.  A draw() method is included that will draw the text in a
 * graphics context, centered at a specified point.
 */
public class TextItem {
	
	public final static int CENTER = 0;  // Constants for use with setJustify()
	public final static int LEFT = 1;
	public final static int RIGHT = 2;

	private String text = "Hello\nWorld"; // the displayed text, with '\n' indicating line breaks.
	private Color color = Color.BLACK;
	private double lineHeightMultiplier = 2;
	private boolean bold = true;
	private boolean italic;
	private int fontSize = 30;
	private String fontName = "Serif";
	private int justify = LEFT;

	private String[] lines = { "Hello", "World" }; // same as text, but broken into individual lines.
	
	public void draw(Graphics g, int centerX, int centerY) {
		Color saveColor = g.getColor();
		Font saveFont = g.getFont();
		int style;
		if (italic && bold)
			style = Font.BOLD | Font.ITALIC;
		else if (italic)
			style = Font.ITALIC;
		else if (bold)
			style = Font.BOLD;
		else
			style = Font.PLAIN;
		Font font = new Font(fontName, style, fontSize);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		double lineHeight = fm.getHeight() * lineHeightMultiplier;
		int totalHeight = (int)(lineHeight*(lines.length-1)) + fm.getAscent() + fm.getDescent();
		if (color != null)
			g.setColor(color);
		int[] widths = new int[lines.length];
		int totalWidth = 0;
		for (int i = 0; i < lines.length; i++) {
			widths[i] = fm.stringWidth(lines[i]);
			if (widths[i] > totalWidth)
				totalWidth = widths[i];
		}
		for (int i = 0; i < lines.length; i++) {
			int x;
			if (justify == CENTER)
				x = centerX - widths[i]/2;
			else if (justify == LEFT)
				x = centerX - totalWidth/2;
			else
				x = centerX + totalWidth/2 - fm.stringWidth(lines[i]);
			int y = centerY - totalHeight/2 + fm.getAscent() + (int)(i*lineHeight);
			g.drawString(lines[i],x,y);
		}
		g.setColor(saveColor);
		g.setFont(saveFont);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String newText) {
		Scanner reader = new Scanner(newText);
		ArrayList<String> s = new ArrayList<String>();
		while (reader.hasNextLine()) {
			s.add(reader.nextLine());
		}
		while (s.size() > 0 && s.get(0).trim().length() == 0)
			s.remove(0);  // remove blank lines from front
		while (s.size() > 0 && s.get(s.size()-1).trim().length() == 0)
			s.remove(s.size()-1);  // remove blank lines from end
		if (s.size() == 0) {
			reader.close();
			throw new IllegalArgumentException("Text can't be empty.");
		}
		lines = new String[s.size()];
		for (int i = 0; i < lines.length; i++)
			lines[i] = s.get(i);
		text = newText;
		reader.close();
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public double getLineHeightMultiplier() {
		return lineHeightMultiplier;
	}
	
	public void setLineHeightMultiplier(double lineHeightMultiplier) {
		if (lineHeightMultiplier < 0)
			throw new IllegalArgumentException("Line height multiplier cannot be negative.");
		this.lineHeightMultiplier = lineHeightMultiplier;
	}
	
	public boolean isBold() {
		return bold;
	}
	
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	public boolean isItalic() {
		return italic;
	}
	
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int fontSize) {
		if (fontSize <= 0)
			throw new IllegalArgumentException("Font size must be positive.");
		this.fontSize = fontSize;
	}
	
	public String getFontName() {
		return fontName;
	}
	
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getJustify() {
		return justify;
	}

	public void setJustify(int justify) {
		if (justify != CENTER && justify != RIGHT && justify != LEFT)
			throw new IllegalArgumentException("Justify can only be CENTER, LEFT, or RIGHT");
		this.justify = justify;
	}


}
