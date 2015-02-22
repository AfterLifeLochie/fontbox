package net.afterlifelochie.fontbox.document;

import java.io.IOException;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.PageWriter;

public class Paragraph extends Element {

	public String text;
	public AlignmentMode align;

	public Paragraph(String text) {
		this(text, AlignmentMode.JUSTIFY);
	}

	public Paragraph(String text, AlignmentMode align) {
		this.text = text;
		this.align = align;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		// TODO: where do we get the metric from?
		// TODO: where does alignment get specified? :\
		boxText(trace, writer, metric, text);
	}

}
