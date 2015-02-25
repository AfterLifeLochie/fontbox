package net.afterlifelochie.demo;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.api.PrintOutputTracer;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.font.GLFont;
import net.minecraft.util.ResourceLocation;

public class FontboxClient extends FontboxServer {

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		try {
			Fontbox.setTracer(new PrintOutputTracer());
			GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/daniel.ttf"));
			GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/notethis.ttf"));
			GLFont.fromTTF(Fontbox.tracer(), new ResourceLocation("fontbox", "fonts/ampersand.ttf"));
		} catch (FontException f0) {
			f0.printStackTrace();
		}
	}

}
