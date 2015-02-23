package net.afterlifelochie.fontbox.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.afterlifelochie.demo.FontboxClient;
import net.afterlifelochie.demo.FontboxDemoMod;
import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.font.FontException;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class BookGUI extends GuiScreen {

	public static enum UpMode {
		ONEUP(1), TWOUP(2);

		public final int pages;

		UpMode(int pages) {
			this.pages = pages;
		}
	}

	public static class Layout {
		public int x, y;

		public Layout(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	protected final UpMode mode;
	protected final Layout[] layout;

	protected ArrayList<Page> pages;
	protected int ptr = 0;

	public BookGUI(UpMode mode, Layout[] layout) {
		if (layout == null)
			throw new IllegalArgumentException("Layout cannot be null!");
		if (mode == null)
			throw new IllegalArgumentException("Mode cannot be null!");
		if (layout.length != mode.pages)
			throw new IllegalArgumentException("Expected " + mode.pages + " pages for mode " + mode);
		this.mode = mode;
		this.layout = layout;
	}

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
		try {
			if (this.pages != null) {
				for (int i = 0; i < mode.pages; i++) {
					Layout where = layout[i];
					Page page = pages.get(ptr + i);
					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					renderPage(page, where.x, where.y, zLevel, mx, my, frames);
				}
			}
		} catch (RenderException err) {
			err.printStackTrace();
		}
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
		for (int i = 0; i < mode.pages; i++) {
			Layout where = layout[i];
			Page page = pages.get(ptr + i);
			int mouseX = mx - where.x, mouseY = my - where.y;
			if (mouseX >= 0 && mouseY >= 0 && mouseX <= page.width && mouseY <= page.height) {
				Element elem = DocumentProcessor.getElementAt(page, mouseX, mouseY);
				if (elem != null)
					elem.clicked(this, mouseX, mouseY);
			}
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

	private void renderPage(Page page, float x, float y, float z, int mx, int my, float frame) throws RenderException {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		/*
		 * TODO: Draw this to a displaylist, then recall the displaylist later
		 * on. This saves us recomputing a lot of values per frame and thus
		 * prevents avoidable performance reductions.
		 */
		int count = page.elements.size();
		for (int i = 0; i < count; i++) {
			page.elements.get(i).render(this, mx, my, frame);
		}
		GL11.glPopMatrix();
	}

	

}
