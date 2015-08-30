package net.afterlifelochie.fontbox.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

import net.afterlifelochie.fontbox.Fontbox;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageCursor;
import net.afterlifelochie.fontbox.layout.PageIndex;
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
	/** The list of cursors */
	protected ArrayList<PageCursor> cursors;
	/** The data index */
	protected PageIndex index;
	/** The current page pointer */
	protected int ptr = 0;

	/** The current opengl display list state */
	protected boolean useDisplayList = false;
	/** The current opengl buffer list */
	protected int[] glDisplayLists;
	/** The current buffer dirty state */
	protected boolean glBufferDirty[];

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
		prepareGraphics();
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
	 * @param index
	 *            The new page index
	 */
	public void changePages(ArrayList<Page> pages, PageIndex index) {
		if (ptr >= pages.size()) {
			ptr = 0;
			internalOnPageChanged(this, ptr);
		}
		this.pages = pages;
		this.index = index;
	}

	/**
	 * <p>
	 * Updates the cursors currently being rendered. Used for debugging only.
	 * </p>
	 * <p>
	 * If the cursor parameter is not null, the matching cursor for each page
	 * will be displayed. If the cursor parameter is null, no cursors will be
	 * rendered on the page.
	 * </p>
	 * 
	 * @param cursors
	 *            The list of cursors, or null if no cursors should be rendered
	 */
	public void changeCursors(ArrayList<PageCursor> cursors) {
		this.cursors = cursors;
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
	public void onGuiClosed() {
		if (useDisplayList) {
			useDisplayList = false;
			GL11.glDeleteLists(glDisplayLists[0], glDisplayLists.length);
			for (int i = 0; i < glDisplayLists.length; i++)
				glDisplayLists[i] = -1;
		}
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

					if (this.cursors != null && what < cursors.size()) {
						PageCursor cursor = cursors.get(what);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
						GLUtils.drawDefaultRect(where.x, where.y, page.width * 0.44f, page.height * 0.44f, zLevel);
						GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
						GLUtils.drawDefaultRect(where.x + (cursor.x() * 0.44f), where.y + (cursor.y() * 0.44f), 1.0,
								1.0, zLevel);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
					}
					renderPage(i, page, where.x, where.y, zLevel, mx, my, frames);

				}
			}
		} catch (RenderException err) {
			err.printStackTrace();
		}
		drawForeground(mx, my, frames);
	}

	/**
	 * Called when the current page is changed
	 * 
	 * @param gui
	 *            The current GUI
	 * @param whatPtr
	 *            The new page pointer value
	 */
	public abstract void onPageChanged(BookGUI gui, int whatPtr);

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

	/**
	 * Called internally when the page is changed. Don't override this, use
	 * {@link #onPageChanged(BookGUI, int)} instead.
	 * 
	 * @param gui
	 *            The book GUI
	 * @param whatPtr
	 *            The new page pointer
	 */
	protected void internalOnPageChanged(BookGUI gui, int whatPtr) {
		for (int i = 0; i < mode.pages; i++) {
			glBufferDirty[i] = true;
		}
		onPageChanged(gui, whatPtr);
	}

	/**
	 * Called internally to set up the display lists.
	 */
	protected void prepareGraphics() {
		try {
			Util.checkGLError();
		} catch (OpenGLException glex) {
			Fontbox.tracer().warn("BookGUI.prepareGraphics", "Bad OpenGL operation detected, check GL history!");
			glex.printStackTrace();
			return;
		}
		glDisplayLists = new int[this.mode.pages];
		glBufferDirty = new boolean[this.mode.pages];
		int glList = GL11.glGenLists(glDisplayLists.length);

		try {
			Util.checkGLError();
		} catch (OpenGLException glex) {
			Fontbox.tracer().warn("BookGUI.prepareGraphics",
					"Unable to allocate display-list buffers, using immediate mode.");
			return;
		}

		if (glList <= 0)
			Fontbox.tracer().warn("BookGUI.prepareGraphics", "No display-lists available, using immediate mode.");
		else {
			for (int i = 0; i < this.glDisplayLists.length; i++) {
				glDisplayLists[i] = glList + i;
				glBufferDirty[i] = true;
			}
			Fontbox.tracer()
					.trace("BookGUI.prepareGraphics", "Displaylist initialized.", glList, glDisplayLists.length);
			useDisplayList = true;
		}
	}

	/**
	 * Advance to the next page
	 */
	protected void next() {
		if (ptr + mode.pages < pages.size()) {
			ptr += mode.pages;
			internalOnPageChanged(this, ptr);
		}
	}

	/**
	 * Go to a page in the index; if the item doesn't exist, no navigation
	 * occurs
	 * 
	 * @param id
	 *            The ID of the anchor to go to
	 */
	protected void go(String id) {
		int where = index.find(id);
		if (where != -1)
			go(where);
	}

	/**
	 * Go to a page
	 * 
	 * @param where
	 *            The page pointer
	 */
	protected void go(int where) {
		where = where - (where % mode.pages);
		if (ptr != where && 0 <= where - mode.pages && where + mode.pages < pages.size()) {
			ptr = where;
			internalOnPageChanged(this, ptr);
		}
	}

	/**
	 * Reverse to the previous page
	 */
	protected void previous() {
		if (0 <= ptr - mode.pages) {
			ptr -= mode.pages;
			internalOnPageChanged(this, ptr);
		}
	}

	@Override
	protected void keyTyped(char val, int code) {
		super.keyTyped(val, code);
		if (code == Keyboard.KEY_LEFT)
			previous();
		if (code == Keyboard.KEY_RIGHT)
			next();
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

	private void renderPage(int index, Page page, float x, float y, float z, int mx, int my, float frame)
			throws RenderException {
		if (!useDisplayList) {
			renderPageImmediate(page, x, y, z, mx, my, frame);
		} else {
			if (glBufferDirty[index]) {
				GL11.glNewList(glDisplayLists[index], GL11.GL_COMPILE);
				renderPageImmediate(page, x, y, z, mx, my, frame);
				GL11.glEndList();
				glBufferDirty[index] = false;
			}
			GL11.glCallList(glDisplayLists[index]);
		}
	}

	private void renderPageImmediate(Page page, float x, float y, float z, int mx, int my, float frame)
			throws RenderException {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		int count = page.staticElements().size();
		for (int i = 0; i < count; i++) {
			page.staticElements().get(i).render(this, mx, my, frame);
			if (this.cursors != null) {
				ObjectBounds bounds = page.staticElements().get(i).bounds();
				if (bounds != null) {
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(0.0f, 0.0f, 1.0f, 0.5f);
					GL11.glPushMatrix();
					GL11.glTranslatef(bounds.x * 0.44f, bounds.y * 0.44f, 0);
					GLUtils.drawDefaultRect(0, 0, bounds.width * 0.44f, bounds.height * 0.44f, zLevel);
					GL11.glPopMatrix();
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}
			}
		}
		GL11.glPopMatrix();
	}

}
