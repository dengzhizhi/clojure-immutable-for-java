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

import net.ci4j.immutable.clojure_utils.JsonParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ImmutableListTest
{

	private static final ImmutableList<String> ABC_LIST = ImmutableList.create("a", "b", "c");

	@Test
	public void testFromStringWithBadExpression()
	{
		try {
			final ImmutableList<Object> list = ImmutableList.fromString("abc");
			Assert.fail("Should throw ClassCastException.");
		} catch (ClassCastException e) {
			//success
		}
	}

	@Test
	public void testFromStringWithWrongCollectionType()
	{
		try {
			final ImmutableList<Object> list = ImmutableList.fromString("{:a 1, :b 2}");
			Assert.fail("Should throw ClassCastException");
		} catch (ClassCastException e) {
			//success
		}
	}

	@Test
	public void testFromStringWithNull()
	{
		final ImmutableList<Object> list = ImmutableList.fromString(null);
		Assert.assertEquals(0, list.size());
		Assert.assertTrue(list.isEmpty());
	}

	@Test
	public void testFromString()
	{
		final ImmutableList<String> list = ImmutableList.fromString("[\"a\", \"b\", \"c\"]");
		Assert.assertEquals(3, list.size());
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("b", list.get(1));
		Assert.assertEquals("c", list.get(2));
	}

	@Test
	public void testFromJSON()
	{
		final ImmutableList<String> list = ImmutableList.fromJSON("[\"a\", \"b\", \"c\"]");
		Assert.assertEquals(3, list.size());
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("b", list.get(1));
		Assert.assertEquals("c", list.get(2));
	}

	@Test
	public void testFromJSONWithBadJson()
	{
		try {
			ImmutableList.fromJSON("abc");
			Assert.fail("Should throw JsonParseException");
		} catch (JsonParseException e) {
			//success
		}
	}

	@Test
	public void testFromJSONWithNonArray()
	{
		try {
			ImmutableList.fromJSON("{\"a\": 1}");
			Assert.fail("Should throw ClassCastException");
		} catch (ClassCastException e) {
			//success
		}
	}

	@Test
	public void testIterator()
	{
		final Iterator<String> it = ABC_LIST.iterator();
		Assert.assertEquals("a", it.next());
		Assert.assertEquals("b", it.next());
		Assert.assertEquals("c", it.next());
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testListIterator()
	{
		final ListIterator<String> it = ABC_LIST.listIterator();
		Assert.assertEquals("a", it.next());
		Assert.assertTrue(it.hasPrevious());
		Assert.assertEquals("a", it.previous());
		Assert.assertFalse(it.hasPrevious());
		Assert.assertEquals("a", it.next());
		Assert.assertEquals("b", it.next());
		Assert.assertEquals(2, it.nextIndex());
		Assert.assertEquals(1, it.previousIndex());
	}

	@Test
	public void testToArray()
	{
		final Object[] items = ABC_LIST.toArray();
		Assert.assertArrayEquals(new String[] {"a", "b", "c"}, items);
	}

	@Test
	public void testEquals()
	{
		Assert.assertTrue(ABC_LIST.equals(ImmutableList.create("a", "b", "c")));
		Assert.assertFalse(ABC_LIST.equals(ImmutableList.create("a", "b", "c", "d")));
	}

	@Test
	public void testSubList()
	{
		final List<String> subList = ABC_LIST.subList(1, 3);
		Assert.assertEquals(ImmutableList.create("b", "c"), subList);
	}

	@Test
	public void testSubListFull()
	{
		final List<String> subList = ABC_LIST.subList(0, 3);
		Assert.assertSame(ABC_LIST, subList);
	}

	@Test
	public void testDissoc()
	{
		final ImmutableList<String> dissoced = ABC_LIST.dissocN(1);
		Assert.assertArrayEquals(new String[] {"a", "c"}, dissoced.toArray());
	}

	@Test
	public void testFilterOut()
	{
		final ImmutableList<String> dissoced = ABC_LIST.filterOut(it -> it.equals("b"));
		Assert.assertArrayEquals(new String[] {"a", "c"}, dissoced.toArray());
	}

	@Test
	public void testFilterOutNoChange()
	{
		Assert.assertSame(ABC_LIST, ABC_LIST.filterOut(it -> false));
	}

	@Test
	public void testFilter()
	{
		final ImmutableList<String> filtered = ABC_LIST.filter(it -> it.equals("b"));
		Assert.assertArrayEquals(new String[] {"b"}, filtered.toArray());
	}

	@Test
	public void testFilterNoChange()
	{
		final ImmutableList<String> filtered = ABC_LIST.filter(it -> true);
		Assert.assertSame(ABC_LIST, filtered);
	}
}