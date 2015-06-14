package fontbox;

import static org.junit.Assert.*;

import java.util.EnumSet;

import net.afterlifelochie.fontbox.document.CompilerHint;
import net.afterlifelochie.fontbox.document.CompilerHint.HintType;
import net.afterlifelochie.fontbox.document.property.FloatMode;
import net.afterlifelochie.fontbox.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.layout.components.PageProperties;
import net.afterlifelochie.io.IntegerExclusionStream;

import org.junit.Test;

public class ReliabilityTests {

	/**
	 * Test to check if the ObjectBounds intersections are computed correctly.
	 */
	@Test
	public void testObjectBoundsIntersects() {
		ObjectBounds box0 = new ObjectBounds(30, 30, 30, 30, FloatMode.NONE);
		ObjectBounds box1 = new ObjectBounds(45, 45, 15, 15, FloatMode.NONE);
		ObjectBounds box2 = new ObjectBounds(100, 100, 20, 20, FloatMode.NONE);
		assertTrue("box0 intersects box1", box0.intersects(box1));
		assertTrue("box1 intersects box0", box1.intersects(box0));
		assertFalse("box2 !intersects box1", box2.intersects(box1));
		assertFalse("box1 !intersects box2", box1.intersects(box2));
	}

	/**
	 * Test to check if the ObjectBounds encloses are computed correctly.
	 */
	@Test
	public void testObjectBoundsEnclose() {
		ObjectBounds bounds = new ObjectBounds(50, 50, 50, 50, FloatMode.NONE);
		assertTrue("bounds encloses (75, 75)", bounds.encloses(75, 75));
		assertFalse("bounds excludes (0, 0)", bounds.encloses(0, 0));
		assertFalse("bounds excludes (-75, -75)", bounds.encloses(-75, -75));
	}

	/**
	 * Test to check that Page correctly computes element bounds.
	 */
	@Test
	public void testPageBounding() {
		Page testPage = new Page(new PageProperties(100, 100, null));
		assertTrue("box is inside page", testPage.insidePage(new ObjectBounds(0, 0, 100, 100, FloatMode.NONE)));
		assertTrue("box is inside page", testPage.insidePage(new ObjectBounds(25, 25, 50, 50, FloatMode.NONE)));
		assertFalse("box is outside bounds", testPage.insidePage(new ObjectBounds(-20, -20, 140, 140, FloatMode.NONE)));
		assertFalse("box is outside bounds", testPage.insidePage(new ObjectBounds(50, 50, 100, 100, FloatMode.NONE)));
	}

	/**
	 * Test to check that CompilerHints doesn't accept bad constructor
	 * parameters.
	 */
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

	/**
	 * Test to check that integer exclusion streams work properly.
	 */
	@Test
	public void testIntegerStreams() {
		try {
			IntegerExclusionStream stream = new IntegerExclusionStream(0, 20);
			stream.exclude(1);
			assertTrue("first is not 0", stream.next() == 0);
			assertTrue("second is not 2", stream.next() == 2);
			stream.excludeRange(3, 9);
			assertTrue("last is not 10", stream.next() == 10);
		} catch (Throwable t) {
			if (t instanceof AssertionError)
				throw (AssertionError) t;
			fail("Unexpected exception: " + t);
		}
	}
}
