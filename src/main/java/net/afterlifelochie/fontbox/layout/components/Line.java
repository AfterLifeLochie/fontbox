package net.afterlifelochie.fontbox.layout.components;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.document.formatting.TextFormat;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.font.GLFontMetrics;
import net.afterlifelochie.fontbox.font.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.afterlifelochie.fontbox.render.GLUtils;
import net.afterlifelochie.fontbox.render.RenderException;

/**
 * One formatted line with a spacing and line-height
 * 
 * @author AfterLifeLochie
 */
public class Line extends Element {
	/**
	 * The real text
	 * 
	 * @deprecated to be replaced with an object-interpolated version so that
	 *             formatting data can be streamed to the object and the
	 *             renderer, rather than using lookup-tables or checking the
	 *             formatting of each char
	 */
	public final String line;
	/** The line's ID */
	public String id;
	/** The size of the spacing between words */
	public final int space_size;
	/** 
	 * The font to render the line in 
	 * @deprecated to be obtained using {@link TextFormat} instead
	 */
	public GLFont font;

	/**
	 * Create a new line
	 * 
	 * @param line
	 *            The line's text
	 * @param bounds
	 *            The location of the line
	 * @param font
	 *            The font to draw with
	 * @param space_size
	 *            The size of the spacing between words
	 */
	public Line(String line, ObjectBounds bounds, GLFont font, int space_size) {
		this.setBounds(bounds);
		this.line = line;
		this.font = font;
		this.id = null;
		this.space_size = space_size;
	}

	/**
	 * Create a new line with an ID
	 * 
	 * @param line
	 *            The line's text
	 * @param uid
	 *            The line's ID
	 * @param bounds
	 *            The location of the line
	 * @param font
	 *            The font to draw with
	 * @param space_size
	 *            The size of the spacing between words
	 */
	public Line(String line, String uid, ObjectBounds bounds, GLFont font,
			int space_size) {
		this(line, bounds, font, space_size);
		this.id = uid;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException,
			LayoutException {
		throw new LayoutException("Cannot layout Line type; Line already laid!");
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void update() {
		/* No action required */
	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame)
			throws RenderException {
		if (font == null)
			throw new IllegalArgumentException("font may not be null");
		float x = 0, y = 0;
		if (font.getTextureId() == -1)
			throw new RenderException("Font object not loaded!");
		GLFontMetrics metric = font.getMetric();
		if (metric == null)
			throw new RenderException("Font object not loaded!");
		if (line.length() == 0)
			return;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureId());

		GL11.glPushMatrix();
		GL11.glScalef(font.getScale(), font.getScale(), 1.0f);
		GL11.glTranslatef(bounds().x, bounds().y, 0);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// Translate to the draw dest
		/*
		 * TODO: Does margin get stored in drawcoords, or do we externalize this
		 * in a property??
		 */
		// GL11.glTranslatef(page.properties.margin_left, 0.0f, 0.0f);

		// Start drawing
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == ' ') { // is a space?
				x += space_size; // shunt by a space
				continue;
			}

			GLGlyphMetric glyph = metric.glyphs.get((int) c);
			if (glyph == null) // blank glyph?
				continue;
			// TODO: formatting?
			GL11.glColor3f(0.0f, 0.0f, 0.0f);
			double u = glyph.ux / metric.fontImageWidth;
			double v = (glyph.vy - glyph.ascent) / metric.fontImageHeight;
			double wz = glyph.width / metric.fontImageWidth;
			double hz = glyph.height / metric.fontImageHeight;
			GLUtils.drawDefaultRect(x, y, glyph.width, glyph.height, 1.0);
			GLUtils.drawTexturedRectUV(x, y, glyph.width, glyph.height, u, v,
					wz, hz, 1.0);
			x += glyph.width; // shunt by glpyh size
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	@Override
	public void clicked(BookGUI gui, int mx, int my) {
		/* No action required */
	}

	@Override
	public void typed(BookGUI gui, char val, int code) {
		/* No action required */
	}

	@Override
	public String identifier() {
		return id;
	}
}