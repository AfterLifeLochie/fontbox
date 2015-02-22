package net.afterlifelochie.fontbox.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.afterlifelochie.demo.FontboxClient;
import net.afterlifelochie.demo.FontboxDemoMod;
import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class BookGUI extends GuiScreen {

	protected ArrayList<Page> pages;
	protected int ptr = 0;
	protected int top, left;

	@Override
	public boolean doesGuiPauseGame() {
		return false;
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
	public void drawScreen(int mx, int my, float frames) {
		super.drawScreen(mx, my, frames);
	}

	@Override
	protected void keyTyped(char val, int code) {
		super.keyTyped(val, code);
		if (code == Keyboard.KEY_LEFT)
			if (0 <= ptr - 2)
				ptr -= 2;
		if (code == Keyboard.KEY_RIGHT)
			if (ptr + 2 < pages.size())
				ptr += 2;
	}

	@Override
	protected void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);
		int mouseX = mx - left;
		int mouseY = my - top;
		if (mouseX < 200) {
			if (this.pages.size() > ptr)
				// FIXME: Externalize constants!
				System.out.println(DocumentProcessor.getWord(this.pages.get(ptr), mouseX - 18, mouseY - 12));
		} else {
			if (this.pages.size() > ptr + 1)
				// FIXME: Externalize constants!
				System.out.println(DocumentProcessor.getWord(this.pages.get(ptr + 1), mouseX - 204, mouseY - 12));
		}
	}

	@Override
	protected void mouseMovedOrUp(int mx, int my, int button) {
		super.mouseMovedOrUp(mx, my, button);
	}

	@Override
	protected void mouseClickMove(int mx, int my, int button, long ticks) {
		super.mouseClickMove(mx, my, button, ticks);
	}

	protected void drawPageAt(int x, int y, int offset) throws RenderException {
		FontboxClient client = (FontboxClient) FontboxDemoMod.proxy;
		client.renderer.renderPages(Fontbox.fromName("Daniel"), pages.get(ptr + offset), x, y, zLevel, false);
	}

	protected void useFontboxTexture(String name) {
		mc.getTextureManager().bindTexture(new ResourceLocation("fontbox", "textures/gui/" + name + ".png"));
	}

	protected void drawTexturedRectUV(double x, double y, double w, double h, double u, double v, double us, double vs) {
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
