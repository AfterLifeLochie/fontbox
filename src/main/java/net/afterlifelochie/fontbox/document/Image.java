package net.afterlifelochie.fontbox.document;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import sun.java2d.windows.GDIRenderer.Tracer;
import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.PageCursor;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.afterlifelochie.fontbox.render.GLUtils;
import net.minecraft.util.ResourceLocation;

public class Image extends Element {

	public ResourceLocation source;
	public int width, height;

	public AlignmentMode align;
	public FloatMode floating;

	/**
	 * Creates a new inline image with the properties specified.
	 * 
	 * @param source
	 *            The image source location, may not be null.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @param align
	 *            The alignment of the image.
	 */
	public Image(ResourceLocation source, int width, int height, AlignmentMode align) {
		this(source, width, height, align, FloatMode.NONE);
	}

	/**
	 * Creates a new floating image with the properties specified.
	 * 
	 * @param source
	 *            The image source location, may not be null.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @param floating
	 *            The floating mode.
	 */
	public Image(ResourceLocation source, int width, int height, FloatMode floating) {
		this(source, width, height, AlignmentMode.LEFT, floating);
	}

	/**
	 * Creates a new image with the properties specified.
	 * 
	 * @param source
	 *            The image source location, may not be null.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @param align
	 *            The alignment of the image.
	 * @param floating
	 *            The floating mode.
	 */
	public Image(ResourceLocation source, int width, int height, AlignmentMode align, FloatMode floating) {
		this.source = source;
		this.width = width;
		this.height = height;
		this.align = align;
		this.floating = floating;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		Page current = writer.current();
		PageCursor cursor = writer.cursor();
		int yh = cursor.y() + height;
		if (yh > current.properties.height) {
			current = writer.next();
			cursor = writer.cursor();
		}

		int x, y;

		switch (align) {
		case CENTER:
			float qt = current.properties.width - width;
			x = (int) Math.floor(qt / 2.0f);
			break;
		case JUSTIFY:
			float srh = (float) height / (float) width;
			width = current.properties.width - cursor.x();
			height = (int) Math.ceil(width * srh);
			x = cursor.x();
			break;
		case LEFT:
		default:
			x = cursor.x();
			break;
		case RIGHT:
			x = current.properties.width - width;
			break;
		}

		if (floating == FloatMode.RIGHT)
			x = current.properties.width - width;

		trace.trace("Image.layout", "finalize", x, cursor.y(), width, height, floating != FloatMode.NONE);
		setBounds(new ObjectBounds(x, cursor.y(), width, height, floating));
		writer.write(this);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame) {
		GL11.glPushMatrix();
		GLUtils.useSystemTexture(source);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GLUtils.drawTexturedRectUV(bounds().x * 0.44f, bounds().y * 0.44f, bounds().width * 0.44f,
				bounds().height * 0.44f, 0, 0, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	public void clicked(BookGUI gui, int mx, int my) {
		// TODO Auto-generated method stub

	}

	@Override
	public void typed(BookGUI gui, char val, int code) {
		// TODO Auto-generated method stub

	}

}
