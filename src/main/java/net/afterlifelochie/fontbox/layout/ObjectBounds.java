package net.afterlifelochie.fontbox.layout;

import net.afterlifelochie.fontbox.document.property.FloatMode;

public class ObjectBounds {

	public int x, y;
	public int width, height;
	public FloatMode floating;

	public ObjectBounds(int x, int y, int width, int height, FloatMode floating) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.floating = floating;
	}

	public boolean floating() {
		return floating != FloatMode.NONE;
	}

	public FloatMode floatMode() {
		return floating;
	}

	public boolean intersects(ObjectBounds that) {
		boolean flag0 = (x < that.x + that.width && x + width > that.x);
		boolean flag1 = (y < that.y + that.height && y + height > that.y);
		return flag0 && flag1;
	}

	public boolean encloses(int x, int y) {
		boolean flag0 = (this.x <= x && this.x + width >= x);
		boolean flag1 = (this.y <= y && this.y + height >= y);
		return flag0 && flag1;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "] => [" + width + " x " + height + "]";
	}

	public boolean inside(ObjectBounds bounds) {
		return bounds.encloses(x, y) && bounds.encloses(x + width, y + height);
	}
}
