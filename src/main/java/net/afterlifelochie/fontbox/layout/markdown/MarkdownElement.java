package net.afterlifelochie.fontbox.layout.markdown;

import java.util.ArrayList;

public class MarkdownElement {
	public String type;
	public String label;
	public String blob;
	public ArrayList<MarkdownElement> children = new ArrayList<MarkdownElement>();

	public MarkdownElement(String t, String l, String b) {
		this.type = t;
		this.label = l;
		this.blob = b;
	}
}
