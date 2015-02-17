package net.afterlifelochie.fontbox.layout;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.FontException;
import net.afterlifelochie.fontbox.GLFont;
import net.afterlifelochie.fontbox.GLFontMetrics;
import net.afterlifelochie.fontbox.GLGlyphMetric;
import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.io.StackedPushbackStringReader;

/**
 * Document pagination generator. Used to convert raw text into real page
 * objects which can be rendered or manipulated.
 * 
 * @author AfterLifeLochie
 *
 */
public class LayoutCalculator {
	/**
	 * Attempt to box a line or part of a line onto a PageBox. This immediately
	 * attempts to fit as much of the line onto a LineBox and then glues it to
	 * the tail of a PageBox if the PageBox can support the addition of a line.
	 * Any overflow text which cannot be boxed onto the page is returned.
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param metric
	 *            The font metric to calculate with
	 * @param text
	 *            The text stream
	 * @param page
	 *            The page to box onto
	 * @return If a page overflow occurs - that is, if there is no more
	 *         available vertical space for lines to occupy.
	 * @throws IOException
	 *             Any exception which occurs when performing operations on the
	 *             text stream
	 * @throws FontException
	 *             Any exception which occurs when placing the line onto the
	 *             page.
	 */
	public static boolean boxLine(ITracer trace, GLFontMetrics metric, StackedPushbackStringReader text, Page page)
			throws IOException, FontException {
		// Calculate some required properties
		int effectiveWidth = page.width - page.properties.margin_left - page.properties.margin_right;
		int effectiveHeight = page.getFreeHeight();

		int width_new_line = 0, width_new_word = 0;
		// Start globbing characters
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<Character> chars = new ArrayList<Character>();
		// Push our place in case we have to abort
		text.pushPosition();
		while (text.available() > 0) {
			// Take a char
			char c = text.next();
			// Treat space as a word separator
			if (c == '\r' || c == '\n') {
				// Look and see if the next character is a newline
				char c1 = text.next();
				if (c1 != '\n' && c1 != '\r')
					text.rewind(1); // put it back, aha!
				break; // hard eol
			} else if (c == ' ') {
				// Push a whole word if one exists
				if (chars.size() > 0) {
					// Find out if there is enough space to push this word
					int new_width_nl = width_new_line + width_new_word + page.properties.min_space_size;
					if (effectiveWidth >= new_width_nl) {
						// Yes, there is enough space, add the word
						width_new_line += width_new_word;
						StringBuilder builder = new StringBuilder();
						for (char c1 : chars)
							builder.append(c1);
						words.add(builder.toString());
						trace.trace("LayoutCalculator.boxLine", "pushWord", builder.toString());
						// Clear the character buffers
						chars.clear();
						width_new_word = 0;
					} else {
						// No, the word doesn't fit, back it up
						trace.trace("LayoutCalculator.boxLine", "revertWord", width_new_word);
						text.rewind(chars.size() + 1);
						chars.clear();
						width_new_word = 0;
						break;
					}
				}
			} else {
				GLGlyphMetric mx = metric.glyphs.get((int) c);
				if (mx != null) {
					trace.trace("LayoutCalculator.boxLine", "pushChar", c, mx);
					width_new_word += mx.width;
					chars.add(c);
				} else {
					trace.trace("LayoutCalculator.boxLine", "badChar", c);
					throw new FontException("Unable to render glyph " + c);
				}
			}
		}

		// Anything left on buffer?
		if (chars.size() > 0) {
			// Find out if there is enough space to push this word
			int new_width_nl = width_new_line + width_new_word + page.properties.min_space_size;
			if (effectiveWidth >= new_width_nl) {
				// Yes, there is enough space, add the word
				width_new_line += width_new_word;
				StringBuilder builder = new StringBuilder();
				for (char c1 : chars)
					builder.append(c1);
				trace.trace("LayoutCalculator.boxLine", "pushOverflow", builder.toString());
				words.add(builder.toString());
				// Clear the character buffers
				chars.clear();
				width_new_word = 0;
			} else {
				// No, the word doesn't fit, back it up
				trace.trace("LayoutCalculator.boxLine", "clearOverflow", width_new_word);
				chars.clear();
			}
		}

		// Find the maximum height of any characters in the line
		int height_new_line = page.properties.lineheight_size;
		for (String word : words) {
			for (int j = 0; j < word.length(); j++) {
				char c = word.charAt(j);
				if (c != ' ') {
					GLGlyphMetric mx = metric.glyphs.get((int) c);
					if (mx.height > height_new_line)
						height_new_line = mx.height;
				}
			}
		}

		// If the line doesn't fit at all, we can't do anything
		if (height_new_line > effectiveHeight) {
			trace.trace("LayoutCalculator.boxLine", "revertLine", height_new_line);
			text.popPosition(); // back out
			return true;
		}

		// Commit our position as we have now read a line and it fits all
		// current constraints on the page
		text.commitPosition();

		// Glue the whole line together
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < words.size(); i++) {
			line.append(words.get(i));
			if (i != words.size() - 1)
				line.append(" ");
		}

