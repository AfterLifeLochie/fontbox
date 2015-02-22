package net.afterlifelochie.fontbox.layout;

public class ObjectBounds {

	public int x, y;
	public int width, height;
	public boolean floating;

	public ObjectBounds(int x, int y, int width, int height, boolean floating) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.floating = floating;
	}

	public boolean floating() {
		return floating;
	}

}
