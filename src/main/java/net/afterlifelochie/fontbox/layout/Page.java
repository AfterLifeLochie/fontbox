package net.afterlifelochie.fontbox.layout;

import java.util.LinkedList;

/**
 * One whole page containing a collection of spaced lines with line-heights and
 * inside a page margin (gutters).
 * 
 * @author AfterLifeLochie
 */
public class Page extends Container {

	public PageProperties properties;
	public LinkedList<Line> lines = new LinkedList<Line>();

	public Page(PageProperties properties) {
		super(properties.width, properties.height);
		this.properties = properties;
	}

	public int getFreeHeight() {
		int h = height;
		for (Line line : lines)
			h -= line.line_height;
		return h;
	}
}
