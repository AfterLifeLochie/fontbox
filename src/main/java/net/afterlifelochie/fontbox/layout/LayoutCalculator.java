package net.afterlifelochie.fontbox.layout;

import java.io.IOException;
import java.util.ArrayList;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.font.GLFontMetrics;
import net.afterlifelochie.fontbox.font.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.components.Line;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.layout.components.PageProperties;
import net.afterlifelochie.io.StackedPushbackStringReader;

/**
 * Document pagination generator. Used to convert raw text into real page
 * objects which can be rendered or manipulated.
 * 
 * @deprecated Replaced by {@link DocumentProcessor} instead. It delegates the
 *             transformation of pages now, so this class is only here until
 *             other functionals (such as clicking, etc) are moved.
 * 
 * @author AfterLifeLochie
 *
 */
public class LayoutCalculator {

	/**
	 * Get the {@link net.afterlifelochie.fontbox.layout.components.Line} on the
	 * {@link net.afterlifelochie.fontbox.layout.components.Page}
	 * 
	 * @param page
	 *            the given page
	 * @param font
	 *            the used font to write
	 * @param offset
	 *            the yPos
	 * @return a line or null if offset is not on a line
	 */
	public static Line getLine(Page page, GLFont font, float offset) {
		/*if (offset < 0)
			return null;
		for (Line line : page.lines)
			if ((offset -= line.height * font.getScale()) < 0)
				return line;*/
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
		Line line = getLine(page, font, offsetY);
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
				offsetX -= line.space_size * font.getScale();
				word = "";
				continue;
			}
			GLGlyphMetric mx = font.getMetric().glyphs.get((int) c);
			if (mx == null)
				continue;
			offsetX -= mx.width * font.getScale();
		}
		return null;
	}

	private LayoutCalculator() {
		/* Not instantiable */
	}
}
