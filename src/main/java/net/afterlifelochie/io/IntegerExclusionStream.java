package net.afterlifelochie.io;

import java.util.ArrayList;

public class IntegerExclusionStream {

	private volatile int ptr = -1;
	private final int min;
	private final int max;
	private ArrayList<Integer> exclusions = new ArrayList<Integer>();

	public IntegerExclusionStream(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public void exclude(int i) {
		exclusions.add(i);
	}

	public void excludeRange(int a, int b) {
		if (a == b)
			exclude(a);
		else
			for (int n = Math.min(a, b), p = Math.max(a, b); n <= p; n++)
				exclude(n);
	}

	public int largest() {
		int size = 0, bestStart = 0, bestSize = 0;
		for (int start = 0; start <= max; start++) {
			if (exclusions.contains(start)) {
				if (size > bestSize) {
					bestStart = start - size;
					bestSize = size;
				}
				size = 0;
			} else {
				size++;
			}
		}
		if (size > bestSize) {
			bestStart = max - size;
			bestSize = size;
		}
		return Math.max(0, bestStart);
	}

	public int next() {
		while (true) {
			ptr++;
			if (!exclusions.contains(ptr))
				break;
		}
		return ptr;
	}

	public int previous() {
		while (true) {
			ptr--;
			if (!exclusions.contains(ptr))
				break;
		}
		return ptr;
	}

}
