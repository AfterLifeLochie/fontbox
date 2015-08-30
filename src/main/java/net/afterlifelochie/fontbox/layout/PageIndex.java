package net.afterlifelochie.fontbox.layout;

import java.util.HashMap;

/**
 * Dynamic page index container. Internally, contains a map of all known labels
 * and the associated place-in-page page numbers for the label.
 * 
 * @author AfterLifeLochie
 *
 */
public class PageIndex {

	private final HashMap<String, Integer> ids;

	/** Default constructor */
	public PageIndex() {
		ids = new HashMap<String, Integer>();
	}

	/**
	 * Push an index into memory. If the index already exists, the index is
	 * overwritten immediately and without warning.
	 * 
	 * @param id
	 *            The label
	 * @param page
	 *            The page number
	 */
	public void push(String id, int page) {
		ids.put(id, page);
	}

	/**
	 * <p>
	 * Request the page-number from an identifying label value. The dictionary
	 * automatically determines if the index has been seen.
	 * </p>
	 * <p>
	 * If the index value has not been seen, <code>-1</code> is returned; else,
	 * the first page is zero (<code>0</code>) and so on.
	 * </p>
	 * 
	 * @param id
	 *            The identifier label
	 * @return The page index, or <code>-1</code> if the label is unknown
	 */
	public int find(String id) {
		return (ids.containsKey(id)) ? ids.get(id) : -1;
	}

}
