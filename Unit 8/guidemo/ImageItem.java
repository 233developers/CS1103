package guidemo.guidemo;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Represents an image, drawn with its center at a specified point.
 */
public class ImageItem {
	
	private BufferedImage image;
	private int centerX, centerY;
		
	public ImageItem(BufferedImage image, int centerX, int centerY) {
		this.image = image;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public void draw(Graphics g) {
		g.drawImage(image,centerX-image.getWidth()/2,centerY-image.getHeight()/2,null);
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		if (image == null)
			throw new IllegalArgumentException("Null image not allowed");
		this.image = image;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setPosition(int x, int y) {
		centerX = x;
		centerY = y;
	}

	public boolean contains(int x, int y) {
		int w = image.getWidth();
		int h = image.getHeight();
		return x > centerX - w/2 && x < centerX + w/2 && y > centerY - h/2 && y < centerY + h/2;
	}
	
}
