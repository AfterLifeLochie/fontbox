package net.afterlifelochie.fontbox.document;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.font.GLFontMetrics;
import net.afterlifelochie.fontbox.font.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.PageCursor;
import net.afterlifelochie.fontbox.layout.components.Line;
import net.afterlifelochie.fontbox.layout.components.LineWriter;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.afterlifelochie.fontbox.render.RenderException;
import net.afterlifelochie.io.StackedPushbackStringReader;

/**
 * <p>
 * Document element class. Elements are used to in a Document to construct a
 * linked list of objects which are subsequently paginated and rendered.
 * </p>
 * 
 * @author AfterLifeLochie
 *
 */
public abstract class Element {

	private ObjectBounds bounds;

	/**
	 * Get the bounds of the object
	 * 
	 * @return The bounds of the object
	 */
	public ObjectBounds bounds() {
		return this.bounds;
	}

	/**
	 * Set the bounds of the object
	 * 
	 * @param bb
	 *            The new bounds of the object
	 */
	public void setBounds(ObjectBounds bb) {
		this.bounds = bb;
	}

	/**
	 * <p>
	 * Called by the document generator to request this element fill in it's
	 * rendering-based properties. The element should place itself on the
	 * current page and update the writing cursor if required.
	 * </p>
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param writer
	 *            The current page writer
	 * @throws IOException
	 *             Any I/O exception which occurs when writing to the stream
	 * @throws LayoutException
	 *             Any exception which prevents the element from being written
	 *             to the writing stream
	 */
	public abstract void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException;

	/**
	 * Called to determine if this element requires explicit update ticks.
	 * 
	 * @return If the element requires update ticks
	 */
	public abstract boolean canUpdate();

	/**
	 * Called to update the interface
	 */
	public abstract void update();

	/**
	 * <p>
	 * Called to render the element on the page. You should use the pre-computed
	 * rendering properties generated through the call to
	 * {@link Element#layout(ITracer, PageWriter)}.
	 * </p>
	 * 
	 * @param gui
	 *            The GUI rendering on
	 * @param mx
	 *            The mouse x-coordinate
	 * @param my
	 *            The mouse y-coordinate
	 * @param frame
	 *            The current partial frame
	 * @throws RenderException
	 *             Any rendering exception which prevents the element from being
	 *             rendered on the page
	 */
	public abstract void render(BookGUI gui, int mx, int my, float frame) throws RenderException;

	/**
	 * <p>
	 * Called by the container controller when a click occurs on the element.
	 * </p>
	 * 
	 * @param gui
	 *            The GUI being clicked
	 * @param mx
	 *            The mouse x-coordinate
	 * @param my
	 *            The mouse y-coordinate
	 */
	public abstract void clicked(BookGUI gui, int mx, int my);

	/**
	 * <p>
	 * Called by the container when a key press occurs.
	 * </p>
	 * 
	 * @param gui
	 *            The GUI being typed into
	 * @param val
	 *            The character value
	 * @param code
	 *            The key code
	 */
	public abstract void typed(BookGUI gui, char val, int code);

	/**
	 * <p>
	 * Called by the generator to ask for unique ID for this element. If the
	 * object does not need to be indexed, this method should return null and
	 * not be overridden; else, you should return a unique identifier.
	 * </p>
	 * 
	 * @return The unique identifier for this element
	 */
	public String identifier() {
		return null;
	}

