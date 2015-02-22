package net.afterlifelochie.fontbox.layout.components;

import java.io.IOException;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.render.BookGUI;

/**
 * One formatted line with a spacing and line-height
 * 
 * @author AfterLifeLochie
 */
public class Line extends Element {
	/** The real text */
	public final String line;
	/** The size of the spacing between words */
	public final int space_size;
	/** The font to render the line in */
	public GLFont font;

	/**
	 * Create a new line
	 * 
	 * @param line
	 *            The line's text
	 * @param bounds
	 *            The location of the line
	 * @param font
	 *            The font to draw with
	 * @param space_size
	 *            The size of the spacing between words
	 */
	public Line(String line, ObjectBounds bounds, GLFont font, int space_size) {
		this.setBounds(bounds);
		this.line = line;
		this.font = font;
		this.space_size = space_size;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		throw new LayoutException("Cannot layout Line type; Line already laid!");
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clicked(BookGUI gui, int mx, int my) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void typed(BookGUI gui, char val, int code) {
		// TODO Auto-generated method stub
		
	}
}