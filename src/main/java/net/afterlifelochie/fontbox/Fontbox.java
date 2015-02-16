package net.afterlifelochie.fontbox;

import java.util.HashMap;

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

	public static void alloateFont(GLFont font) {
		Fontbox.instance().fonts.put(font.getName(), font);
	}

	public static GLFont fromName(String name) {
		return Fontbox.instance().fonts.get(name);
	}

	public ITracer tracer = new VoidTracer();
	public HashMap<String, GLFont> fonts = new HashMap<String, GLFont>();

}
