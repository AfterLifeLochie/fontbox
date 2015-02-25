package net.afterlifelochie.fontbox.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.api.ITracer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a Font object for OpenGL.
 * 
 * @author AfterLifeLochie
 *
 */
public class GLFont {

	private static char MIN_CH = '\u0000';
	private static final char MAX_CH = '\u00ff';

	/**
	 * Create a GLFont from a TTF file
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param px
	 *            The font pixel size
	 * @param ttf
	 *            The TTF file
	 * @return The GLFont result
	 * @throws FontException
	 *             Any exception which occurs when reading the TTF file, brewing
	 *             the buffer or creating the final font.
	 */
	public static GLFont fromTTF(ITracer trace, float px, ResourceLocation ttf) throws FontException {
		if (trace == null)
			throw new IllegalArgumentException("trace may not be null");
		if (ttf == null)
			throw new IllegalArgumentException("ttf may not be null");
		try {
			IResource metricResource = Minecraft.getMinecraft().getResourceManager().getResource(ttf);
			InputStream stream = metricResource.getInputStream();
			if (stream == null)
				throw new IOException("Could not open TTF file.");
			Font sysfont = Font.createFont(Font.TRUETYPE_FONT, stream);
			if (trace != null)
				trace.trace("GLFont.fromTTF", sysfont.getName());
			return fromFont(trace, sysfont.deriveFont(px));
		} catch (IOException ioex) {
			trace.trace("GLFont.fromTTF", ioex);
			throw new FontException("Can't perform I/O operation!", ioex);
		} catch (FontFormatException ffe) {
			trace.trace("GLFont.fromTTF", ffe);
			throw new FontException("Invalid TTF file!", ffe);
		}
	}

	/**
	 * Create a GLFont from a spritefont and XML descriptor
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param name
	 *            The name of the font, case sensitive
	 * @param image
	 *            The image file
	 * @param xml
	 *            The XML descriptor file
	 * @return The GLFont result
	 * @throws FontException
	 *             Any exception which occurs when reading the image file, when
	 *             reading the XML descriptor, when brewing the buffer or
	 *             creating the final font.
	 */
	public static GLFont fromSpritefont(ITracer trace, String name, ResourceLocation image, ResourceLocation xml)
			throws FontException {
		if (trace == null)
			throw new IllegalArgumentException("trace may not be null");
		if (name == null)
			throw new IllegalArgumentException("name may not be null");
		if (image == null)
			throw new IllegalArgumentException("image may not be null");
		if (xml == null)
			throw new IllegalArgumentException("xml may not be null");
		try {
			IResource imageResource = Minecraft.getMinecraft().getResourceManager().getResource(image);
			InputStream stream = imageResource.getInputStream();
			if (stream == null)
				throw new IOException("Could not open image file.");
			BufferedImage buffer = ImageIO.read(stream);

			GLFontMetrics metric = GLFontMetrics.fromResource(trace, xml, buffer.getWidth(), buffer.getHeight());
			trace.trace("GLFont.fromSpritefont", "fromMetric", metric);
			GLFont f0 = fromBuffer(trace, name, buffer, buffer.getWidth(), buffer.getHeight(), metric);
			trace.trace("GLFont.fromSpritefont", f0);
			return f0;

		} catch (IOException ioex) {
			trace.trace("GLFont.fromSpritefont", ioex);
			throw new FontException("Can't perform I/O operation!", ioex);
		}
	}

