package net.afterlifelochie.fontbox.layout;

import java.util.ArrayList;

import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.document.Element;

public class DocumentProcessor {

	public static ArrayList<Page> generatePages(Document doc) {
		ArrayList<Page> pages = new ArrayList<Page>();
		for (int i = 0; i < doc.elements.size(); i++) {
			Element e0 = doc.elements.get(i);
			pushElement(pages, e0);
		}
		return pages;
	}

	private static void pushElement(ArrayList<Page> pages, Element element) {
		Page last = pages.get(pages.size() - 1);
		pushElement(pages, element, findCursor(last));
	}

	private static void pushElement(ArrayList<Page> pages, Element element, int cursor) {

	}

	private static int findCursor(Page page) {
		/*
		 * page => objects->delegate() { dheight > height? height = dheight :
		 * void; }; return dheight;
		 */
		return 0;
	}

}
