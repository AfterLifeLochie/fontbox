package net.afterlifelochie.fontbox;

import java.util.HashMap;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.api.VoidTracer;

/**
 * Fontbox main registry.
 * 
 * @author AfterLifeLochie
 *
 */
public class Fontbox {

	private static Fontbox inst;

	/**
	 * Get the current Fontbox instance. If the instance doesn't exist, a new
	 * one will be created for the environment.
	 * 
	 * @return The current global Fontbox instance
	 */
	public static Fontbox instance() {
		if (Fontbox.inst == null)
			Fontbox.inst = new Fontbox();
		return Fontbox.inst;
	}

	/**
	 * Get the current system tracer.
	 * 
	 * @return The current system tracer
	 */
	public static ITracer tracer() {
		return Fontbox.instance().tracer;
	}

	/**
	 * Set the current system tracer.
	 * 
	 * @param tracer
	 *            The new tracer
	 */
	public static void setTracer(ITracer tracer) {
		Fontbox.instance().tracer = tracer;
	}

	/**
	 * Allocate a font on the font record heap. The font can later be referenced
	 * using {@link Fontbox#fromName(String)}.
	 * 
	 * @param font
	 *            The font object
	 */
	public static void alloateFont(GLFont font) {
		Fontbox.instance().fonts.put(font.getName(), font);
	}

	/**
	 * Get a font from the font record heap. If the font hasn't been loaded or
	 * doesn't exist, null will be returned.
	 * 
	 * @param name
	 *            The name of the font, case sensitive
	 * @return The game font associated with the name, or null if the font
	 *         hasn't been loaded or doesn't exist.
	 */
	public static GLFont fromName(String name) {
		return Fontbox.instance().fonts.get(name);
	}

	/** The system tracer */
	public ITracer tracer = new VoidTracer();
	/** The map of all fontnames to fonts */
	public HashMap<String, GLFont> fonts = new HashMap<String, GLFont>();

}
