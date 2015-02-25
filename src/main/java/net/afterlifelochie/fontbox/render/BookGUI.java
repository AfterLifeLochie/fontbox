package net.afterlifelochie.fontbox.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.minecraft.client.gui.GuiScreen;

public abstract class BookGUI extends GuiScreen {

	/**
	 * The page-up mode.
	 * 
	 * @author AfterLifeLochie
	 */
	public static enum UpMode {
		/** One-up (one page) mode */
		ONEUP(1),
		/** Two-up (two page) mode */
		TWOUP(2);

		/** The number of pages in this mode */
		public final int pages;

		UpMode(int pages) {
			this.pages = pages;
		}
	}

	/**
	 * Page layout container
	 * 
	 * @author AfterLifeLochie
	 *
	 */
	public static class Layout {
		public int x, y;

		/**
		 * Create a new Layout container for rendering the page on screen.
		 * 
		 * @param x
		 *            The x-coordinate to render at
		 * @param y
		 *            The y-coordinate to render at
		 */
		public Layout(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	/** The renderer's UpMode */
	protected final UpMode mode;
	/** The page layout grid */
	protected final Layout[] layout;

	/** The list of pages */
	protected ArrayList<Page> pages;
	/** The current page pointer */
	protected int ptr = 0;

	/**
	 * <p>
	 * Create a new Book rendering context on top of the existing Minecraft GUI
	 * system. The book rendering properties are set through the constructor and
	 * control how many and where pages are rendered.
	 * </p>
	 * 
	 * @param mode
	 *            The page mode, usually ONEUP or TWOUP.
	 * @param layout
	 *            The layout array which specifies where the pages should be
	 *            rendered. The number of elements in the array must match the
	 *            number of pages required by the UpMode specified.
	 */
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

	/**
	 * <p>
	 * Updates the pages currently being rendered.
	 * </p>
	 * <p>
	 * If the page read pointer is currently beyond the end of the new page
	 * count (ie, the number of pages has reduced), the pointer will be reset to
	 * the beginning of the book.
	 * </p>
	 * 
	 * @param pages
	 *            The new list of pages
	 */
	public void changePages(ArrayList<Page> pages) {
		if (ptr >= pages.size())
			ptr = 0;
		this.pages = pages;
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
		drawBackground(mx, my, frames);
		try {
			if (this.pages != null) {
				for (int i = 0; i < mode.pages; i++) {
					Layout where = layout[i];
					int what = ptr + i;
					if (pages.size() <= what)
						break;
					Page page = pages.get(ptr + i);
					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					renderPage(page, where.x, where.y, zLevel, mx, my, frames);
				}
			}
		} catch (RenderException err) {
			err.printStackTrace();
		}
		drawForeground(mx, my, frames);
	}

	/**
	 * <p>
	 * Draw the background layer of the interface. You must leave the opengl
	 * state such that the layout (0, 0) will be drawn in the current place.
	 * </p>
	 * 
	 * @param mx
	 *            The mouse x-coordinate
	 * @param my
	 *            The mouse y-coordinate
	 * @param frame
	 *            The partial frames rendered
	 */
	public abstract void drawBackground(int mx, int my, float frame);

	/**
	 * <p>
	 * Draw the foreground layer of the interface. The opengl state is such that
	 * the layout coordinates (0, 0) are in the top-left corner of the written
	 * text.
	 * </p>
	 * 
	 * @param mx
	 *            The mouse x-coordinate
	 * @param my
	 *            The mouse y-coordinate
	 * @param frame
	 *            The partial frames rendered
	 */
	public abstract void drawForeground(int mx, int my, float frame);

	@Override
	protected void keyTyped(char val, int code) {
		super.keyTyped(val, code);
		if (code == Keyboard.KEY_LEFT)
			if (0 <= ptr - mode.pages)
				ptr -= mode.pages;
		if (code == Keyboard.KEY_RIGHT)
			if (ptr + mode.pages < pages.size())
				ptr += mode.pages;
	}

	@Override
	protected void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);
		for (int i = 0; i < mode.pages; i++) {
			Layout where = layout[i];
			int which = ptr + i;
			if (pages.size() <= which)
				break;
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
