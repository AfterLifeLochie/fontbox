package net.afterlifelochie.fontbox.document;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class ImageItemStack extends Image {

	private static RenderItem renderer = new RenderItem();

	public ItemStack block;

	/**
	 * Creates a new inline item-stack image with the properties specified.
	 * 
	 * @param source
	 *            The item stack, may not be null.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @param align
	 *            The alignment of the image.
	 */
	public ImageItemStack(ItemStack source, int width, int height, AlignmentMode align) {
		this(source, width, height, align, FloatMode.NONE);
	}

	/**
	 * Creates a new floating item-stack image with the properties specified.
	 * 
	 * @param source
	 *            The item stack, may not be null.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @param floating
	 *            The floating mode.
	 */
	public ImageItemStack(ItemStack source, int width, int height, FloatMode floating) {
		this(source, width, height, AlignmentMode.LEFT, floating);
	}

	/**
	 * Creates a new item-stack image with the properties specified.
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
	public ImageItemStack(ItemStack source, int width, int height, AlignmentMode align, FloatMode floating) {
		super(null, width, height, align, floating);
		this.block = source;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void update() {
		/* No action required */
	}

	@Override
	public boolean canCompileRender() {
		return true;
	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame) {
		GL11.glPushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslatef(bounds().x * 0.44f, bounds().y * 0.44f, 0);
		GL11.glScalef(bounds().width * 0.44f / 16.0f, bounds().height * 0.44f / 16.0f, 1.0f);
		renderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft()
				.getTextureManager(), block, 0, 0);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		GL11.glPopMatrix();
	}

	@Override
	public void clicked(BookGUI gui, int mx, int my) {
		/* No action required */
	}

	@Override
	public void typed(BookGUI gui, char val, int code) {
		/* No action required */
	}
}
