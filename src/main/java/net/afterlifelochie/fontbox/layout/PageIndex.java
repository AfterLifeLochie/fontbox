package net.afterlifelochie.fontbox.layout;

import java.util.HashMap;

public class PageIndex {

	private final HashMap<String, Integer> ids;

	public PageIndex() {
		this.ids = new HashMap<String, Integer>();
	}

	public void push(String id, int page) {
		ids.put(id, page);
	}

	public int find(String id) {
		return (ids.containsKey(id)) ? ids.get(id) : -1;
	}

}
