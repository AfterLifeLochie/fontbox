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

	private LayoutCalculator() {
		/* Not instantiable */
	}
}
