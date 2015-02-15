package net.afterlifelochie.fontbox.api;

public class PrintOutputTracer implements ITracer {

	@Override
	public void trace(Object... params) {
		Object[] data = params;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			result.append(data[i]).append(", ");
		}
		String r0 = result.toString();
		System.out.println("PrintOutputTracer.trace: " + r0.substring(0, r0.length() - 2));
	}

}
