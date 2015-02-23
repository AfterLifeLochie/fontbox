package net.afterlifelochie.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.components.Line;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.layout.components.PageProperties;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.afterlifelochie.fontbox.render.RenderException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class GuiDemoBook extends BookGUI {

	public GuiDemoBook() {
		super(null, null);
		StringBuffer fileData = new StringBuffer();
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("fontbox", "books/lipsum.book"));
			InputStream stream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			char[] buf = new char[1024];
			int len = 0;
			while ((len = reader.read(buf)) != -1)
				fileData.append(buf, 0, len);
			reader.close();

			FontboxClient client = (FontboxClient) FontboxDemoMod.proxy;
			GLFont daniel = Fontbox.fromName("Daniel");
			PageProperties properties = new PageProperties(350, 450, daniel);
			properties.bothMargin(2).lineheightSize(1).spaceSize(10);
			Document doc = new Document();
			pages = DocumentProcessor.generatePages(Fontbox.tracer(), doc, properties);
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} catch (LayoutException layout) {
			layout.printStackTrace();
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(width / 2 - 200, height / 2 - 110, 0.0f);
		useFontboxTexture("noteback");
		drawTexturedRectUV(0, 0, 400, 220, 0, 0, 1083.0f / 1111.0f, 847.0f / 1024.0f);
		GL11.glPopMatrix();
	}

}
