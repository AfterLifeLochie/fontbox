package net.afterlifelochie.fontbox.document;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.PageCursor;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.render.BookGUI;

public class CompilerHint extends Element {

	public static enum HintType {
		PAGEBREAK, FLOATBREAK;
	}

	public EnumSet<HintType> types;

	/**
	 * Constructs a new compiler hint rule with one hint type.
	 *
	 * @param type
	 *            The type of hint. May not be null.
	 */
	public CompilerHint(HintType type) {
		if (type == null)
			throw new IllegalArgumentException("Hint type cannot be null");
		types = EnumSet.of(type);
	}

	/**
	 * Constructs a new compiler hint rule with a set of hint types.
	 *
	 * @param types
	 *            The list of hints. May not be null, may not be empty.
	 */
	public CompilerHint(EnumSet<HintType> types) {
		if (types == null)
			throw new IllegalArgumentException("Hint types cannot be null");
		if (types.size() == 0)
			throw new IllegalArgumentException("Hint list must include 1 or more hints");
		this.types = types;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		Iterator<HintType> hints = types.iterator();
		while (hints.hasNext()) {
			HintType whatHint = hints.next();
			switch (whatHint) {
			case FLOATBREAK:
				PageCursor cursor = writer.cursor();
				Page current = writer.current();
				Element lowest = null;
				int dfx = 0;
				for (Element elem : current.allElements()) {
					int dux = elem.bounds().y + elem.bounds().height;
					if (dux > dfx) {
						dfx = dux;
						lowest = elem;
					}
				}
				if (!lowest.bounds().floating())
					return;
				if (lowest.bounds().x == 0)
					cursor.left(lowest.bounds().x + lowest.bounds().width);
				cursor.top(dfx);

				break;
			case PAGEBREAK:
				writer.next();
				break;
			default:
				throw new LayoutException("Unknown compiler hint: "
						+ ((whatHint == null) ? "<null>" : whatHint.getClass().getName()));
			}
		}
	}

	@Override
	public boolean canCompileRender() {
		throw new RuntimeException("Undefined behaviour: CompilerHint in doctree!");
	}

	@Override
	public boolean canUpdate() {
		throw new RuntimeException("Undefined behaviour: CompilerHint in doctree!");
	}

	@Override
	public void update() {
		throw new RuntimeException("Undefined behaviour: CompilerHint in doctree!");
	}

	@Override
	public void render(BookGUI gui, int mx, int my, float frame) {
		throw new RuntimeException("Undefined behaviour: CompilerHint in doctree!");
	}

	@Override
	public void clicked(BookGUI gui, int mx, int my) {
		throw new RuntimeException("Undefined behaviour: CompilerHint in doctree!");
	}

	@Override
	public void typed(BookGUI gui, char val, int code) {
		throw new RuntimeException("Undefined behaviour: CompilerHint in doctree!");
	}
}
