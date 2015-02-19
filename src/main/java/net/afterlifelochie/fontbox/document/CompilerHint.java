package net.afterlifelochie.fontbox.document;

import java.util.EnumSet;

public class CompilerHint extends Element {

	public static enum HintType {
		PAGEBREAK, FLOATBREAK;
	}

	public EnumSet<HintType> types;

	public CompilerHint(HintType type) {
		this.types = EnumSet.of(type);
	}
	
	public CompilerHint(EnumSet<HintType> types) {
		this.types = types;
	}
}
