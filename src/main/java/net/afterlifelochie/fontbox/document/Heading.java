package net.afterlifelochie.fontbox.document;

import java.io.IOException;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.PageWriter;

public class Heading extends Element {

	public String id;
	public String text;

	public Heading(String id, String text) {
		this.id = id;
		this.text = text;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		// TODO: where do we get the metric from?
		// boxText(trace, writer, metric, text);
	}

}
