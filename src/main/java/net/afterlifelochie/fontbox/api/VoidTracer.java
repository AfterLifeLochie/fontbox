package net.afterlifelochie.fontbox.api;

/**
 * <p>
 * Void tracer. Does exactly that - void. You should use this in place of
 * passing null to the tracer arguments in method calls.
 * </p>
 * 
 * @author AfterLifeLochie
 *
 */
public class VoidTracer implements ITracer {

	@Override
	public void trace(Object... params) {
		/* Do nothing */
	}

	@Override
	public void warn(Object... params) {
		Object[] data = params;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < data.length; i++)
			result.append(data[i]).append(", ");
		String r0 = result.toString();
		System.out.println("Fontbox warning: " + r0.substring(0, r0.length() - 2));
	}

}
