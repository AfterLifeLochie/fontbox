package net.afterlifelochie.fontbox.layout;

public class PageProperties {

	public int width;
	public int height;
	public int margin_left = 0;
	public int margin_right = 0;
	public int min_space_size = 0;
	public int lineheight_size = 0;

	public PageProperties(int w, int h) {
		width = w;
		height = h;
	}

	public PageProperties(int w, int h, int ml, int mr, int min_sp, int min_lhs) {
		width = w;
		height = h;
		margin_left = ml;
		margin_right = mr;
		min_space_size = min_sp;
		lineheight_size = min_lhs;
	}

	public PageProperties leftMargin(int ml) {
		margin_left = ml;
		return this;
	}

	public PageProperties rightMargin(int mr) {
		margin_right = mr;
		return this;
	}

	public PageProperties bothMargin(int m) {
		margin_left = margin_right = m;
		return this;
	}

	public PageProperties spaceSize(int s) {
		min_space_size = s;
		return this;
	}

	public PageProperties lineheightSize(int s) {
		lineheight_size = s;
		return this;
	}

	public PageProperties copy() {
		return new PageProperties(width, height, margin_left, margin_right, min_space_size, lineheight_size);
	}

}
