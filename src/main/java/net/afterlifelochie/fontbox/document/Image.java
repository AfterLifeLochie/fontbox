package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.document.property.AlignmentMode;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.minecraft.util.ResourceLocation;

public class Image extends Element {

	public ResourceLocation source;
	public int width, height;

	public AlignmentMode align;
	public FloatMode floating;

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

}
