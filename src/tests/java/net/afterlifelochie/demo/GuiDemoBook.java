package net.afterlifelochie.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.afterlifelochie.fontbox.FontException;
import net.afterlifelochie.fontbox.layout.PageBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class GuiDemoBook extends GuiScreen {
	private PageBox[] pages;
	private int currentPage = 0;

	public GuiDemoBook() {
		this(null);
	}

	public GuiDemoBook(String index) {
		StringBuffer fileData = new StringBuffer();
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("fontbox", "books/fable.book"));
			InputStream stream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			char[] buf = new char[1024];
			int len = 0;
			while ((len = reader.read(buf)) != -1)
				fileData.append(buf, 0, len);
			reader.close();

			FontboxClient client = (FontboxClient) FontboxDemoMod.proxy;
			System.out.println("boxing...");
			this.pages = client.fontCalculator.boxParagraph(client.font, fileData.toString(), 325, 425, 2, 2, 10, 1);
			System.out.println("done boxing!");
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} catch (FontException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(width / 2 - 200, height / 2 - 110, 0.0f);
		useTexture("noteback");
		drawTexturedRectUV(0, 0, 400, 220, 0, 0, 1083.0f / 1111.0f, 847.0f / 1024.0f);

		FontboxClient client = (FontboxClient) FontboxDemoMod.proxy;
		if (this.pages != null) {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			if (this.pages.length > currentPage) {
				client.renderer.renderPages(client.font, client.fontBuffer, this.pages[currentPage], 18, 12, zLevel,
						true);
				GL11.glPushMatrix();
				GL11.glTranslatef(90.0f, 200.0f, 0.0f);
				GL11.glScalef(0.5f, 0.5f, 1.0f);
				fontRendererObj.drawString(String.format("- %s -", currentPage), 0, 0, 0);
				GL11.glPopMatrix();
			}
			if (this.pages.length > currentPage + 1) {
				client.renderer.renderPages(client.font, client.fontBuffer, this.pages[currentPage + 1], 204, 12,
						zLevel, true);
				GL11.glPushMatrix();
				GL11.glTranslatef(290.0f, 200.0f, 0.0f);
				GL11.glScalef(0.5f, 0.5f, 1.0f);
				fontRendererObj.drawString(String.format("- %s -", currentPage + 1), 0, 0, 0);
				GL11.glPopMatrix();
			}
		}
		GL11.glPopMatrix();
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);

		if (par2 == Keyboard.KEY_LEFT) {
			if (0 < currentPage - 2)
				currentPage -= 2;
		}

		if (par2 == Keyboard.KEY_RIGHT) {
			if (currentPage + 2 < pages.length)
				currentPage += 2;
		}

	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);

	}

	private void useTexture(String name) {
		mc.getTextureManager().bindTexture(new ResourceLocation("fontbox", "textures/gui/" + name + ".png"));
	}

	private void pushVertex(float x, float y, float z, float u, float v) {
		GL11.glTexCoord2f(u, v);
		GL11.glVertex3f(x, y, z);
	}

	public void drawTexturedRectUV(double x, double y, double w, double h, double u, double v, double us, double vs) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.setColorOpaque_F(1.0f, 1.0f, 1.0f);
		tess.addVertexWithUV(x, y + h, zLevel, u, v + vs);
		tess.addVertexWithUV(x + w, y + h, zLevel, u + us, v + vs);
		tess.addVertexWithUV(x + w, y, zLevel, u + us, v);
		tess.addVertexWithUV(x, y, zLevel, u, v);
		tess.draw();
	}
}