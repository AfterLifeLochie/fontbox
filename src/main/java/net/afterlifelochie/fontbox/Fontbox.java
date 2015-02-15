package net.afterlifelochie.fontbox;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.api.VoidTracer;

public class Fontbox {

	private static Fontbox inst;

	public static Fontbox instance() {
		if (Fontbox.inst == null)
			Fontbox.inst = new Fontbox();
		return Fontbox.inst;
	}

	public static ITracer tracer() {
		return Fontbox.instance().tracer;
	}
	
	public static void setTracer(ITracer tracer) {
		Fontbox.instance().tracer = tracer;
	}

	public ITracer tracer = new VoidTracer();

}
