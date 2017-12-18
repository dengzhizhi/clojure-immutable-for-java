package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.Fn6;
import clojure.lang.AFn;

class IFn6 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn6 fn;

	IFn6(Fn6 fn)
	{
		this.fn = fn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6)
	{
		return fn.apply(arg1, arg2, arg3, arg4, arg5, arg6);
	}
}