	/**
	 * <p>
	 * Attempt to box text from a string onto as many pages as is required. The
	 * text provided will be added to the tail of the current page and will
	 * overflow onto any subsequent pages as is required.
	 * </p>
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param writer
	 *            The page writer
	 * @param font
	 *            The font to write with
	 * @param what
	 *            The text to write
	 * @param alignment
	 *            The text alignment mode
	 * @throws IOException
	 *             Any exception which occurs when reading from the text stream
	 * @throws LayoutException
	 *             Any layout problem which prevents the text from being laid
	 *             out correctly
	 */
	protected void boxText(ITracer trace, PageWriter writer, GLFont font, String what, AlignmentMode alignment)
			throws IOException, LayoutException {
		StackedPushbackStringReader reader = new StackedPushbackStringReader(what);
		trace.trace("Element.boxText", "startBox");
		while (reader.available() > 0) {
			Page current = writer.current();
			PageCursor cursor = writer.cursor();
			ObjectBounds bounds = new ObjectBounds(cursor.x(), cursor.y(), current.properties.width - cursor.x(),
					current.properties.height - cursor.y(), FloatMode.NONE);

			boxText(trace, writer, bounds, font, reader, alignment);
			trace.trace("Element.boxText", "streamRemain", reader.available());
			if (reader.available() > 0)
				writer.next();
		}
		trace.trace("Element.boxText", "endBox");
	}

	/**
	 * <p>
	 * Attempt to box text from a specified stream onto the page in the bounds
	 * specified. The text on the stream will be read and written into Line[]
	 * objects such that either:
	 * </p>
	 * <p>
	 * <ul>
	 * <li>The stream becomes empty; the cursor on the stream is placed at the
	 * end.</li>
	 * <li>No further text can be fit into the region specified by the bounds;
	 * the cursor on the stream is placed at the end of the last full word
	 * written onto the region specified.</li>
	 * </ul>
	 * </p>
	 *
	 * @param trace
	 *            The debugging tracer object
	 * @param writer
	 *            The underlying stream to write onto
	 * @param bounds
	 *            The bounding box to write inside
	 * @param font
	 *            The font to write with
	 * @param text
	 *            The text stream to read from
	 * @param alignment
	 *            The text alignment mode
	 * @throws IOException
	 *             Any exception which occurs when reading from the text stream
	 * @throws LayoutException
	 *             Any layout problem which prevents the text from being laid
	 *             out correctly
	 */
	protected void boxText(ITracer trace, PageWriter writer, ObjectBounds bounds, GLFont font,
			StackedPushbackStringReader text, AlignmentMode alignment) throws IOException, LayoutException {
		LineWriter stream = new LineWriter(writer, font, alignment);
		main: while (text.available() > 0) {
			// Put some words on the writer:
			while (true) {
				// Push the writer so we can back out
				text.pushPosition();

				// Build the word:
				StringBuilder inWord = new StringBuilder();
				char cz;
				while (true) {
					cz = text.next();
					// Skip spaces or tabs;
					if (cz != 0 && cz != ' ' && cz != '\t')
						inWord.append(cz); // push
					else if (inWord.length() > 0)
						break; // okay, consider now
					else if (cz == 0)
						break; // okay, end of stream
				}

				if (inWord.toString().trim().length() == 0)
					break;

				// Consider the word:
				trace.trace("Element.boxText", "considerWord", inWord.toString());
				stream.push(inWord.toString());
				ObjectBounds future = stream.pendingBounds();
				Page current = writer.current();
				trace.trace("Element.boxText", "considerCursor", writer.cursor());

				// If we overflow the page, back out last change to fit:
				if (!current.insidePage(future)) {
					trace.trace("Element.boxText", "overflowPage", current.width, current.height, future, stream.size());
					stream.pop();
					text.popPosition();
					// If there are now no words on the writer, then
					if (stream.size() == 0)
						break main; // nothing fits; break the loop
					else
						break; // break the local loop
				} else if (current.intersectsElement(future) != null) {
					// We hit another object, so let's undo
					trace.trace("Element.boxText", "collideElement", stream.size());
					Element e0 = current.intersectsElement(future);
					trace.trace("Element.boxText", "collideHit", e0.bounds().toString(), future.toString());
					stream.pop();
					text.popPosition();
					if (stream.size() == 0)
						break main; // Nothing fits at all where we are; break
					else
						break; // break the local loop
				} else {
					// Store our work
					trace.trace("Element.boxText", "commitWord");
					text.commitPosition();
				}
			}

			// Writer now contains a list of words which fit, so do something
			// useful with that line
			Line line = stream.emit();
			trace.trace("Element.boxText", "emitLine", line.line);
			writer.write(line);
		}
	}

}
