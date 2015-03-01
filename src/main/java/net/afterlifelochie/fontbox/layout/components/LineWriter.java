package net.afterlifelochie.fontbox.layout.components;

import java.util.ArrayList;

import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;

public class LineWriter {

	/** The writer stream */
	private final PageWriter writer;
	/** The font to write with */
	private final GLFont font;
	/** The list of words on the stack currently */
	private final ArrayList<String> words;

	/** The current computed bounds of the stack's words */
	private ObjectBounds bounds;
	/** The current size of the spaces between the stack's words */
	private int spaceSize;

	/**
	 * Construct a new line writing utility. The underlying stream and the writing
	 * font must be specified and cannot be null.
	 * @param writer The underlying stream to operate on.
	 * @param font The font to write with.
	 */
	public LineWriter(PageWriter writer, GLFont font) {
		this.writer = writer;
		this.font = font;
		words = new ArrayList<String>();
	}

	
	private void update() {
	}

	/**
	 * Called to emit the stack's contents to a Line element. The contents of the stack
	 *  are automatically cleared and zerored on invocation. 
	 * @return The formatted line. The stack, properties and other values associated with generating
	 * the line are reset on the self object.
	 */
	public Line emit() {
		StringBuilder words = new StringBuilder();
		for (int i = 0; i < words.length(); i++) {
			String what = this.words.get(i);
			words.append(what);
			if (i < words.length() - 1)
				words.append(" ");
		}
		/*
		 * TODO: store properties on in the container so we don't need to
		 * recompute them.
		 */
		Line what = new Line(words.toString(), bounds, font, spaceSize);
		bounds = null;
		spaceSize = 0;
		this.words.clear();
		return what;
	}

	/**
	 * Pushes the word onto the writer stack. The word is placed on the end of
	 * the stack and the dimensions of the stack are recomputed automatically.
	 * 
	 * @param word
	 *            The word to place on the end of the stack
	 */
	public void push(String word) {
		words.add(word);
		update();
	}

	/**
	 * Removes the word from the end of the writer stack. The word removed is
	 * returned and then dimenions of the stack are recomputed automatically.
	 * 
	 * @return The word which was removed from the end of the stack
	 */
	public String pop() {
		String word = words.remove(words.size() - 1);
		update();
		return word;
	}

	/**
	 * Get the size (number of elements) on the stack at the current time.
	 * 
	 * @return The number of elements currently on the writer stack at the time
	 *         of invocation.
	 */
	public int size() {
		return words.size();
	}

}
