package net.afterlifelochie.fontbox.document;

import java.io.IOException;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.PageCursor;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.render.BookGUI;

public class Heading extends Element {

	public String id;
	public String text;

	/**
	 * Creates a new Heading element
	 * 
	 * @param id
	 *            The heading's unique identifier
	 * @param text
	 *            The heading's text value
	 */
	public Heading(String id, String text) {
		this.id = id;
		this.text = text;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		Page page = writer.current();
		boxText(trace, writer, page.properties.headingFont, text, AlignmentMode.LEFT);
		writer.cursor().pushDown(10);
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

	@Override
	public String identifier() {
		return id;
	}

}
