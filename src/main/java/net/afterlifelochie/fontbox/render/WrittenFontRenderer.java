package net.afterlifelochie.fontbox.render;

import net.afterlifelochie.fontbox.FontException;
import net.afterlifelochie.fontbox.GLFontMetrics;
import net.afterlifelochie.fontbox.GLFont;
import net.afterlifelochie.fontbox.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.Line;
import net.afterlifelochie.fontbox.layout.Page;

import org.lwjgl.opengl.GL11;

/**
 * Renders a written-like typeface in game using natural writing styles.
 * 
 * @author AfterLifeLochie
 */
public class WrittenFontRenderer {

	/**
	 * Called to render a page to the screen.
	 * 
	 * @param font
	 *            The font. May not be null.
	 * @param page
	 *            The page element. May not be null.
	 * @param ox
	 *            The origin x coord for the draw.
	 * @param oy
	 *            The origin y coord for the draw.
	 * @param z
	 *            The z-depth of the draw.
	 * @param debug
	 *            If the draw is debug enabled.
	 * @throws FontException
	 *             Thrown if the font is invalid or if the font engine cannot
	 *             render using the font.
	 */
	public void renderPages(GLFont font, Page page, float ox, float oy, float z, boolean debug) throws FontException {
		if (font == null)
			throw new IllegalArgumentException("font may not be null");
		if (page == null)
			throw new IllegalArgumentException("page may not be null");
		assert ox >= 0.0f : "drawing off screen";
		assert oy >= 0.0f : "drawing off screen";
		assert z >= 0.0f : "drawing behind the z-buffer";
		float x = 0, y = 0;
		if (font.getTextureId() == -1)
			throw new FontException("Font object not loaded!");
		GLFontMetrics metric = font.getMetric();
		if (metric == null)
			throw new FontException("Font object not loaded!");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureId());

		GL11.glPushMatrix();
		GL11.glTranslatef(ox, oy, z);

		// TODO: Externalize the rendering scale
		GL11.glScalef(0.44f, 0.44f, 1.0f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if (debug) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(1.0f, 0.0f, 0.0f);
			int realPageWidth = page.width + page.properties.margin_left + page.properties.margin_right;
			drawRect(0, 0, realPageWidth, page.height, 1);
			GL11.glTranslatef(page.properties.margin_left, 0.0f, 0.0f);
			GL11.glColor3f(0.0f, 1.0f, 0.0f);
			drawRect(0, 0, page.width, page.height, 1);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}

		// Translate to the draw dest
		GL11.glTranslatef(page.properties.margin_left, 0.0f, 0.0f);

		// Start drawing
		for (Line line : page.lines) {
			x = 0; // carriage return
			for (int i = 0; i < line.line.length(); i++) {
				char c = line.line.charAt(i);
				if (c == ' ') { // is a space?
					x += line.space_size; // shunt by a space
					continue;
				}
				GLGlyphMetric mx = metric.glyphs.get((int) c);
				if (mx == null) // blank glyph?
					continue;
				// TODO: formatting?
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				double u = mx.ux / metric.fontImageWidth;
				double v = (mx.vy - mx.ascent) / metric.fontImageHeight;
				double wz = mx.width / metric.fontImageWidth;
				double hz = mx.height / metric.fontImageHeight;
				drawTexturedRectUV(x, y, mx.width, mx.height, u, v, wz, hz, 1.0);
				x += mx.width; // shunt by glpyh size
			}
			y += line.line_height; // shunt by line's height prop
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private void drawRect(double x, double y, double w, double h, double zLevel) {
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(x, y + h, zLevel);
		GL11.glVertex3d(x + w, y + h, zLevel);
		GL11.glVertex3d(x + w, y, zLevel);
		GL11.glVertex3d(x, y, zLevel);
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	private void drawTexturedRectUV(double x, double y, double w, double h, double u, double v, double us, double vs,
			double zLevel) {
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(u, v + vs);
		GL11.glVertex3d(x, y + h, zLevel);

		GL11.glTexCoord2d(u + us, v + vs);
		GL11.glVertex3d(x + w, y + h, zLevel);

		GL11.glTexCoord2d(u + us, v);
		GL11.glVertex3d(x + w, y, zLevel);

		GL11.glTexCoord2d(u, v);
		GL11.glVertex3d(x, y, zLevel);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
