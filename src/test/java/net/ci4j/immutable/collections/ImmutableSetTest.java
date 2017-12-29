/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable.collections;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ImmutableSetTest
{
	private final static ImmutableSet<String> ABC_SET = ImmutableSet.create("a", "b", "c");
	@Test
	public void testConsNewItem()
	{
		final ImmutableSet<String> newSet = ABC_SET.cons("d");
		assertEquals(4, newSet.size());
		assertTrue(newSet.containsAll(ABC_SET));
		assertTrue(newSet.contains("d"));
	}

	@Test
	public void testConsExistingItem()
	{
		final ImmutableSet<String> sameSet = ABC_SET.cons("b");
		assertSame(ABC_SET, sameSet);
	}

	@Test
	public void testConsAllNewItems() {
		final ImmutableSet<String> newSet = ABC_SET.consAll("c", "d", "e");
		assertEquals(5, newSet.size());
		assertTrue(newSet.containsAll(Arrays.asList("a", "b", "c", "d", "e")));
	}

	@Test
	public void testConsAllExistItems() {
		final ImmutableSet<String> newSet = ABC_SET.consAll("a", "b", "c");
		assertSame(ABC_SET, newSet);
	}
}