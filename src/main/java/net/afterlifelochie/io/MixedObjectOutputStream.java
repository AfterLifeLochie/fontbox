package net.afterlifelochie.io;

import java.io.IOException;
import java.util.List;

public class MixedObjectOutputStream {

	/** Mutex lock. */
	protected Object lock;
	/** The underlying list */
	private List<Object> list;

	public MixedObjectOutputStream(List<Object> list) {
		lock = this;
		this.list = list;
	}

	/** Check to make sure that the stream has not been closed */
	private void ensureOpen() throws IOException {
		if (list == null)
			throw new IOException("Stream closed");
	}

	/**
	 * Ensures the stream is ready for use.
	 *
	 * @return If the stream is ready for use.
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public boolean ready() throws IOException {
		synchronized (lock) {
			ensureOpen();
			return true;
		}
	}

	/**
	 * Closes the writer.
	 */
	public void close() {
		list = null;
	}

	/**
	 * Pushes an object to the stream.
	 *
	 * @param o
	 *            The object
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public void write(Object o) throws IOException {
		synchronized (lock) {
			ensureOpen();
			list.add(o);
		}
	}

}
