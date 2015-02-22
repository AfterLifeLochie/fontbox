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

}
