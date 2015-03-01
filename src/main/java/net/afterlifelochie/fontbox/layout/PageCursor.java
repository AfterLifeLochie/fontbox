package net.afterlifelochie.fontbox.layout;

public class PageCursor {

	private int x, y;

	public PageCursor() {
		this.x = 0;
		this.y = 0;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public void left(int vx) {
		x = vx;
	}

	public void top(int vy) {
		y = vy;
	}

	public void pushLeft(int dx) {
		x += dx;
	}

	public void pushDown(int dy) {
		y += dy;
	}

}
