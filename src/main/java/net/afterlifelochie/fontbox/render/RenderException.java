package net.afterlifelochie.fontbox.render;

/**
 * Rendering exception class. Used when an Element cannot be rendered or some
 * other condition prevents the rendering from occurring.
 * 
 * @author AfterLifeLochie
 *
 */
public class RenderException extends Exception {

	/**
	 * Serializer version ID
	 */
	private static final long serialVersionUID = -4995118231054148211L;

	/**
	 * Initializes the rendering exception
	 * 
	 * @param reason
	 *            The reason for the exception
	 */
	public RenderException(String reason) {
		super(reason);
	}

	/**
	 * Initializes the rendering exception
	 * 
	 * @param reason
	 *            The reason for the exception
	 * @param cause
	 *            The parent cause exception
	 */
	public RenderException(String reason, Throwable cause) {
		super(reason, cause);
	}

}
