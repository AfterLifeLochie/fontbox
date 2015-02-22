package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.minecraft.item.ItemStack;

public class ImageItem extends Image {

	public ItemStack stack;

	public ImageItem(ItemStack source, int width, int height, AlignmentMode align) {
		this(source, width, height, align, FloatMode.NONE);
	}

	public ImageItem(ItemStack source, int width, int height, FloatMode floating) {
		this(source, width, height, AlignmentMode.JUSTIFY, floating);
	}

	public ImageItem(ItemStack source, int width, int height, AlignmentMode align, FloatMode floating) {
		super(null, width, height, align, floating);
		this.stack = source;
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
		// TODO Auto-generated method stub
		
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
