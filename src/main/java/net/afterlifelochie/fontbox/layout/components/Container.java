package net.afterlifelochie.fontbox.layout.components;

/**
 * Layout container object. Containers have properties such as a width and a
 * height, and can contain child Container elements.
 * 
 * @author AfterLifeLochie
 *
 */
public abstract class Container {

	/** The width of the container */
	public int width;
	/** The height of the container */
	public int height;

	/**
	 * Initialize a new container
	 * 
	 * @param width
	 *            The desired width
	 * @param height
	 *            The desired height
	 */
	public Container(int width, int height) {
		this.width = width;
		this.height = height;
	}

}
