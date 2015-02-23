package net.afterlifelochie.demo;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.api.PrintOutputTracer;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.render.WrittenFontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontboxClient extends FontboxServer {

	public GLFont daniel, notethis, ampersand;

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		try {
			Fontbox.setTracer(new PrintOutputTracer());
			daniel = GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/daniel.ttf"));
			notethis = GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/notethis.ttf"));
			ampersand = GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/ampersand.ttf"));
		} catch (FontException f0) {
			f0.printStackTrace();
		}
	}

}
