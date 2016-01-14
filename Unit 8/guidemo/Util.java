package guidemo.guidemo;

import java.applet.AudioClip;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JApplet;

/**
 * This class provides some static utility functions for working
 * with resources (to avoid having to look up all the messy details).
 * Resources are stored somewhere on the class path, usually in their
 * own package.  They are located by paths to files, such as
 * "resources/images/mandelbrot.jpeg".
 */
public class Util {
	
	/**
	 * Load an image resource.  In this case, the data will actually
	 * be read into memory only when the Image is first drawn.
	 * @param pathToResource the path to the resource.
	 * @return the image, or null if the resource can't be located.
	 */
	public static Image getImageResource(String pathToResource) {
		ClassLoader cl = Util.class.getClassLoader();
		URL loc = cl.getResource(pathToResource);
		if (loc == null)
			return null;
		Image img = Toolkit.getDefaultToolkit().createImage(loc);
		return img;
	}
	
	/**
	 * Load a buffered image from a resource.  In this case, the method
	 * does not return until the image data has been read and stored
	 * in memory.
	 * @param pathToResource the path to the resource.
	 * @return the image, or null if the resource can't be loaded.
	 */
	public static BufferedImage getBufferedImageResource(String pathToResource) {
		ClassLoader cl = Util.class.getClassLoader();
		URL loc = cl.getResource(pathToResource);
		if (loc == null)
			return null;
		try {
			return ImageIO.read(loc);
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Create an ImageIcon from an image that is stored as a resource.
	 * @param pathToResource the path to the resource.
	 * @return the ImageIcon, or null if the resource can't be located.
	 */
	public static ImageIcon iconFromResource(String pathToResource) {
		Image img = getImageResource(pathToResource);
		if (img == null)
			return null;
		else
			return new ImageIcon(img);
	}
	
	/**
	 * Play a sound that is stored as a resource file.  If the resource
	 * can't be located or can't be played, no sound is played, and
	 * no exception is thrown.
	 * @param pathToResource the path to the resource.
	 */
	public static void playSoundResource(String pathToResource) {
		try {
			ClassLoader cl = Util.class.getClassLoader();
			URL loc = cl.getResource(pathToResource);
			AudioClip sound = JApplet.newAudioClip(loc);
			sound.play();
		}
		catch (Exception e) {
			System.out.println("Can't play sound " + pathToResource);
		}
	}
	
	/**
	 * Load an AudioClip from a resource file.  The clip can be played
	 * by calling its play() method.
	 * @param pathToResource the path to the resource.
	 * @return the audio clip, or null if the resource can't be loaded.
	 */
	public static AudioClip getSound(String pathToResource) {
		ClassLoader cl = Util.class.getClassLoader();
		URL loc = cl.getResource(pathToResource);
		if (loc == null)
			return null;
		try {
			return JApplet.newAudioClip(loc);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Create a cursor from an image, with hot point at the upper left
	 * corner (0,0).
	 * @param image the image; can't be null.
	 * @return a cursor that will show the image.
	 */
	public static Cursor createImageCursor(Image image) {
		return createImageCursor(image, 0, 0);
	}

	/**
	 * Create a cursor from an image resource file, with hot point at the
	 * upper left corner (0,0).
	 * @param pathToResource the path to the resource.
	 * @return the cursor or, if the resource can't be loaded, the
	 * default cursor.
	 */
	public static Cursor createImageCursor(String pathToResource) {
		return createImageCursor(pathToResource, 0, 0);
	}

	/**
	 * Create a cursor from a resource file, with hot point at 
	 * (hotSpotX, hotSpotY).
	 * @param pathToResource the path to the resource.
	 * @return a cursor that will show the image.
	 */
	public static Cursor createImageCursor(String pathToResource, int hotSpotX, int hotSpotY) {
		Image img = getImageResource(pathToResource);
		if (img == null)
			return Cursor.getDefaultCursor();
		else
			return Toolkit.getDefaultToolkit().createCustomCursor(
					img, new Point(hotSpotX,hotSpotY), pathToResource );
	}
	
	/**
	 * Create a cursor from an image, with hot point at 
	 * (hotSpotX, hotSpotY).
	 * @param image the image; can't be null.
	 * @return a cursor that will show the image.
	 */
	public static Cursor createImageCursor(Image image, int hotSpotX, int hotSpotY) {
		return Toolkit.getDefaultToolkit().createCustomCursor(
					image, new Point(hotSpotX,hotSpotY), null );
	}
	
	
	
}
