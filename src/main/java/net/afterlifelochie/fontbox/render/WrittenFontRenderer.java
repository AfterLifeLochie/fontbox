package net.afterlifelochie.fontbox.render;

import net.afterlifelochie.fontbox.GLFontMetrics;
import net.afterlifelochie.fontbox.GLFont;
import net.afterlifelochie.fontbox.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.LineBox;
import net.afterlifelochie.fontbox.layout.PageBox;
import net.minecraft.client.Minecraft;

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
	 *            The font.
	 * @param buffer
	 *            The font render buffer.
	 * @param page
	 *            The page element.
	 * @param ox
	 *            The origin x coord for the draw.
	 * @param oy
	 *            The origin y coord for the draw.
	 * @param z
	 *            The z-depth of the draw.
	 * @param debug
	 *            If the draw is debug enabled.
	 */
	public void renderPages(GLFont font, FontRenderBuffer buffer, PageBox page, float ox, float oy, float z,
			boolean debug) {
		float x = 0, y = 0;
		GLFontMetrics metric = font.getMetric();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureId());
		GL11.glPushMatrix();
		GL11.glTranslatef(ox, oy, z);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// Translate to the draw dest
		for (LineBox line : page.lines) {
			x = 0; // carriage return
			for (int i = 0; i < line.line.length(); i++) {
				char c = line.line.charAt(i);
				if (c == ' ') // is a space?
					x += line.space_size; // shunt by a space
				GLGlyphMetric mx = metric.glyphs.get((int) c);
				if (mx == null) // blank glyph?
					continue;
				GL11.glPushMatrix();
				GL11.glTranslatef(x * 0.45f, y * 0.45f, 0.0f);
				GL11.glColor3f(1.0f, 0.0f, 0.0f);
				buffer.callCharacter((int) c);
				GL11.glPopMatrix();
				x += mx.width; // shunt by glpyh size
			}
			y += line.line_height; // shunt by line's height prop
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

	}
}
