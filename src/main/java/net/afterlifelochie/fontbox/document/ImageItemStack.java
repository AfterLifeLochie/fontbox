package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class ImageItemStack extends Image {

	private static RenderItem renderer = new RenderItem();

	public ItemStack block;

	public ImageItemStack(ItemStack source, int width, int height, AlignmentMode align) {
		this(source, width, height, align, FloatMode.NONE);
	}

	public ImageItemStack(ItemStack source, int width, int height, FloatMode floating) {
		this(source, width, height, AlignmentMode.JUSTIFY, floating);
	}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame) {
		renderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft()
				.getTextureManager(), block, x, y);
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
