package net.afterlifelochie.fontbox.layout;

/**
 * Layout manager exception. Used when a layout cannot be computed due to
 * invalid or unspecified parameters, behaviours or other issues which prevent
 * the layout from being built.
 * 
 * @author AfterLifeLochie
 *
 */
public class LayoutException extends Exception {

	/**
	 * Serializer version ID
	 */
	private static final long serialVersionUID = 1437904113154741479L;

	/**
	 * Initializes the layout exception
	 * 
	 * @param reason
	 *            The reason for the exception
	 */
	public LayoutException(String reason) {
		super(reason);
	}

	/**
	 * Initializes the layout exception
	 * 
	 * @param reason
	 *            The reason for the exception
	 * @param cause
	 *            The parent cause exception
	 */
	public LayoutException(String reason, Throwable cause) {
		super(reason, cause);
	}

}
