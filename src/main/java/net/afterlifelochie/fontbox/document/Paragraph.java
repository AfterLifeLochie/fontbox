package net.afterlifelochie.fontbox.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.afterlifelochie.io.MixedObjectInputStream;

public class Paragraph extends Element {

	/** The list of paragraph elements */
	public List<Object> elements;
	/** The paragraph elements alignment mode */
	public AlignmentMode align;

	/**
	 * Create a new paragraph with a specified text and the default alignment
	 * (justified).
	 * 
	 * @deprecated To be replaced with a mixed-type stream
	 * @param text
	 *            The text
	 */
	public Paragraph(String text) {
		this(text, AlignmentMode.JUSTIFY);
	}

	/**
	 * Create a new paragraph with the specified properties.
	 * 
	 * @deprecated To be replaced with a mixed-type stream
	 * @param text
	 *            The text
	 * @param align
	 *            The alignment mode
	 */
	public Paragraph(String text, AlignmentMode align) {
		this.elements = new ArrayList<Object>();
		this.elements.add(text);
		this.align = align;
	}
	
	public Paragraph(List<Object> elements, AlignmentMode align) {
		this.elements = elements;
		this.align = align;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		Page page = writer.current();
		MixedObjectInputStream stream = new MixedObjectInputStream(elements);
		boxText(trace, writer, page.properties.bodyFont, stream, null, AlignmentMode.JUSTIFY);
		writer.cursor().pushDown(10);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void update() {
		/* No action required */
	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame) {
		/* No action required */
	}

	@Override
	public void clicked(BookGUI gui, int mx, int my) {
		/* No action required */
	}

	@Override
	public void typed(BookGUI gui, char val, int code) {
		/* No action required */
	}

}
