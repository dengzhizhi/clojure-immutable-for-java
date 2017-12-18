package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.Fn3;
import clojure.lang.AFn;

class IFn3 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn3 fn;

	IFn3(Fn3 fn)
	{
		this.fn = fn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object arg1, Object arg2, Object arg3)
	{
		return fn.apply(arg1, arg2, arg3);
	}
}
