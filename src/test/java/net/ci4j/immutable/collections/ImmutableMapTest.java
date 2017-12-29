package net.ci4j.immutable.collections;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImmutableMapTest
{
	private static final ImmutableMap<String, Long> ABC_MAP = ImmutableMap.create("a", 1L, "b", 2L, "c", 3L);

	@Test
	public void testAssocExist()
	{
		final ImmutableMap<String, Long> newMap = ABC_MAP.assoc("a", 1L);
		assertSame(ABC_MAP, newMap);
	}
}