	/**
	 * Create a GLFont from a Java Font object
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param font
	 *            The font object
	 * @return The GLFont result
	 * @throws FontException
	 *             Any exception which occurs when brewing the buffer or
	 *             creating the final result.
	 */
	public static GLFont fromFont(ITracer trace, Font font) throws FontException {
		if (trace == null)
			throw new IllegalArgumentException("trace may not be null");
		if (font == null)
			throw new IllegalArgumentException("font may not be null");
		int uDim = 512;
		BufferedImage buffer = new BufferedImage(uDim, uDim, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) buffer.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int off = 0;
		int charsPerRow = 12;
		for (char k = MIN_CH; k <= MAX_CH; k++, off++) {
			TextLayout layout = new TextLayout(String.valueOf(k), font, graphics.getFontRenderContext());
			Rectangle2D rect = layout.getBounds();
			int x = (off % charsPerRow) * (uDim / charsPerRow);
			int y = (off / charsPerRow) * (uDim / charsPerRow);
			float cy = (float) rect.getHeight();
			graphics.setColor(Color.WHITE);
			trace.trace("GLFont.fromFont", "placeGlyph", k, x, y - cy);
			layout.draw(graphics, x, y - cy);
		}

		GLFontMetrics metric = GLFontMetrics.fromFontMetrics(trace, font, graphics.getFontRenderContext(), uDim, uDim,
				charsPerRow, MIN_CH, MAX_CH);
		trace.trace("GLFont.fromFont", "fromMetric", metric);
		GLFont f0 = fromBuffer(trace, font.getFontName(), buffer, uDim, uDim, metric);
		trace.trace("GLFont.fromFont", f0);
		return f0;
	}

	/**
	 * Create a GLFont from an image buffer of a specified size with a specified
	 * metric map.
	 * 
	 * @param trace
	 *            The debugging tracer object
	 * @param name
	 *            The name of the font
	 * @param image
	 *            The buffered image
	 * @param width
	 *            The width of the image, absolute pixels
	 * @param height
	 *            The height of the image, absolute pixels
	 * @param metric
	 *            The font metric map
	 * @return The GLFont result
	 * @throws FontException
	 *             Any exception which occurs when transforming the buffer into
	 *             a GLFont container.
	 */
	public static GLFont fromBuffer(ITracer trace, String name, BufferedImage image, int width, int height,
			GLFontMetrics metric) throws FontException {
		if (trace == null)
			throw new IllegalArgumentException("trace may not be null");
		if (name == null)
			throw new IllegalArgumentException("name may not be null");
		if (image == null)
			throw new IllegalArgumentException("image may not be null");
		if (metric == null)
			throw new IllegalArgumentException("metric may not be null");
		ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {
				8, 8, 8, 8 }, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
		BufferedImage texImage = new BufferedImage(glAlphaColorModel, raster, true, new Hashtable<Object, Object>());
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, width, height);
		g.drawImage(image, 0, 0, null);

		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

		ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data, 0, data.length);
		buffer.flip();

		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				buffer);
		tmp.rewind();
		int texIdx = tmp.get(0);
		trace.trace("GLFont.fromBuffer", "texId", texIdx);
		GLFont font = new GLFont(name, texIdx, 0.44f, metric);
		trace.trace("GLFont.fromBuffer", font);
		Fontbox.allocateFont(font);
		return font;
	}

	private String name;
	private float scale;
	private int textureId;
	private GLFontMetrics metric;

	private GLFont(String name, int textureId, float scale, GLFontMetrics metric) {
		this.name = name;
		this.textureId = textureId;
		this.scale = scale;
		this.metric = metric;
	}

	/**
	 * Get the name of the font.
	 * 
	 * @return The name of the font
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the OpenGL texture ID for this font.
	 * 
	 * @return The 2D texture ID for the font
	 */
	public int getTextureId() {
		return textureId;
	}

	/**
	 * Get the OpenGL font scale for this font.
	 * 
	 * @return The 2D font scaling ratio
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Get the font metric map associated with this font.
	 * 
	 * @return The metric map
	 */
	public GLFontMetrics getMetric() {
		return metric;
	}

	/**
	 * Delete the font. This releases all the resources associated with the font
	 * immediately.
	 */
	public void delete() {
		Fontbox.deleteFont(this);
		GL11.glDeleteTextures(textureId);
		textureId = -1;
		name = null;
		metric = null;
	}

	@Override
	public String toString() {
		return "GLFont { hash: " + System.identityHashCode(this) + ", texture: " + textureId + ", metric: "
				+ System.identityHashCode(metric) + " }";
	}
}
