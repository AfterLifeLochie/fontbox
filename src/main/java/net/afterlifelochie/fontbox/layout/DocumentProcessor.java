package net.afterlifelochie.fontbox.layout;

import java.io.IOException;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.CompilerHint;
import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.document.Heading;
import net.afterlifelochie.fontbox.document.Image;
import net.afterlifelochie.fontbox.document.ImageItemStack;
import net.afterlifelochie.fontbox.document.Paragraph;
import net.afterlifelochie.fontbox.layout.components.Page;

public class DocumentProcessor {

	public static Element getElementAt(Page page, int x, int y) {
		for (Element element : page.allElements())
			if (element.bounds().encloses(x, y))
				return element;
		return null;
	}

	/**
	 * <p>
	 * Generate a list of formatted Pages from a Document and a Page layout
	 * configuration. The Document must contain at least one Element in the
	 * list. After processing, the Elements inside the Document are written to
	 * the pages on the writer stream.
	 * </p>
	 * <p>
	 * The Elements in the Document list are modified so that they contain
	 * rendering properties and other pre-computed parameters.
	 * </p>
	 * 
	 * @param trace
	 *            The debugger
	 * @param doc
	 *            The Document to transform
	 * @param writer
	 *            The page writer
	 * @throws IOException
	 *             Any I/O exception which occurs when reading from nested
	 *             streams or when writing to the Page output stream
	 * @throws LayoutException
	 *             Any layout exception which occurs when attempting to place an
	 *             element on a Page
	 */
	public static void generatePages(ITracer trace, Document doc, PageWriter writer) throws IOException,
			LayoutException {
		for (int i = 0; i < doc.elements.size(); i++) {
			Element e0 = doc.elements.get(i);
			pushElement(trace, writer, e0);
		}
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
			if (image instanceof ImageItemStack) {
				ImageItemStack blockIconImg = (ImageItemStack) image;
				blockIconImg.layout(trace, writer);
			} else {
				image.layout(trace, writer);
			}
		} else if (element instanceof Paragraph) {
			Paragraph paragraph = (Paragraph) element;
			paragraph.layout(trace, writer);
		}
	}
}
