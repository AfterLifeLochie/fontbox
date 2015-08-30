package net.afterlifelochie.fontbox.layout;

import net.afterlifelochie.fontbox.document.property.FloatMode;

/**
 * <p>
 * Represents a container for the logical storage of object bounds in memory.
 * </p>
 * <p>
 * The bounds container stores two types of values; the width, height and
 * floating mode - which are defined by the owning element; and the x and y
 * coordinate - which are defined when the page is being written. The values in
 * the former group must not be undefined and must be well-formed and the values
 * in the latter group must be undefined until the pagination occurs.
 * </p>
 * <p>
 * The values in this container should be considered volatile; that is, the
 * values may (and will) be overwritten during pagination (and possibly
 * rendering), so you should access values in a thread-safe way.
 * </p>
 * 
 * @author AfterLifeLochie
 *
 */
public class ObjectBounds {

	/** The x-coordinate after pagination of the object */
	public int x;
	/** The y-coordinate after pagination of the object */
	public int y;
	/** The width of the object */
	public int width;
	/** The height of the object */
	public int height;
	/** The floating mode of the object */
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
