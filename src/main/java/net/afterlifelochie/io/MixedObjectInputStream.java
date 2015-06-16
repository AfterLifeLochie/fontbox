package net.afterlifelochie.io;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class MixedObjectInputStream {
	/** Mutex lock. */
	protected Object lock;
	/** The underlying list */
	private List<Object> list;
	/** Pushback history stack */
	private Stack<Integer> pushback;
	/** Top of stream pointer */
	private int next = 0;

	public MixedObjectInputStream(List<Object> list) {
		this.lock = this;
		this.list = list;
		this.pushback = new Stack<Integer>();
	}

	/** Check to make sure that the stream has not been closed */
	private void ensureOpen() throws IOException {
		if (list == null)
			throw new IOException("Stream closed");
	}

	/**
	 * Get the next object on the stream, or null if no objects are remaining on
	 * the stream.
	 * 
	 * @return An object or null
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public <T> T next() throws IOException {
		synchronized (lock) {
			ensureOpen();
			if (next >= list.size())
				return null;
			return (T) list.get(next++);
		}
	}

	/**
	 * Pushes the current position onto the stack. If the stack is full, a
	 * pushback overflow will be returned.
	 * 
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public void pushPosition() throws IOException {
		synchronized (lock) {
			ensureOpen();
			if (this.pushback.size() > 64)
				throw new IOException("Pusback overflow!");
			this.pushback.push(next);
		}
	}

	/**
	 * Pops the previous position off the stack. If the stack is empty, a
	 * pushback underflow will be returned.
	 * 
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public void popPosition() throws IOException {
		synchronized (lock) {
			ensureOpen();
			if (this.pushback.size() == 0)
				throw new IOException("Pushback underflow!");
			this.next = this.pushback.pop();
		}
	}

	/**
	 * Returns the current position of the reader.
	 * 
	 * @return The current position of the reader.
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public int getPosition() throws IOException {
		synchronized (lock) {
			ensureOpen();
			return this.next;
		}
	}

	/**
	 * Sets the position of the reader.
	 * 
	 * @param ns
	 *            A new position.
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public void setPosition(int ns) throws IOException {
		synchronized (lock) {
			ensureOpen();
			this.next = ns;
		}
	}

	/**
	 * Commits the current position of the reader. This pops the previous return
	 * position without restoring the pointer.
	 * 
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public void commitPosition() throws IOException {
		synchronized (lock) {
			ensureOpen();
			this.pushback.pop();
		}
	}

	/**
	 * Skips forward a number of objects.
	 * 
	 * @param ns
	 *            How far forward to skip.
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public void skip(long ns) throws IOException {
		synchronized (lock) {
			ensureOpen();
			next += ns;
			if (0 > next)
				next = 0;
			if (next > list.size())
				next = list.size();
		}
	}

	/**
	 * Skips backwards a number of objects.
	 * 
	 * @param ns
	 *            How far backwards to skip.
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public void rewind(long ns) throws IOException {
		synchronized (lock) {
			ensureOpen();
			next -= ns;
			if (0 > next)
				next = 0;
			if (next > list.size())
				next = list.size();
		}
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
	 * Determine the number of objects remaining in the read collection.
	 * 
	 * @return The number of objects waiting to be read.
	 * @throws IOException
	 *             If the lock cannot be obtained or if the stream is not open,
	 *             an IOException will be thrown.
	 */
	public int available() throws IOException {
		synchronized (lock) {
			ensureOpen();
			return Math.max(0, list.size() - next);
		}
	}

	/**
	 * Closes the reader.
	 */
	public void close() {
		list = null;
	}
}
