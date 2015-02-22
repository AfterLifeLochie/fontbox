package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.minecraft.block.Block;

public class ImageBlock extends Image {

	public Block block;

	public ImageBlock(Block source, int width, int height, AlignmentMode align) {
		this(source, width, height, align, FloatMode.NONE);
	}

	public ImageBlock(Block source, int width, int height, FloatMode floating) {
		this(source, width, height, AlignmentMode.JUSTIFY, floating);
	}

	public ImageBlock(Block source, int width, int height, AlignmentMode align, FloatMode floating) {
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
