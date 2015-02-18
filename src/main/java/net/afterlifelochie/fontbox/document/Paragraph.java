package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.document.property.AlignmentMode;

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

}
