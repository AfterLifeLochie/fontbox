package net.afterlifelochie.demo;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.afterlifelochie.fontbox.FontException;
import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.GLFont;
import net.afterlifelochie.fontbox.api.PrintOutputTracer;
import net.afterlifelochie.fontbox.layout.LayoutCalculator;
import net.afterlifelochie.fontbox.render.WrittenFontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontboxClient extends FontboxServer {

	public GLFont daniel, notethis, ampersand;
	public LayoutCalculator fontCalculator;
	public WrittenFontRenderer renderer;

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		try {
			Fontbox.setTracer(new PrintOutputTracer());
			daniel = GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/daniel.ttf"));
			notethis = GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/notethis.ttf"));
			ampersand = GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/ampersand.ttf"));

			fontCalculator = new LayoutCalculator();
			renderer = new WrittenFontRenderer();
		} catch (FontException f0) {
			f0.printStackTrace();
		}
	}

}
