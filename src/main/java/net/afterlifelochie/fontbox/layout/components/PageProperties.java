package net.afterlifelochie.fontbox.layout.components;

import net.afterlifelochie.fontbox.document.formatting.TextFormat;

/**
 * The page layout properties. Includes properties such as the width, the
 * height, the gutters (margins), the minimum space dimensions and the default
 * line-height sizes.
 *
 * @author AfterLifeLochie
 *
 */
public class PageProperties {

	/** The width of the page */
	public int width;
	/** The height of the page */
	public int height;
	/** The size of the left margin */
	public int margin_left = 0;
	/** The size of the right margin */
	public int margin_right = 0;
	/** The minimum size for the spacing between each word */
	public int min_space_size = 0;
	/** The minimum line density before justified alignment is effected */
	public float min_line_density;
	/** The default line-height size */
	public int lineheight_size = 0;
	/** The size of non-breaking tabs */
	public int tab_size = 8;

	/** The TextFormat to use when rendering headings */
	public TextFormat headingFormat;
	/** The TextFormat to use when rendering body text */
	public TextFormat bodyFormat;
	/** The TextFormat to use when rendering links */
	public TextFormat linkFormat;

	/**
	 * Create a new PageProperties container
	 *
	 * @param w
	 *            The width of the page
	 * @param h
	 *            The height of the page
	 * @param defaultFormat
	 *            The default font to use
	 */
	public PageProperties(int w, int h, TextFormat defaultFormat) {
		width = w;
		height = h;
		headingFormat = bodyFormat = linkFormat = defaultFormat;
	}

	/**
	 * Create a new PageProperties container
	 *
	 * @param w
	 *            The width of the page
	 * @param h
	 *            The height of the page
	 * @param ml
	 *            The left margin size
	 * @param mr
	 *            The right margin size
	 * @param min_sp
	 *            The minimum spacing between words
	 * @param min_lhs
	 *            The default line-height size
	 * @param line_density
	 *            The JUSTIFY line density threshold
	 * @param tab
	 *            The tab size
	 * @param head
	 *            The heading font to use
	 * @param body
	 *            The body font to use
	 * @param link
	 *            The link font to use
	 *
	 */
	public PageProperties(int w, int h, int ml, int mr, int min_sp, int min_lhs, float line_density, int tab,
			TextFormat head, TextFormat body, TextFormat link) {
		width = w;
		height = h;
		margin_left = ml;
		margin_right = mr;
		min_space_size = min_sp;
		lineheight_size = min_lhs;
		min_line_density = line_density;
		tab_size = tab;
		headingFormat = head;
		bodyFormat = body;
		linkFormat = link;
	}

	/**
	 * Set the left margin of the page
	 *
	 * @param ml
	 *            The new value
	 * @return The self object
	 */
	public PageProperties leftMargin(int ml) {
		margin_left = ml;
		return this;
	}

	/**
	 * Set the right margin of the page
	 *
	 * @param mr
	 *            The new value
	 * @return The self object
	 */
	public PageProperties rightMargin(int mr) {
		margin_right = mr;
		return this;
	}

	/**
	 * Set the left and right margins of the page
	 *
	 * @param m
	 *            The new value
	 * @return The self object
	 */
	public PageProperties bothMargin(int m) {
		margin_left = margin_right = m;
		return this;
	}

	/**
	 * Set the spacing size between words
	 *
	 * @param s
	 *            The new value
	 * @return The self object
	 */
	public PageProperties spaceSize(int s) {
		min_space_size = s;
		return this;
	}

	/**
	 * Set the default line-height size
	 *
	 * @param s
	 *            The new value
	 * @return The self object
	 */
	public PageProperties lineheightSize(int s) {
		lineheight_size = s;
		return this;
	}

	/**
	 * Set the default density for computation of justified text
	 *
	 * @param rz
	 *            The new value
	 * @return The self object
	 */
	public PageProperties densitiy(float rz) {
		if (0.0f > rz)
			rz = 0.0f;
		if (1.0f < rz)
			rz = 1.0f;
		min_line_density = rz;
		return this;
	}

	/**
	 * Set the default tab wid1th for fixed-width tabs
	 *
	 * @param sz
	 *            The new value
	 * @return The self object
	 */
	public PageProperties tabSize(int sz) {
		tab_size = sz;
		return this;
	}

	/**
	 * Set the headings format
	 *
	 * @param font
	 *            The format to use
	 * @return The self object
	 */
	public PageProperties headingFormat(TextFormat font) {
		headingFormat = font;
		return this;
	}

	/**
	 * Set the body format
	 *
	 * @param font
	 *            The format to use
	 * @return The self object
	 */
	public PageProperties bodyFormat(TextFormat font) {
		bodyFormat = font;
		return this;
	}

	/**
	 * Set the links format
	 *
	 * @param font
	 *            The format to use
	 * @return The self object
	 */
	public PageProperties linkFormat(TextFormat font) {
		linkFormat = font;
		return this;
	}

	/**
	 * Copy the PageProperties object with all current values. The new object
	 * generated is an identical clone; modifications to the parent object will
	 * not alter any children and visa-versa.
	 *
	 * @return An identical copy of this PageProperties object
	 */
	public PageProperties copy() {
		return new PageProperties(width, height, margin_left, margin_right, min_space_size, lineheight_size,
				min_line_density, tab_size, headingFormat, bodyFormat, linkFormat);
	}

}
