package net.ci4j.immutable.fn;

import net.ci4j.fn.VoidFn4;
import net.ci4j.immutable.stm.Atom;
import clojure.lang.AFn;

public class WatcherIFn<T> extends AFn
{
	private final VoidFn4<Object, Atom<T>, T, T> fn;

	public WatcherIFn(VoidFn4<Object, Atom<T>, T, T> fn)
	{
		this.fn = fn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4)
	{
		fn.apply(arg1, new Atom<>((clojure.lang.Atom) arg2), (T) arg3, (T) arg4);
		return null;
	}
}
