package net.afterlifelochie.fontbox.layout;

/**
 * One formatted line with a spacing and line-height
 * 
 * @author AfterLifeLochie
 */
public class Line {
	public final String line;
	public final int space_size;
	public final int line_height;

	public Line(String line, int space_size, int line_height) {
		this.line = line;
		this.space_size = space_size;
		this.line_height = line_height;
	}
}