package net.afterlifelochie.fontbox.api;

/**
 * Void tracer. Does exactly that - void. You should use this in place of
 * passing null to the tracer arguments in method calls.
 * 
 * @author AfterLifeLochie
 *
 */
public class VoidTracer implements ITracer {

	@Override
	public void trace(Object... params) {
		/* Do nothing */
	}

}
