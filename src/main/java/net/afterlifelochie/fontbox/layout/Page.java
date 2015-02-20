package net.afterlifelochie.fontbox.layout;

import java.util.ArrayList;

import net.afterlifelochie.fontbox.document.Element;

/**
 * One whole page containing a collection of spaced lines with line-heights and
 * inside a page margin (gutters).
 * 
 * @author AfterLifeLochie
 */
public class Page extends Container {

	/** The page layout properties container */
	public PageProperties properties;

	/** The list of elements on the page */
	public ArrayList<Element> elements = new ArrayList<Element>();

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
}
