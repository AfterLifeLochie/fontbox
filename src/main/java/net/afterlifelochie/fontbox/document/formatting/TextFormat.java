package net.afterlifelochie.fontbox.document.formatting;

import java.util.Set;

import net.afterlifelochie.fontbox.font.GLFont;

public class TextFormat {

	public final Set<DecorationStyle> decorations;
	public final GLFont font;

	public TextFormat(GLFont font) {
		this(null, font);
	}

	public TextFormat(Set<DecorationStyle> decorations) {
		this(decorations, null);
	}

	public TextFormat(Set<DecorationStyle> decorations, GLFont font) {
		this.decorations = decorations;
		this.font = font;
	}

}
