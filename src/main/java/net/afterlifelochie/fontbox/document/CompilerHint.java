package net.afterlifelochie.fontbox.document;

public class CompilerHint extends Element {

	public static enum HintType {
		PAGEBREAK, FLOATBREAK;
	}

	public HintType type;

	public CompilerHint(HintType type) {
		this.type = type;
	}
}
