package net.afterlifelochie.fontbox.data;

import net.afterlifelochie.fontbox.document.formatting.TextFormat;

public class FormattedString {

	public final String string;
	public final TextFormat[] format;

	public FormattedString(String string) {
		this.string = string;
		this.format = new TextFormat[string.length()];
	}

	public FormattedString applyFormat(TextFormat format, int index) {
		this.format[index] = format;
		return this;
	}

}
