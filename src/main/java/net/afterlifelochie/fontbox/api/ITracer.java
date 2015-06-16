package net.afterlifelochie.fontbox.api;

/**
 * Debugging tracer interface. Objects which implement this interface can be
 * passed to Fontbox in order to perform fine-grain tracing.
 *
 * @author AfterLifeLochie
 *
 */
public interface ITracer {

	/**
	 * <p>
	 * Called by Fontbox to trace a particular event to the tracer.
	 * </p>
	 * <p>
	 * Events triggered usually take the following parameter forms:
	 * <ol>
	 * <li>The method name being invoked (the source)</li>
	 * <li>The return argument (can be null) <i>or</i> the name of the phase
	 * being recorded</li>
	 * <li>Any other debugging parameters if the call is for a phase</li>
	 * </ol>
	 * See the location of invocation for more information about the details of
	 * the parameters passed.
	 * </p>
	 *
	 * @param params
	 *            The parameters from the invoked method
	 */
	public void trace(Object... params);

	/**
	 * <p>
	 * Called by Fontbox to trace a particular warning notification to the
	 * tracer.
	 * </p>
	 * <p>
	 * Events triggered usually take the following parameter forms:
	 * <ol>
	 * <li>The method name being invoked (the source)</li>
	 * <li>The reason for the warning message</li>
	 * <li>Any other debugging parameters where appropriate</li>
	 * </ol>
	 * See the location of invocation for more information about the details of
	 * the parameters passed.
	 * </p>
	 *
	 * @param params
	 *            The parameters from the invoked method
	 */
	public void warn(Object... params);

	/**
	 * <p>
	 * Called by Fontbox to ask if Fontbox assertions are enabled.
	 * </p>
	 *
	 * @return If Fontbox internal assertions are enabled
	 */
	public boolean enableAssertion();

}
