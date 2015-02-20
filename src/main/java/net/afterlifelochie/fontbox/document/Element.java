package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.layout.ObjectBounds;

public class Element {

	private ObjectBounds bounds;

	public ObjectBounds bounds() {
		return this.bounds;
	}

	public void setBounds(ObjectBounds bb) {
		this.bounds = bb;
	}

}
