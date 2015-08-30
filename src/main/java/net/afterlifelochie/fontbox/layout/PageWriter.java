package net.afterlifelochie.fontbox.layout;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.layout.components.PageProperties;
import net.afterlifelochie.io.IntegerExclusionStream;

/**
 * Page writing stream. Used internally to facilitate ordered writing of doctree
 * elements to Page containers programmatically.
 * 
 * @author AfterLifeLochie
 *
 */
public class PageWriter {

	private Object lock = new Object();
	private ArrayList<Page> pages = new ArrayList<Page>();
	private ArrayList<PageCursor> cursors = new ArrayList<PageCursor>();
	private PageProperties attributes;
	private PageIndex index;
	private boolean closed = false;
	private int ptr = 0;

	/**
	 * Create an open a new page writing stream.
	 * 
	 * @param attributes
	 *            The default properties of all new pages created under the
	 *            writer, as part of pagination or if requested by written
	 *            elements.
	 */
	public PageWriter(PageProperties attributes) {
		this.attributes = attributes;
		index = new PageIndex();
	}

	/**
	 * Closes the stream.
	 */
	public void close() {
		synchronized (lock) {
			closed = true;
		}
	}

	private void checkOpen() throws IOException {
		synchronized (lock) {
			if (closed)
				throw new IOException("Writer closed!");
		}
	}

	/**
	 * Seeks backwards to the previous page on the writer and returns it.
	 * 
	 * @return The previous page on the writer
	 * @throws IOException
	 *             If the writer is writing the fist page, or if the stream is
	 *             closed, an IOException will be thrown.
	 */
	public Page previous() throws IOException {
		synchronized (lock) {
			checkOpen();
			seek(-1);
			return pages.get(ptr);
		}
	}

	/**
	 * Seeks forward to the previous page on the writer and returns it.
	 * 
	 * @return The next page on the writer.
	 * @throws IOException
	 *             If the writer has not written the next page, or if the stream
	 *             is closed, an IOException will be thrown.
	 */
	public Page next() throws IOException {
		synchronized (lock) {
			checkOpen();
			seek(1);
			return pages.get(ptr);
		}
	}

	/**
	 * Gets the current page on the writer.
	 * 
	 * @return The current page on the writer.
	 * @throws IOException
	 *             If the stream is closed, an IOException will be thrown.
	 */
	public Page current() throws IOException {
		synchronized (lock) {
			checkOpen();
			seek(0);
			return pages.get(ptr);
		}
	}

	/**
	 * Writes an element onto the underlying Page at the current cursor.
	 * 
	 * @param element
	 *            The element to write
	 * @return If the element was written successfully
	 * @throws IOException
	 *             If the element cannot be written to the page, if the stream
	 *             raises an exception when writing, or if the stream has
	 *             already been closed, an IOException will be thrown.
	 */
	public boolean write(Element element) throws IOException {
		synchronized (lock) {
			checkOpen();
			Page what = current();
			if (element.bounds() == null)
				throw new IOException("Cannot write unbounded object to page.");
			Fontbox.doAssert(what.insidePage(element.bounds()), "Element outside page boundary.");
			Element intersect = what.intersectsElement(element.bounds());
			Fontbox.doAssert(intersect == null, "Element intersects existing element " + intersect + ": box "
					+ ((intersect != null && intersect.bounds() != null) ? intersect.bounds() : "<null>") + " and "
					+ element.bounds() + "!");

			if (element.identifier() != null)
				index.push(element.identifier(), ptr);

			what.push(element);

			PageCursor current = cursor();
			for (Element e : what.elements()) {
				if (e.bounds().floating())
					continue;
				ObjectBounds bb = e.bounds();
				if (bb.y + bb.height + 1 > current.y())
					current.top(bb.y + bb.height + 1);
			}

			IntegerExclusionStream window = new IntegerExclusionStream(0, what.width);
			for (Element e : what.elements()) {
				ObjectBounds bb = e.bounds();
				if (current.y() >= bb.y && bb.y + bb.height >= current.y())
					window.excludeRange(0, bb.x + bb.width);
			}
			current.left(window.largest());

			Fontbox.tracer().trace("PageWriter.write", "pushCursor", current);

			return true;
		}
	}

	/**
	 * Get the current cursor on the current page.
	 * 
	 * @return The current cursor.
	 * @throws IOException
	 *             If the stream has been closed, an IOException will be thrown.
	 */
	public PageCursor cursor() throws IOException {
		synchronized (lock) {
			checkOpen();
			return cursors.get(ptr);
		}
	}

	private void seek(int which) throws IOException {
		synchronized (lock) {
			ptr += which;
			if (0 > ptr)
				ptr = 0;
			if (ptr > pages.size())
				ptr = pages.size();
			if (ptr == pages.size()) {
				pages.add(new Page(attributes.copy()));
				cursors.add(new PageCursor());
			}
		}
	}

	/**
	 * Get the list of pages on the stream. If the stream has not been closed,
	 * return a shallow-copy of the list of pages.
	 * 
	 * @return The list of pages on the stream.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Page> pages() {
		synchronized (lock) {
			if (!closed)
				return (ArrayList<Page>) pages.clone();
			return pages;
		}
	}

	/**
	 * Get the page data index.
	 * 
	 * @return The page data index
	 * @throws IOException
	 *             If the stream has not been closed, an IOException will be
	 *             thrown.
	 */
	public PageIndex index() throws IOException {
		synchronized (lock) {
			if (!closed)
				throw new IOException("Writing not finished!");
			return index;
		}
	}

	/**
	 * Get the list of cursors on the stream. If the stream has not been closed,
	 * return a shallow-copy of the cursors.
	 * 
	 * @return The list of cursors on the stream.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PageCursor> cursors() {
		synchronized (lock) {
			if (!closed)
				return (ArrayList<PageCursor>) cursors.clone();
			return cursors;
		}
	}
}
