package net.afterlifelochie.fontbox.document;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

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

	private int x, y;

	public Image(ResourceLocation source, int width, int height, AlignmentMode align) {
		this(source, width, height, align, FloatMode.NONE);
	}

	public Image(ResourceLocation source, int width, int height, FloatMode floating) {
		this(source, width, height, AlignmentMode.JUSTIFY, floating);
	}

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
		int yh = cursor.y + height;
		if (yh > current.properties.height) {
			current = writer.next();
			cursor = writer.cursor();
		}
		x = cursor.x;
		y = cursor.y;
		setBounds(new ObjectBounds(x, y, width, height, false));
		current.elements.add(this);
		cursor.y += height;
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
		GLUtils.drawTexturedRectUV(x * 0.44f, y * 0.44f, width * 0.44f, height * 0.44f, 0, 0, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
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
