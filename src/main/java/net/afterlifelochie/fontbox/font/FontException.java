package net.afterlifelochie.fontbox.font;

/**
 * Font processor exception. Used when a font file cannot be loaded or when a
 * Font related exception state is encountered.
 *
 * @author AfterLifeLochie
 *
 */
public class FontException extends Exception {

	/**
	 * Serializer version ID
	 */
	private static final long serialVersionUID = -5360801527665690451L;

	/**
	 * Initializes the font exception
	 *
	 * @param reason
	 *            The reason for the exception
	 */
	public FontException(String reason) {
		super(reason);
	}

	/**
	 * Initializes the font exception
	 *
	 * @param reason
	 *            The reason for the exception
	 * @param cause
	 *            The parent cause exception
	 */
	public FontException(String reason, Throwable cause) {
		super(reason, cause);
	}

}
