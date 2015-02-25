package net.afterlifelochie.fontbox.document;

import java.util.ArrayList;

public class Document {

	public ArrayList<Element> elements;

	public Document() {
		this.elements = new ArrayList<Element>();
	}

	public void push(Element element) {
		if (elements.contains(element))
			throw new IllegalArgumentException("Element already exists in tree!");
		elements.add(element);
	}

	public Element pop() {
		return elements.remove(elements.size() - 1);
	}

	public Element head() {
		return elements.get(0);
	}

	public Element tail() {
		return elements.get(elements.size() - 1);
	}

}
