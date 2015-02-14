package net.afterlifelochie.demo;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.afterlifelochie.fontbox.FontException;
import net.afterlifelochie.fontbox.GLFont;
import net.afterlifelochie.fontbox.layout.LayoutCalculator;
import net.afterlifelochie.fontbox.render.FontRenderBuffer;
import net.afterlifelochie.fontbox.render.WrittenFontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontboxClient extends FontboxServer {

	public GLFont font;
	public FontRenderBuffer fontBuffer;
	public LayoutCalculator fontCalculator;
	public WrittenFontRenderer renderer;

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		try {
			font = GLFont.fromTTF(new ResourceLocation("fontbox", "fonts/daniel.ttf"));
			fontBuffer = FontRenderBuffer.fromFont(font);

			fontCalculator = new LayoutCalculator();
			renderer = new WrittenFontRenderer();
		} catch (FontException f0) {
			f0.printStackTrace();
		}
	}

}
