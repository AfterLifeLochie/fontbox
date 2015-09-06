package net.afterlifelochie.fontbox.layout.components;

import java.util.ArrayList;

import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.layout.ObjectBounds;

/**
 * One whole page containing a collection of spaced lines with line-heights and
 * inside a page margin (gutters).
 *
 * @author AfterLifeLochie
 */
public class Page extends Container {

	/** The page layout properties container */
	public PageProperties properties;

	/** The list of static elements on the page */
	private ArrayList<Element> staticElements = new ArrayList<Element>();
	/** The list of dynamic elements on the page */
	private ArrayList<Element> dynamicElements = new ArrayList<Element>();

	/**
	 * Initialize a new Page with a specified set of page layout properties.
	 *
	 * @param properties
	 *            The page layout properties.
	 */
	public Page(PageProperties properties) {
		super(properties.width, properties.height);
		this.properties = properties;
	}

	public ArrayList<Element> allElements() {
		ArrayList<Element> all = new ArrayList<Element>();
		all.addAll(staticElements);
		all.addAll(dynamicElements);
		return all;
	}

	/**
	 * Get a list of all static elements on the page
	 *
	 * @return The list of static elements on the page
	 */
	public ArrayList<Element> staticElements() {
		return staticElements;
	}

	/**
	 * Get a list of all dynamic elements on the page
	 *
	 * @return The list of dynamic elements on the page
	 */
	public ArrayList<Element> dynamicElements() {
		return dynamicElements;
	}

	/**
	 * Push an element onto the page, unchecked.
	 *
	 * @param element
	 *            The element to push
	 */
	public void push(Element element) {
		if (!element.canCompileRender())
			dynamicElements.add(element);
		else
			staticElements.add(element);
	}

	/**
	 * Determine if the provided bounding box intersects with an existing
	 * element on the page. Returns true if an intersection occurs, false if
	 * not.
	 *
	 * @param bounds
	 *            The bounding box to check
	 * @return If an intersection occurs
	 */
	public Element intersectsElement(ObjectBounds bounds) {
		for (Element element : staticElements)
			if (element.bounds() != null && element.bounds().intersects(bounds))
				return element;
		return null;
	}

	/**
	 * Determine if the provided bounding box fits entirely on the page. Returns
	 * true if the bounding box fits inside the page, false if not.
	 *
	 * @param bounds
	 *            The bounding box to check
	 * @return If the bounding box fits inside the page
	 */
	public boolean insidePage(ObjectBounds bounds) {
		if (bounds.x < 0 || bounds.y < 0 || bounds.x > width || bounds.y > height)
			return false;
		if (bounds.x + bounds.width > width || bounds.y + bounds.height > height)
			return false;
		return true;
	}
}
