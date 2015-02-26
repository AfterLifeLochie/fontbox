package net.afterlifelochie.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.document.Heading;
import net.afterlifelochie.fontbox.document.Image;
import net.afterlifelochie.fontbox.document.Paragraph;
import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.components.Line;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.layout.components.PageProperties;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.afterlifelochie.fontbox.render.GLUtils;
import net.afterlifelochie.fontbox.render.RenderException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class GuiDemoBook extends BookGUI {

	public GuiDemoBook() {
		super(UpMode.TWOUP, new Layout[] { new Layout(0, 0), new Layout(180, 0) });

		try {
			/* Load the fable book */
			StringBuffer fable = new StringBuffer();
			IResource resource = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("fontbox", "books/fable.book"));
			InputStream stream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			char[] buf = new char[1024];
			int len = 0;
			while ((len = reader.read(buf)) != -1)
				fable.append(buf, 0, len);
			reader.close();

			/* Get some initial fonts */
			GLFont daniel = Fontbox.fromName("Daniel");
			GLFont notethis = Fontbox.fromName("Note this");
			GLFont ampersand = Fontbox.fromName("Ampersand");

			/* Build some document properties */
			PageProperties properties = new PageProperties(400, 450, daniel);
			properties.headingFont(ampersand).bodyFont(notethis);
			properties.bothMargin(2).lineheightSize(8).spaceSize(4).densitiy(0.66f);

			/* Build the document */
			Document document = new Document();
			document.push(new Heading("title", "The Tortoise and the Hare"));
			document.push(new Heading("author", "Written by Aesop"));
			document.push(new Image(new ResourceLocation("fontbox", "textures/books/tortoise-rocket.png"), 300, 205, AlignmentMode.JUSTIFY));
			

			String[] lines = fable.toString().split("\n");
			for (String para : lines)
				if (para.trim().length() > 0)
					document.push(new Paragraph(para.trim()));

			/* Actually generate some pages */
			PageWriter writer = new PageWriter(properties);
			DocumentProcessor.generatePages(Fontbox.tracer(), document, writer);
			writer.close();
			
			/* Set the pages */
			changePages(writer.pages());
			changeCursors(writer.cursors());
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} catch (LayoutException layout) {
			layout.printStackTrace();
		}
	}

	@Override
	public void drawBackground(int mx, int my, float frame) {
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(width / 2 - 200, height / 2 - 110, 0.0f);
		GLUtils.useFontboxTexture("noteback");
		GLUtils.drawTexturedRectUV(0, 0, 400, 220, 0, 0, 1083.0f / 1111.0f, 847.0f / 1024.0f, zLevel);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(width / 2 - 180, height / 2 - 100, 0.0f);
	}

	@Override
	public void drawForeground(int mx, int my, float frame) {
		GL11.glPopMatrix();
	}

}
