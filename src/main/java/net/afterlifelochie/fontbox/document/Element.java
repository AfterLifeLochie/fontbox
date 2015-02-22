package net.afterlifelochie.fontbox.document;

import java.io.IOException;
import java.util.ArrayList;

import javax.print.attribute.standard.PageRanges;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.font.GLFontMetrics;
import net.afterlifelochie.fontbox.font.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.PageWriterCursor;
import net.afterlifelochie.fontbox.layout.components.Line;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.io.StackedPushbackStringReader;

public abstract class Element {

	private ObjectBounds bounds;

	public ObjectBounds bounds() {
		return this.bounds;
	}

	public void setBounds(ObjectBounds bb) {
		this.bounds = bb;
	}

	public abstract void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException;

	/**
	 * <p>
	 * Attempt to box text from a string onto as many pages as is required. The
	 * text provided will be added to the tail of the current page and will
	 * overflow onto any subsequent pages as is required.
	 * </p>
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param writer
	 *            The page writer
	 * @param font
	 *            The font to write with
	 * @param what
	 *            The text to write
	 * @throws IOException
	 *             Any exception which occurs when reading from the text stream
	 * @throws LayoutException
	 *             Any layout problem which prevents the text from being laid
	 *             out correctly
	 */
	protected void boxText(ITracer trace, PageWriter writer, GLFont font, String what) throws IOException,
			LayoutException {
		StackedPushbackStringReader reader = new StackedPushbackStringReader(what);
		while (reader.available() > 0) {
			Page current = writer.current();
			PageWriterCursor cursor = writer.cursor();
			ObjectBounds bounds = new ObjectBounds(cursor.x, cursor.y, current.properties.width - cursor.x,
					current.properties.height - cursor.y, false);
			Line[] blobs = boxText(trace, current, bounds, font, reader);
			for (int i = 0; i < blobs.length; i++)
				current.elements.add(blobs[i]);
			if (reader.available() > 0)
				writer.next();
		}
	}

	/**
	 * <p>
	 * Attempt to box text from a specified stream onto the page in the bounds
	 * specified. The text on the stream will be read and written into Line[]
	 * objects such that either:
	 * </p>
	 * <p>
	 * <ul>
	 * <li>The stream becomes empty; the cursor on the stream is placed at the
	 * end.</li>
	 * <li>No further text can be fit into the region specified by the bounds;
	 * the cursor on the stream is placed at the end of the last full word
	 * written onto the region specified.</li>
	 * </ul>
	 * </p>
	 *
	 * @param trace
	 *            The debugging tracer object
	 * @param page
	 *            The page being write onto
	 * @param bounds
	 *            The bounding box to write inside
	 * @param font
	 *            The font to write with
	 * @param text
	 *            The text stream to read from
	 * @throws IOException
	 *             Any exception which occurs when reading from the text stream
	 * @throws LayoutException
	 *             Any layout problem which prevents the text from being laid
	 *             out correctly
	 * @return The list of lines written to the page inside the bounding box
	 */
	protected Line[] boxText(ITracer trace, Page page, ObjectBounds bounds, GLFont font,
			StackedPushbackStringReader text) throws IOException, LayoutException {
		GLFontMetrics metric = font.getMetric();
		// The list of lines
		ArrayList<Line> lines = new ArrayList<Line>();
		// The height cursor
		int y = 0;
		while (text.available() > 0) {
			// Create some placeholder counters
			int width_new_line = 0, width_new_word = 0;
			// Start globbing characters
			ArrayList<String> words = new ArrayList<String>();
			ArrayList<Character> chars = new ArrayList<Character>();

			// Push our place in case we have to abort
			text.pushPosition();
			// Something on the stream?
			while (text.available() > 0) {
				// Take a char
				char c = text.next();
				/*
				 * Treat space as a word separator: If we find \r\n, \n\r (why?)
				 * or \r or \n on it's own, look at what is next. If we find a
				 * space, look at what's on the buffer to see if it can fit.
				 */
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
						/*
						 * FIXME: This needs to be done in a better way. We need
						 * to check to see if this object intersects ANY object,
						 * not just off the page. It would also be useful to
						 * make it check the Container rather than a Page
						 * because more complex Containers would rely on this
						 * behaviour (text boxes anyone?).
						 */
						if (bounds.width >= new_width_nl) {
							// Yes, there is enough space, add the word
							width_new_line += width_new_word;
							StringBuilder builder = new StringBuilder();
							for (char c1 : chars)
								builder.append(c1);
							words.add(builder.toString());
							trace.trace("Element.boxText", "pushWord", builder.toString());
							// Clear the character buffers
							chars.clear();
							width_new_word = 0;
						} else {
							// No, the word doesn't fit, back it up
							trace.trace("Element.boxText", "revertWord", width_new_word);
							text.rewind(chars.size() + 1);
							chars.clear();
							width_new_word = 0;
							break;
						}
					}
				} else {
					// We have something not a blank char, find it
					GLGlyphMetric mx = metric.glyphs.get((int) c);
					if (mx != null) {
						trace.trace("Element.boxText", "pushChar", c, mx);
						// Record the glyph
						width_new_word += mx.width;
						// Push the character on the stack
						chars.add(c);
					} else {
						trace.trace("Element.boxText", "badChar", c);
						throw new LayoutException("Unable to configure glyph " + c);
					}
				}
			}

			// Anything left on buffer (ie, no trailing space)?
			if (chars.size() > 0) {
				// Find out if there is enough space to push this word
				int new_width_nl = width_new_line + width_new_word + page.properties.min_space_size;
				if (bounds.width >= new_width_nl) {
					// Yes, there is enough space, add the word
					width_new_line += width_new_word;
					StringBuilder builder = new StringBuilder();
					for (char c1 : chars)
						builder.append(c1);
					trace.trace("Element.boxText", "pushOverflow", builder.toString());
					words.add(builder.toString());
					// Clear the character buffers
					chars.clear();
					width_new_word = 0;
				} else {
					// No, the word doesn't fit, back it up
					trace.trace("Element.boxText", "clearOverflow", width_new_word);
					text.rewind(chars.size() + 1);
					chars.clear();
					width_new_word = 0;
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
			if (y + height_new_line > bounds.height) {
				trace.trace("LayoutCalculator.boxLine", "revertLine", height_new_line);
				text.popPosition(); // back out
				break; // break main
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
			int space_remain = bounds.width - width_new_line;
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

			// Really compute the width of the line
			int real_width = width_new_line + (space_width * (words.size() - 1));

			// Create the linebox
			trace.trace("LayoutCalculator.boxLine", "pushLine", line.toString(), space_width, line_height);
			lines.add(new Line(line.toString(), new ObjectBounds(bounds.x, y, real_width, line_height, false), font,
					space_width));

			// Slide downwards
			y += line_height;
		}
		// Return what we've done
		return (Line[]) lines.toArray(new Line[0]);
	}

}
