package net.afterlifelochie.fontbox.layout.components;

import java.io.IOException;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.document.formatting.DecorationStyle;
import net.afterlifelochie.fontbox.document.formatting.TextFormat;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.font.GLFontMetrics;
import net.afterlifelochie.fontbox.font.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.afterlifelochie.fontbox.render.RenderException;

import org.lwjgl.opengl.GL11;

/**
 * One formatted line with a spacing and line-height
 *
 * @author AfterLifeLochie
 */
public class Line extends Element {
	/** The characters */
	public final char[] line;
	/** The character formats */
	public final TextFormat[] format;

	/** The line's ID */
	public String id;
	/** The size of the spacing between words */
	public final int space_size;

	/**
	 * Create a new line
	 *
	 * @param line
	 *            The line's text
	 * @param format
	 *            The text format map
	 * @param bounds
	 *            The location of the line
	 * @param space_size
	 *            The size of the spacing between words
	 */
	public Line(char[] line, TextFormat[] format, ObjectBounds bounds, int space_size) {
		setBounds(bounds);
		this.line = line;
		this.format = format;
		id = null;
		this.space_size = space_size;
	}

	/**
	 * Create a new line with an ID
	 *
	 * @param line
	 *            The line's text
	 * @param format
	 *            The text format map
	 * @param uid
	 *            The line's ID
	 * @param bounds
	 *            The location of the line
	 * @param space_size
	 *            The size of the spacing between words
	 */
	public Line(char[] line, TextFormat[] format, String uid, ObjectBounds bounds, int space_size) {
		this(line, format, bounds, space_size);
		id = uid;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
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
	public boolean canCompileRender() {
		return true;
	}

	private void safeSwitchToFont(GLFont font) throws RenderException {
		if (font.getTextureId() == -1)
			throw new RenderException("Font object not loaded!");
		GLFontMetrics metric = font.getMetric();
		if (metric == null)
			throw new RenderException("Font object not loaded!");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureId());
		GL11.glScalef(font.getScale(), font.getScale(), 1.0f);
	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame) throws RenderException {
		float x = 0, y = 0;
		if (line.length == 0)
			return;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		TextFormat decorator = format[0];
		GL11.glPushMatrix();
		safeSwitchToFont(decorator.font);
		GL11.glTranslatef(bounds().x, bounds().y, 0);

		for (int i = 0; i < line.length; i++) {
			char c = line[i];
			if (c != ' ') {
				TextFormat aDecorator = format[i];
				if (aDecorator != null && !aDecorator.equals(decorator)) {
					if (aDecorator.font != decorator.font) {
						GL11.glPopMatrix();
						GL11.glPushMatrix();
						safeSwitchToFont(aDecorator.font);
						GL11.glTranslatef(bounds().x, bounds().y, 0);
					}
					decorator = aDecorator;
				}

				GLFontMetrics metric = decorator.font.getMetric();
				GLGlyphMetric glyph = metric.glyphs.get((int) c);
				if (glyph == null) // blank glyph?
					continue;

				if (decorator.color == null)
					GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
				else
					GL11.glColor4f(decorator.color.redF(), decorator.color.greenF(), decorator.color.blueF(),
							decorator.color.alphaF());

				float tiltTop = 0.0f, tiltBottom = 0.0f;
				if (decorator.decorations.contains(DecorationStyle.ITALIC)) {
					tiltTop = -5.55f;
					tiltBottom = 5.55f;
				}

				renderGlyphInPlace(metric, glyph, x, y, tiltTop, tiltBottom);
				if (decorator.decorations.contains(DecorationStyle.BOLD))
					renderGlyphInPlace(metric, glyph, x + 0.5f, y + 0.5f, tiltTop, tiltBottom);

				x += glyph.width;
			} else
				x += space_size;
		}

		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private void renderGlyphInPlace(GLFontMetrics metric, GLGlyphMetric glyph, float x, float y, float tiltTop,
			float tiltBottom) {
		double u = glyph.ux / metric.fontImageWidth;
		double v = (glyph.vy - glyph.ascent) / metric.fontImageHeight;
		double us = glyph.width / metric.fontImageWidth;
		double vs = glyph.height / metric.fontImageHeight;
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2d(u, v + vs);
		GL11.glVertex3d(x + tiltTop, y + glyph.height, 1.0);

		GL11.glTexCoord2d(u + us, v + vs);
		GL11.glVertex3d(x + tiltTop + glyph.width, y + glyph.height, 1.0);

		GL11.glTexCoord2d(u + us, v);
		GL11.glVertex3d(x + tiltBottom + glyph.width, y, 1.0);
		GL11.glTexCoord2d(u, v);
		GL11.glVertex3d(x + tiltBottom, y, 1.0);
		GL11.glEnd();
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