package net.afterlifelochie.fontbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class GLFont {

	private static char MIN_CH = '\u0000';
	private static final char MAX_CH = '\u00ff';

	/**
	 * Create a GLFont from a TTF file
	 * 
	 * @param ttf
	 *            The TTF file
	 * @return The GLFont result
	 * @throws FontException
	 *             Any exception which occurs when reading the TTF file, brewing
	 *             the buffer or creating the final font.
	 */
	public static GLFont fromTTF(ResourceLocation ttf) throws FontException {
		try {
			IResource metricResource = Minecraft.getMinecraft().getResourceManager().getResource(ttf);
			InputStream stream = metricResource.getInputStream();
			if (stream == null)
				throw new IOException("Could not open TTF file.");
			Font sysfont = Font.createFont(Font.TRUETYPE_FONT, stream);
			Font dfont = sysfont.deriveFont(Font.PLAIN, 200);
			return fromFont(dfont);
		} catch (IOException ioex) {
			throw new FontException("Can't perform I/O operation!", ioex);
		} catch (FontFormatException ffe) {
			throw new FontException("Invalid TTF file!", ffe);
		}
	}

	public static GLFont fromFont(Font font) throws FontException {
		BufferedImage buffer = new BufferedImage(800, 600, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = (Graphics2D) buffer.getGraphics();
		graphics.setFont(font);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int off = 0;
		int charsPerRow = 800 / 16;
		for (char k = MIN_CH; k <= MAX_CH; k++, off++) {
			TextLayout layout = new TextLayout(String.valueOf(k), font, graphics.getFontRenderContext());
			Rectangle2D rect = layout.getBounds();
			int x = (off % charsPerRow) * 16;
			int y = (off / charsPerRow) * 16;
			float cx = 1 - (float) rect.getX();
			float cy = 800 - graphics.getFontMetrics().getDescent() - 1;
			graphics.setColor(Color.WHITE);
			layout.draw(graphics, x + cx, y + cy);
		}
		return fromBuffer(buffer,
				GLFontMetrics.fromFontMetrics(graphics.getFontMetrics(), 800, 600, charsPerRow, MIN_CH, MAX_CH));
	}

	public static GLFont fromBuffer(BufferedImage image, GLFontMetrics metric) throws FontException {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		buffer.flip();

		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buffer);
		tmp.rewind();
		int texid = tmp.get(0);

		return new GLFont(texid, metric);
	}

	private final int textureId;
	private final GLFontMetrics metric;

	private GLFont(int textureId, GLFontMetrics metric) {
		this.textureId = textureId;
		this.metric = metric;
	}

	public int getTextureId() {
		return textureId;
	}

	public GLFontMetrics getMetric() {
		return metric;
	}
}
