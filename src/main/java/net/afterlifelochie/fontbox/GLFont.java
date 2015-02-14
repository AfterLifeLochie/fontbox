package net.afterlifelochie.fontbox;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

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
			return fromFont(sysfont.deriveFont(12.0f));
		} catch (IOException ioex) {
			throw new FontException("Can't perform I/O operation!", ioex);
		} catch (FontFormatException ffe) {
			throw new FontException("Invalid TTF file!", ffe);
		}
	}

	public static GLFont fromFont(Font font) throws FontException {
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
			layout.draw(graphics, x, y - cy);
		}
		try {
			ImageIO.write(buffer, "PNG", new File("draw.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fromBuffer(buffer, uDim, uDim, GLFontMetrics.fromFontMetrics(font, graphics.getFontRenderContext(),
				uDim, uDim, charsPerRow, MIN_CH, MAX_CH));
	}

	public static GLFont fromBuffer(BufferedImage image, int width, int height, GLFontMetrics metric)
			throws FontException {
		ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {
				8, 8, 8, 8 }, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
		BufferedImage texImage = new BufferedImage(glAlphaColorModel, raster, true, new Hashtable());
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, width, height);
		g.drawImage(image, 0, 0, null);

		try {
			ImageIO.write(texImage, "PNG", new File("raster.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

		ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data, 0, data.length);
		buffer.flip();

		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				buffer);
		tmp.rewind();
		return new GLFont(tmp.get(0), metric);
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
