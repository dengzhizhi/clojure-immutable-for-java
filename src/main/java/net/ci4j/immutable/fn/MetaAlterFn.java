package net.ci4j.immutable.fn;

import net.ci4j.immutable.collections.ImmutableMap;
import clojure.lang.AFn;
import clojure.lang.APersistentMap;

import java.io.Serializable;
import java.util.function.Function;

public class MetaAlterFn extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Function<ImmutableMap<Object, Object>, ImmutableMap<Object, Object>> fn;

	public MetaAlterFn(Function<ImmutableMap<Object, Object>, ImmutableMap<Object, Object>> fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1)
	{
		return fn.apply(new ImmutableMap<>((APersistentMap) arg1)).getRaw();
	}
}
