package fontbox;

import static org.junit.Assert.*;

import java.util.EnumSet;

import net.afterlifelochie.fontbox.document.CompilerHint;
import net.afterlifelochie.fontbox.document.CompilerHint.HintType;
import net.afterlifelochie.fontbox.layout.ObjectBounds;

import org.junit.Test;

public class ReliabilityTests {

	@Test
	public void testObjectBoundsIntersects() {
		ObjectBounds box0 = new ObjectBounds(30, 30, 30, 30, false);
		ObjectBounds box1 = new ObjectBounds(45, 45, 15, 15, false);
		ObjectBounds box2 = new ObjectBounds(100, 100, 20, 20, false);

		assertTrue("box0 intersects box1", box0.intersects(box1));
		assertTrue("box1 intersects box0", box1.intersects(box0));
		assertFalse("box2 !intersects box1", box2.intersects(box1));
		assertFalse("box1 !intersects box2", box1.intersects(box2));
	}

	@Test
	public void testCompilerHints() {
		try {
			HintType typeof = null;
			new CompilerHint(typeof);
			fail("CompilerHint constructor must not accept null");
		} catch (Throwable t) {
			if (!(t instanceof IllegalArgumentException))
				if (t instanceof AssertionError)
					throw (AssertionError) t;
				else
					fail("Unexpected exception: " + t);
		}

		try {
			EnumSet<HintType> blank = EnumSet.noneOf(HintType.class);
			new CompilerHint(blank);
			fail("CompilerHint constructor must not accept empty EnumSet");
		} catch (Throwable t) {
			if (!(t instanceof IllegalArgumentException))
				if (t instanceof AssertionError)
					throw (AssertionError) t;
				else
					fail("Unexpected exception: " + t);
		}
	}
}
