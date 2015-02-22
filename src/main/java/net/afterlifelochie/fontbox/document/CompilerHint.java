package net.afterlifelochie.fontbox.document;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;

import net.afterlifelochie.fontbox.api.ITracer;
import net.afterlifelochie.fontbox.layout.LayoutException;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.PageWriterCursor;
import net.afterlifelochie.fontbox.layout.components.Page;

public class CompilerHint extends Element {

	public static enum HintType {
		PAGEBREAK, FLOATBREAK;
	}

	public EnumSet<HintType> types;

	public CompilerHint(HintType type) {
		this.types = EnumSet.of(type);
	}

	public CompilerHint(EnumSet<HintType> types) {
		this.types = types;
	}

	@Override
	public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
		Iterator<HintType> hints = types.iterator();
		while (hints.hasNext()) {
			HintType whatHint = hints.next();
			switch (whatHint) {
			case FLOATBREAK:
				PageWriterCursor cursor = writer.cursor();
				Page current = writer.current();
				Element lowest = null;
				int dfx = 0;
				for (Element elem : current.elements) {
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
}
