package net.afterlifelochie.fontbox.document.formatting;

import java.util.Set;

import org.lwjgl.util.Color;

import net.afterlifelochie.fontbox.font.GLFont;

public class TextFormat {

	/**
	 * The reset format constant field. If used in an element stream, the
	 * formatting state will be reset to defaults.
	 */
	public static final TextFormat RESET = new TextFormat(null, null, null);

	/** The text decoration mode */
	public final Set<DecorationStyle> decorations;
	/** The text's font */
	public final GLFont font;
	/** The font's color */
	public final Color color;

	/**
	 * <p>
	 * Creates a new text formatting rule. The rule can be injected into text at
	 * one or many places in a chunk of text. The properties of the format are
	 * immutable.
	 * </p>
	 * 
	 * @param font
	 *            The font to switch the renderer to
	 */
	public TextFormat(GLFont font) {
		this(null, null, font);
	}

	/**
	 * <p>
	 * Creates a new text formatting rule. The rule can be injected into text at
	 * one or many places in a chunk of text. The properties of the format are
	 * immutable.
	 * </p>
	 * 
	 * @param decorations
	 *            The text decorations to apply
	 */
	public TextFormat(Set<DecorationStyle> decorations) {
		this(decorations, null, null);
	}

	/**
	 * <p>
	 * Creates a new text formatting rule. The rule can be injected into text at
	 * one or many places in a chunk of text. The properties of the format are
	 * immutable.
	 * </p>
	 * <p>
	 * Where a property is left as {#code null} in the constructor, the property
	 * will be left un-changed in the renderer. If you need to reset all the
	 * properties to a known default state, use the {@link TextFormat#RESET}
	 * value instead.
	 * </p>
	 * 
	 * @param decorations
	 *            The text decorations to apply
	 * @param color
	 *            The font color to apply
	 * @param font
	 *            The font to apply
	 */
	public TextFormat(Set<DecorationStyle> decorations, Color color, GLFont font) {
		this.decorations = decorations;
		this.color = color;
		this.font = font;
	}

}
