package net.afterlifelochie.fontbox.layout;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.layout.components.PageProperties;

public class PageWriter {

	private ArrayList<Page> pages = new ArrayList<Page>();
	private ArrayList<PageCursor> cursors = new ArrayList<PageCursor>();
	private PageProperties attributes;
	private Object lock = new Object();
	private boolean closed = false;
	private int ptr = 0;

	public PageWriter(PageProperties attributes) {
		this.attributes = attributes;
	}

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

	public Page previous() throws IOException {
		synchronized (lock) {
			checkOpen();
			seek(-1);
			return pages.get(ptr);
		}
	}

	public Page next() throws IOException {
		synchronized (lock) {
			checkOpen();
			seek(1);
			return pages.get(ptr);
		}
	}

	public Page current() throws IOException {
		synchronized (lock) {
			checkOpen();
			seek(0);
			return pages.get(ptr);
		}
	}

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
			what.push(element);

			PageCursor current = cursor();
			for (Element e : what.elements()) {
				if (e.bounds().floating())
					continue;
				ObjectBounds bb = e.bounds();
				if (bb.y + bb.height > current.y())
					current.top(bb.y + bb.height);
			}
			for (Element e : what.elements()) {
				ObjectBounds bb = e.bounds();
				if (bb.y >= current.y() && current.y() >= bb.y + bb.height)
					if (bb.floating()) {
						
					}
			}
			Fontbox.tracer().trace("PageWriter.write", "pushCursor", current);

			return true;
		}
	}

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

	public ArrayList<Page> pages() {
		synchronized (lock) {
			if (!closed)
				return (ArrayList<Page>) pages.clone();
			return pages;
		}
	}

	public ArrayList<PageCursor> cursors() {
		synchronized (lock) {
			if (!closed)
				return (ArrayList<PageCursor>) cursors.clone();
			return cursors;
		}
	}
}
