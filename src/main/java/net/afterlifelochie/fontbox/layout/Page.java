package net.afterlifelochie.fontbox.layout;

import java.util.LinkedList;

/**
 * One whole page containing a collection of spaced lines with line-heights and
 * inside a page margin (gutters).
 * 
 * @author AfterLifeLochie
 */
public class Page extends Container {
	public final int margin_left;
	public final int margin_right;
	public final int min_space_size;
	public final int lineheight_size;
	public LinkedList<Line> lines = new LinkedList<Line>();

	public Page(int w, int h, int ml, int mr, int min_sp, int min_lhs) {
		super(w, h);
		margin_left = ml;
		margin_right = mr;
		min_space_size = min_sp;
		lineheight_size = min_lhs;
	}

	public int getFreeHeight() {
		int h = height;
		for (Line line : lines)
			h -= line.line_height;
		return h;
	}
}
