package net.afterlifelochie.fontbox.layout;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.CompilerHint;
import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.document.Heading;
import net.afterlifelochie.fontbox.document.Image;
import net.afterlifelochie.fontbox.document.ImageBlock;
import net.afterlifelochie.fontbox.document.ImageItem;
import net.afterlifelochie.fontbox.document.Paragraph;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.layout.components.PageProperties;

public class DocumentProcessor {

	public static ArrayList<Page> generatePages(ITracer trace, Document doc, PageProperties layout) throws IOException,
			LayoutException {
		PageWriter writer = new PageWriter(layout);
		for (int i = 0; i < doc.elements.size(); i++) {
			Element e0 = doc.elements.get(i);
			pushElement(trace, writer, e0);
		}
		return writer.pages();
	}

	private static void pushElement(ITracer trace, PageWriter writer, Element element) throws IOException,
			LayoutException {
		if (element instanceof CompilerHint) {
			CompilerHint hint = (CompilerHint) element;
			hint.layout(trace, writer);
		} else if (element instanceof Heading) {
			Heading heading = (Heading) element;
			heading.layout(trace, writer);
		} else if (element instanceof Image) {
			Image image = (Image) element;
			if (image instanceof ImageBlock) {
				ImageBlock blockIconImg = (ImageBlock) image;
				blockIconImg.layout(trace, writer);
			} else if (image instanceof ImageItem) {
				ImageItem itemIconImg = (ImageItem) image;
				itemIconImg.layout(trace, writer);
			} else {
				image.layout(trace, writer);
			}
		} else if (element instanceof Paragraph) {
			Paragraph paragraph = (Paragraph) element;
			paragraph.layout(trace, writer);
		}
	}
}
