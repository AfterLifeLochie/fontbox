package net.afterlifelochie.fontbox.layout.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.afterlifelochie.fontbox.document.formatting.TextFormat;
import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.font.GLFontMetrics;
import net.afterlifelochie.fontbox.font.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;

public class LineWriter {

	/** The writer stream */
	private final PageWriter writer;
	/** The alignment writing in */
	private final AlignmentMode alignment;

	/** The list of words on the stack currently */
	private final ArrayList<String> words;
	/** The map of formatting objects on the stack currently */
	private final HashMap<Integer, TextFormat> format;
	/** The default formatting */
	private final TextFormat zeroFormat;

	/** The current computed bounds of the stack's words */
	private ObjectBounds bounds;
	/** The current size of the spaces between the stack's words */
	private int spaceSize;

	/**
	 * Construct a new line writing utility. The underlying stream and the
	 * writing font must be specified and cannot be null.
	 * 
	 * @param writer
	 *            The underlying stream to operate on.
	 * @param defaultFormat
	 *            The default text formatting to use.
	 * @param alignment
	 *            The alignment to paginate in.
	 */
	public LineWriter(PageWriter writer, TextFormat defaultFormat, AlignmentMode alignment) {
		this.writer = writer;
		this.alignment = alignment;
		this.zeroFormat = defaultFormat;
		words = new ArrayList<String>();
		format = new HashMap<Integer, TextFormat>();
		format.put(0, zeroFormat.clone());
	}

	private void update() throws LayoutException, IOException {
		int width = 0, height = 0;

		Page page = writer.current();
		TextFormat activeFormat = format.get(0);

		int offset = 0;
		int wordsWidth = 0;
		for (String word : words) {
			char[] chars = word.toCharArray();
			for (char cz : chars) {
				if (format.get(offset) != null)
					activeFormat = format.get(offset);
				GLGlyphMetric cm = activeFormat.font.getMetric().glyphs.get((int) cz);
				if (cm == null)
					throw new LayoutException(String.format("Glyph %s not supported by font %s.", cz,
							activeFormat.font.getName()));
				wordsWidth += cm.width;
				if (cm.ascent > height)
					height = cm.ascent;
				offset++;
			}
			offset++;
		}

		int blankWidth = page.width - page.properties.margin_left - page.properties.margin_right - wordsWidth;
		spaceSize = page.properties.min_space_size;
		int x = writer.cursor().x(), y = writer.cursor().y();

		switch (alignment) {
		case CENTER:
			float halfBlank = (float) blankWidth / 2.0f;
			x += (int) Math.floor(halfBlank);
			break;
		case JUSTIFY:
			float density = (float) wordsWidth / (float) page.width;
			if (density >= page.properties.min_line_density) {
				int extra_px_per_space = (int) Math.floor(blankWidth / words.size());
				if (extra_px_per_space > page.properties.min_space_size)
					spaceSize = extra_px_per_space;
			}
			break;
		case LEFT:
			/* Do nothing */
			break;
		case RIGHT:
			x += blankWidth;
			break;
		}

		width = wordsWidth + Math.max(words.size() - 2, 0) * spaceSize;
		bounds = new ObjectBounds(x, y, width, height, FloatMode.NONE);
	}

	/**
	 * Called to emit the stack's contents to a Line element. The contents of
	 * the stack are automatically cleared and zerored on invocation.
	 * 
	 * @param uid
	 *            The line's ID, if any
	 * 
	 * @return The formatted line. The stack, properties and other values
	 *         associated with generating the line are reset on the self object.
	 */
	public Line emit(String uid) {
		StringBuilder words = new StringBuilder();
		for (int i = 0; i < this.words.size(); i++) {
			String what = this.words.get(i);
			words.append(what);
			if (i < words.length() - 1)
				words.append(" ");
		}
		char[] glyphs = words.toString().toCharArray();
		TextFormat[] format = new TextFormat[glyphs.length];
		for (int i = 0; i < glyphs.length; i++)
			if (this.format.get(i) != null)
				format[i] = this.format.get(i);
		Line what = new Line(words.toString().toCharArray(), format, uid, bounds, spaceSize);
		bounds = null;
		spaceSize = 0;
		this.words.clear();
		this.format.clear();
		this.format.put(0, zeroFormat.clone());
		return what;
	}

	/**
	 * Get the current pending bounding box of the words on the writer.
	 * 
	 * @return The pending bounding box of the words on the writer.
	 */
	public ObjectBounds pendingBounds() {
		return bounds;
	}

	/**
	 * Pushes the word onto the writer stack. The word is placed on the end of
	 * the stack and the dimensions of the stack are recomputed automatically.
	 * 
	 * @param word
	 *            The word to place on the end of the stack
	 * @param format
	 *            The text formatting, if any
	 * @throws IOException
	 *             Any exception which occurs when reading from the page writing
	 *             stream underlying this writer
	 * @throws LayoutException
	 *             Any exception which occurs when updating the potentially
	 *             paginated text
	 */
	public void push(String word, TextFormat[] format) throws LayoutException, IOException {
		if (format != null) {
			if (word.length() != format.length)
				throw new LayoutException("Specified format doesn't bound word length");

			int offset = 0;
			for (String wd : words)
				offset += wd.length() + 1;

			for (int i = 0; i < format.length; i++) {
				TextFormat aformat = format[i];
				if (aformat != null)
					this.format.put(offset + i, aformat);
			}
		}
		words.add(word);
		update();
	}

	/**
	 * Removes the word from the end of the writer stack. The word removed is
	 * returned and then dimensions of the stack are recomputed automatically.
	 * 
	 * @return The word which was removed from the end of the stack
	 * @throws IOException
	 *             Any exception which occurs when reading from the page writing
	 *             stream underlying this writer
	 * @throws LayoutException
	 *             Any exception which occurs when updating the potentially
	 *             paginated text
	 */
	public String pop() throws LayoutException, IOException {
		String word = words.remove(words.size() - 1);

		int offset = 0;
		for (String wd : words)
			offset += wd.length() + 1;
		Iterator<Entry<Integer, TextFormat>> itx = format.entrySet().iterator();
		while (itx.hasNext()) {
			Entry<Integer, TextFormat> etx = itx.next();
			if (etx.getKey() >= offset)
				itx.remove();
		}
		if (!format.containsKey(0))
			format.put(0, zeroFormat.clone());

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
