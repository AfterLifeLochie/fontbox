package net.afterlifelochie.fontbox.layout.components;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.font.GLFontMetrics;
import net.afterlifelochie.fontbox.font.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;

public class LineWriter {

	/** The writer stream */
	private final PageWriter writer;
	/** The font to write with */
	private final GLFont font;
	/** The alignment writing in */
	private final AlignmentMode alignment;
	/** The list of words on the stack currently */
	private final ArrayList<String> words;

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
	 * @param font
	 *            The font to write with.
	 * @param algnment
	 *            The alignment to paginate in.
	 */
	public LineWriter(PageWriter writer, GLFont font, AlignmentMode algnment) {
		this.writer = writer;
		this.font = font;
		this.alignment = algnment;
		words = new ArrayList<String>();
	}

	private void update() throws LayoutException, IOException {
		int width = 0, height = 0;

		GLFontMetrics metric = font.getMetric();
		Page page = writer.current();

		int wordsWidth = 0;
		for (String word : words) {
			char[] chars = word.toCharArray();
			for (char cz : chars) {
				GLGlyphMetric cm = metric.glyphs.get(cz);
				if (cm == null)
					throw new LayoutException(String.format("Cannot paginate illegal glyph %s", cz));
				wordsWidth += cm.width;
				if (cm.ascent > height)
					height = cm.ascent;
			}
		}

		int blankWidth = bounds.width - wordsWidth;
		spaceSize = page.properties.min_space_size;
		int x = 0, y = 0;

		switch (alignment) {
		case CENTER:
			float halfBlank = (float) blankWidth / 2.0f;
			x += (int) Math.floor(halfBlank);
			break;
		case JUSTIFY:
			float density = (float) wordsWidth / (float) bounds.width;
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
		bounds = new ObjectBounds(x, y, width, height, false);
	}

	/**
	 * Called to emit the stack's contents to a Line element. The contents of
	 * the stack are automatically cleared and zerored on invocation.
	 * 
	 * @return The formatted line. The stack, properties and other values
	 *         associated with generating the line are reset on the self object.
	 */
	public Line emit() {
		StringBuilder words = new StringBuilder();
		for (int i = 0; i < words.length(); i++) {
			String what = this.words.get(i);
			words.append(what);
			if (i < words.length() - 1)
				words.append(" ");
		}
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
	 * @throws IOException
	 *             Any exception which occurs when reading from the page writing
	 *             stream underlying this writer
	 * @throws LayoutException
	 *             Any exception which occurs when updating the potentially
	 *             paginated text
	 */
	public void push(String word) throws LayoutException, IOException {
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
