package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;

public abstract class Element {

	private ObjectBounds bounds;

	public ObjectBounds bounds() {
		return this.bounds;
	}

	public void setBounds(ObjectBounds bb) {
		this.bounds = bb;
	}
	
	public abstract void layout(PageWriter writer);

}
