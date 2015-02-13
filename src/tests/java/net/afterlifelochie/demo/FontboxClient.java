package net.afterlifelochie.demo;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.afterlifelochie.fontbox.FontException;
import net.afterlifelochie.fontbox.FontMetric;
import net.afterlifelochie.fontbox.FontRenderBuffer;
import net.afterlifelochie.fontbox.LayoutCalculator;
import net.afterlifelochie.fontbox.WrittenFontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontboxClient extends FontboxServer {

	public FontMetric font;
	public FontRenderBuffer fontBuffer;
	public LayoutCalculator fontCalculator;
	public WrittenFontRenderer renderer;

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		try {
			font = new FontMetric(new ResourceLocation("fontbox", "fonts/daniel.png"), 418, 242, new ResourceLocation(
					"fontbox", "fonts/daniel.metrics.xml"));
			font.buildFont();
			fontBuffer = new FontRenderBuffer(font);
			fontCalculator = new LayoutCalculator();
			renderer = new WrittenFontRenderer();
		} catch (FontException f0) {
			f0.printStackTrace();
		}
	}

}
