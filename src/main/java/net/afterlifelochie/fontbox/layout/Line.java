package net.afterlifelochie.fontbox.layout;

/**
 * One formatted line with a spacing and line-height
 * 
 * @author AfterLifeLochie
 */
public class Line {
	/** The real text */
	public final String line;
	/** The size of the spacing between words */
	public final int space_size;
	/** The height of the line */
	public final int line_height;

	/**
	 * Create a new line
	 * 
	 * @param line
	 *            The line's text
	 * @param space_size
	 *            The size of the spacing between words
	 * @param line_height
	 *            The height of the line
	 */
	public Line(String line, int space_size, int line_height) {
		this.line = line;
		this.space_size = space_size;
		this.line_height = line_height;
	}
}