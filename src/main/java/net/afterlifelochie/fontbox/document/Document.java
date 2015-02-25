package net.afterlifelochie.fontbox.document;

import java.util.ArrayList;

/**
 * Document class. Used to contain an ordered list of Elements which are later
 * transformed into renderable Elements for the book renderer.
 * 
 * @author AfterLifeLochie
 */
public class Document {

	/** The list of elements in the document */
	public ArrayList<Element> elements;

	/**
	 * Creates a new blank Document
	 */
	public Document() {
		this.elements = new ArrayList<Element>();
	}

	/**
	 * <p>
	 * Pushes an element onto the end of the document.
	 * </p>
	 * <p>
	 * The element must not already exist in the document. If the element
	 * already exists in the document, an {@link IllegalArgumentException} will
	 * be thrown.
	 * </p>
	 * 
	 * @param element
	 *            The element to add
	 */
	public void push(Element element) {
		if (elements.contains(element))
			throw new IllegalArgumentException("Element already exists in tree!");
		elements.add(element);
	}

	/**
	 * <p>
	 * Removes an element from the end of the document and returns it. If the
	 * document is empty, null will be returned.
	 * </p>
	 * 
	 * @return The element on the end of the document, or null if the document
	 *         is empty
	 */
	public Element pop() {
		if (elements.size() == 0)
			return null;
		return elements.remove(elements.size() - 1);
	}

	/**
	 * <p>
	 * Returns the element from the beginning of the document. If the document
	 * is empty, null is returned.
	 * </p>
	 * 
	 * @return The first element in the document, or null
	 */
	public Element head() {
		return elements.get(0);
	}

	/**
	 * <p>
	 * Returns last element from the end of the document. If the document is
	 * empty, null is returned.
	 * </p>
	 * 
	 * @return The last element in the document, or null
	 */
	public Element tail() {
		return elements.get(elements.size() - 1);
	}

}
