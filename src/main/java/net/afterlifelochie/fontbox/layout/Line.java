package net.afterlifelochie.fontbox.layout;

/**
 * One formatted line with a spacing and line-height
 * 
 * @author AfterLifeLochie
 */
public class Line extends Container {
	/** The real text */
	public final String line;
	/** The size of the spacing between words */
	public final int space_size;

	/**
	 * Create a new line
	 * 
	 * @param line
	 *            The line's text
	 * @param width
	 *            The width of the line
	 * @param height
	 *            The height of the line
	 * @param space_size
	 *            The size of the spacing between words
	 */
	public Line(String line, int width, int height, int space_size) {
		super(width, height);
		this.line = line;
		this.space_size = space_size;
	}
}