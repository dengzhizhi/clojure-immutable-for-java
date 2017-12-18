package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.Fn4;
import clojure.lang.AFn;

class IFn4 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn4 fn;

	IFn4(Fn4 fn)
	{
		this.fn = fn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4)
	{
		return fn.apply(arg1, arg2, arg3, arg4);
	}
}