		// Figure out how much space is left over from the line
		int space_remain = effectiveWidth - width_new_line;
		int space_width = page.properties.min_space_size;

		// If the line is not blank, then...
		if (words.size() > 0) {
			int extra_px_per_space = (int) Math.floor(space_remain / words.size());
			if (width_new_line > extra_px_per_space)
				space_width = page.properties.min_space_size + extra_px_per_space;
		} else
			height_new_line = 2 * page.properties.lineheight_size;

		// Make the line height fit exactly 1 or more line units
		int line_height = height_new_line;
		if (line_height % page.properties.lineheight_size != 0) {
			line_height += line_height % page.properties.lineheight_size;
			// line_height += page.lineheight_size;
		}

		// Create the linebox
		trace.trace("LayoutCalculator.boxLine", "pushLine", line.toString(), space_width, line_height);
		page.lines.add(new Line(line.toString(), space_width, line_height));
		return false;
	}

	/**
	 * Attempt to box a paragraph or part of a paragraph onto a collection of
	 * PageBox instances.
	 * 
	 * @param trace
	 *            The debugging trace object
	 * @param metric
	 *            The font metric to calculate with
	 * @param text
	 *            The text blob
	 * @param props
	 *            The page layout properties
	 * @return The page results
	 * @throws IOException
	 *             Any exception which occurs when performing operations on the
	 *             text stream
	 * @throws FontException
	 *             Any exception which occurs when placing the line onto the
	 *             page.
	 */
	public static Page[] boxParagraph(ITracer trace, GLFontMetrics metric, String text, PageProperties props)
			throws IOException, FontException {
		StackedPushbackStringReader reader = new StackedPushbackStringReader(text);
		ArrayList<Page> pages = new ArrayList<Page>();
		Page currentPage = new Page(props.copy());
		boolean flag = false;
		while (reader.available() > 0) {
			trace.trace("LayoutCalculator.boxParagraph", "boxStream", reader, reader.available());
			flag = boxLine(trace, metric, reader, currentPage);
			trace.trace("LayoutCalculator.boxParagraph", "boxResult", reader, flag);
			if (flag) {
				trace.trace("LayoutCalculator.boxParagraph", "pushPage", currentPage);
				pages.add(currentPage);
				currentPage = new Page(props.copy());
			}
		}
		if (!flag) {
			trace.trace("LayoutCalculator.boxParagraph", "pushPage");
			pages.add(currentPage);
		}
		Page[] result = pages.toArray(new Page[0]);
		trace.trace("LayoutCalculator.boxParagraph", result);
		return result;
	}

	/**
	 * Attempt to box a paragraph or part of a paragraph onto a collection of
	 * PageBox instances.
	 * 
	 * @param trace
	 *            The debugging trace object
	 * @param font
	 *            The font to calculate with
	 * @param text
	 *            The text blob
	 * @param props
	 *            The page layout properties
	 * @return The page results
	 * @throws IOException
	 *             Any exception which occurs when performing operations on the
	 *             text stream
	 * @throws FontException
	 *             Any exception which occurs when placing the line onto the
	 *             page.
	 */
	public static Page[] boxParagraph(ITracer trace, GLFont font, String text, PageProperties props)
			throws IOException, FontException {
		return boxParagraph(trace, font.getMetric(), text, props);
	}

	// TODO: Externalize the rendering scale to the font
	private static final float scale = 0.44F;

	/**
	 * Get the {@link net.afterlifelochie.fontbox.layout.Line} on the
	 * {@link net.afterlifelochie.fontbox.layout.Page}
	 * 
	 * @param page
	 *            the given page
	 * @param offset
	 *            the yPos
	 * @return a line or null if offset is not on a line
	 */
	public static Line getLine(Page page, float offset) {
		if (offset < 0)
			return null;
		for (Line line : page.lines)
			if ((offset -= line.line_height * scale) < 0)
				return line;
		return null;
	}

	/**
	 * Get the word at on the {@link Page} written in given {@link GLFont}
	 * 
	 * @param page
	 *            the page to work with
	 * @param font
	 *            the used font to write
	 * @param offsetX
	 *            the xPos
	 * @param offsetY
	 *            the yPos
	 * @return the word clicked on or null if no word was there
	 */
	public static String getWord(Page page, GLFont font, float offsetX, float offsetY) {
		Line line = getLine(page, offsetY);
		if (line == null)
			return null;
		String word = "";
		for (int i = 0; i < line.line.length(); i++) {
			if (offsetX < 0) {
				if (word.isEmpty())
					return null;
				for (; i < line.line.length(); i++) {
					char c = line.line.charAt(i);
					if (c == ' ')
						break;
					word += c;
				}
				return word;
			}
			char c = line.line.charAt(i);
			word += c;
			if (c == ' ') {
				offsetX -= line.space_size * scale;
				word = "";
				continue;
			}
			GLGlyphMetric mx = font.getMetric().glyphs.get((int) c);
			if (mx == null)
				continue;
			offsetX -= mx.width * scale;
		}
		return null;
	}

	private LayoutCalculator() {
		/* Not instantiable */
	}
}
