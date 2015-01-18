package opc;


/**
 * Parent class for all animations against a PixelStrip.
 */
public abstract class Animation {
	
	public static final int BLACK = Animation.makeColor(0, 0, 0);

	/**
	 * Reset any internal variables.
	 * @param strip a strip of pixels.
	 */
	public abstract void reset(PixelStrip strip);
	
	/**
	 * @param strip  a strip of pixels.
	 * @return whether a redraw is needed.
	 */
	public abstract boolean draw(PixelStrip strip);

	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// Utility functions:
	
	/**
	 * Package red/green/blue values into a single integer.
	 */
	final public static int makeColor(int red, int green, int blue) {
		assert red >=0 && red <= 255;
		assert green >=0 && green <= 255;
		assert blue >=0 && red <= blue;
		int r = red & 0x000000FF;
		int g = green & 0x000000FF;
		int b = blue & 0x000000FF;
		return (r << 16) | (g << 8) | (b) ;
	}
	
	/**
	 * Extract the red component from a color integer.
	 */
	final public static int getRed(int color) {
		return (color >> 16) & 0x000000FF;
	}
	
	/**
	 * Extract the green component from a color integer.
	 */
	final public static int getGreen(int color) {
		return (color >> 8) & 0x000000FF;
	}
	
	/**
	 * Extract the blue component from a color integer.
	 */
	final public static int getBlue(int color) {
		return color & 0x000000FF;
	}
	
	/**
	 * Return a color that has been faded by the given brightness.
	 * @param brightness  a number from 0 to 255.
	 * @return a new color.
	 */
	final public static int fadeColor(int c, int brightness) {
		int r = ((c >> 16) & 0x000000FF);
		int g = ((c >> 8) & 0x000000FF);
		int b = (c & 0x000000FF);
		r = (int) (((int) r * brightness / 256));
		g = (int) (((int) g * brightness / 256));
		b = (int) (((int) b * brightness / 256));
		return (r << 16) | (g << 8) | (b) ;
	}
	
	/**
	 * @return The current time in milliseconds.
	 */
	protected long millis() {
		return System.currentTimeMillis() - programStartTime;
	}
	static private long programStartTime = System.currentTimeMillis();
}

