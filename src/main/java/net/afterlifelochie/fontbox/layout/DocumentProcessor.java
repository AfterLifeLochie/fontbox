package net.afterlifelochie.fontbox.layout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import net.afterlifelochie.fontbox.document.CompilerHint;
import net.afterlifelochie.fontbox.document.CompilerHint.HintType;
import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.document.Heading;
import net.afterlifelochie.fontbox.document.Image;
import net.afterlifelochie.fontbox.document.ImageBlock;
import net.afterlifelochie.fontbox.document.ImageItem;
import net.afterlifelochie.fontbox.document.Paragraph;

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
		int cursor = findCursor(current);
		if (cursor == -1) {
			current = writer.next();
			cursor = findCursor(current);
		}
		pushElement(writer, element, cursor);
	}

	private static void pushElement(PageWriter writer, Element element, int cursor) throws IOException {
		Page work = writer.current();
		if (element instanceof CompilerHint) {
			CompilerHint hint = (CompilerHint) element;
			Iterator<HintType> hints = hint.types.iterator();
			while (hints.hasNext()) {
				HintType whatHint = hints.next();
				switch (whatHint) {
				case FLOATBREAK:
					// TODO: shift cursor somehow, dummy element needed
					break;
				case PAGEBREAK:
					writer.next();
					break;
				default:
					// TODO: panic?
					break;
				}
			}
		} else if (element instanceof Heading) {
			Heading heading = (Heading) element;

		} else if (element instanceof Image) {
			Image image = (Image) element;
			if (image instanceof ImageBlock) {
				ImageBlock blockIconImg = (ImageBlock) image;

			} else if (image instanceof ImageItem) {
				ImageItem itemIconImg = (ImageItem) image;
			} else {

			}
		} else if (element instanceof Paragraph) {
			Paragraph paragraph = (Paragraph) element;

		}

	}

	private static int findCursor(Page page) {
		int cursor = 0;
		for (Element ez : page.elements) {
			ObjectBounds box = ez.bounds();
			if (box == null)
				continue;
			int tail = box.y + box.height;
			if (tail > cursor)
				cursor = tail;
		}
		if (cursor >= page.properties.height)
			return -1;
		return cursor;
	}

}
