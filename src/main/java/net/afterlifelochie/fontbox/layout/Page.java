package net.afterlifelochie.fontbox.layout;

import java.util.LinkedList;

/**
 * One whole page containing a collection of spaced lines with line-heights and
 * inside a page margin (gutters).
 * 
 * @author AfterLifeLochie
 */
public class Page extends Container {

	/** The page layout properties container */
	public PageProperties properties;
	/** The list of lines */
	public LinkedList<Line> lines = new LinkedList<Line>();

	/**
	 * Initialize a new Page with a specified set of page layout properties.
	 * 
	 * @param properties
	 *            The page layout properties.
	 */
	public Page(PageProperties properties) {
		super(properties.width, properties.height);
		this.properties = properties;
	}

	/**
	 * Get the total quantity of free height on the page.
	 * 
	 * @return The total quantity of free height on the page
	 */
	public int getFreeHeight() {
		int h = height;
		for (Line line : lines)
			h -= line.height;
		return h;
	}
}
