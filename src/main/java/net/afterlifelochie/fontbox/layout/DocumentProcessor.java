package net.afterlifelochie.fontbox.layout;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.document.Element;

public class DocumentProcessor {

	public static ArrayList<Page> generatePages(Document doc, PageProperties layout) throws IOException {
		PageWriter writer = new PageWriter(layout);
		for (int i = 0; i < doc.elements.size(); i++) {
			Element e0 = doc.elements.get(i);
			pushElement(writer, e0);
		}
		return writer.pages();
	}

	private static void pushElement(PageWriter writer, Element element) throws IOException {
		Page current = writer.current();
		pushElement(writer, element, findCursor(current));
	}

	private static void pushElement(PageWriter writer, Element element, int cursor) throws IOException {

	}

	private static int findCursor(Page page) {
		/*
		 * page => objects->delegate() { dheight > height? height = dheight :
		 * void; }; return dheight;
		 */
		
		
		return 0;
	}

}
