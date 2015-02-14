package net.afterlifelochie.fontbox;

import java.awt.FontMetrics;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

/**
 * A font metric digest. Contains information on the font, such as the
 * orientation and size of each glyph supported, the texture coordinate for each
 * glyph and the resource mappings to the font image file.
 * 
 * @author AfterLifeLochie
 * 
 */
public class GLFontMetrics {

	public static GLFontMetrics fromFontMetrics(FontMetrics font, int fontImageWidth, int fontImageHeight,
			int charsPerRow, char minChar, char maxChar) throws FontException {
		int off = 0;
		GLFontMetrics metric = new GLFontMetrics(fontImageWidth, fontImageHeight);
		for (char k = minChar; k <= maxChar; k++) {
			int width = font.charWidth(k);
			int height = font.getAscent();
			int x = (off % charsPerRow) * 16;
			int y = (off / charsPerRow) * 16;
			metric.glyphs.put((int) k, new GLGlyphMetric(width, height, x, y));
		}
		return metric;
	}

	public static GLFontMetrics fromResource(ResourceLocation fontMetricName, int fontImageWidth, int fontImageHeight)
			throws FontException {
		try {
			IResource metricResource = Minecraft.getMinecraft().getResourceManager().getResource(fontMetricName);
			InputStream stream = metricResource.getInputStream();
			if (stream == null)
				throw new IOException("Could not open font metric file.");

			GLFontMetrics metric = new GLFontMetrics(fontImageWidth, fontImageHeight);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(stream);
			Element metrics = doc.getDocumentElement();

			NodeList list_character = metrics.getElementsByTagName("character");
			for (int i = 0; i < list_character.getLength(); i++) {
				Element character = (Element) list_character.item(i);
				int charcode = Integer.parseInt(character.getAttributes().getNamedItem("key").getNodeValue());
				if (0 > charcode || charcode > 255)
					throw new FontException(String.format("Unsupported character code %s", charcode));
				int w = -1, h = -1, u = -1, v = -1;
				NodeList character_properties = character.getChildNodes();
				for (int k = 0; k < character_properties.getLength(); k++) {
					Node property = character_properties.item(k);
					if (!(property instanceof Element))
						continue;
					Element elem = (Element) property;
					String name = elem.getNodeName().toLowerCase();
					int val = Integer.parseInt(elem.getFirstChild().getNodeValue());
					if (name.equals("width"))
						w = val;
					else if (name.equals("height"))
						h = val;
					else if (name.equals("x"))
						u = val;
					else if (name.equals("y"))
						v = val;
					else
						throw new FontException(String.format("Unexpected metric command %s", name));
				}
				if (w == -1 || h == -1 || u == -1 || v == -1)
					throw new FontException(String.format("Invalid metric properties set for key %s", charcode));
				metric.glyphs.put(charcode, new GLGlyphMetric(w, h, u, v));
			}
			return metric;
		} catch (IOException e) {
			throw new FontException("Cannot setup font.", e);
		} catch (ParserConfigurationException e) {
			throw new FontException("Cannot read font metric data.", e);
		} catch (SAXException e) {
			throw new FontException("Cannot read font metric data.", e);
		}
	}

	/**
	 * The individual dimensions and u-v locations of each character in the set
	 */
	public final HashMap<Integer, GLGlyphMetric> glyphs = new HashMap<Integer, GLGlyphMetric>();

	/**
	 * The universal width of the font image.
	 */
	public final float fontImageWidth;
	/**
	 * The universal height of the font image.
	 */
	public final float fontImageHeight;

	private GLFontMetrics(int fontImageWidth, int fontImageHeight) {
		this.fontImageWidth = (float) fontImageWidth;
		this.fontImageHeight = (float) fontImageHeight;
	}
}
