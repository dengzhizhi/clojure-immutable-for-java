package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.Fn2;
import clojure.lang.AFn;

class IFn2 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn2 fn;

	IFn2(Fn2 fn)
	{
		this.fn = fn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object arg1, Object arg2)
	{
		return fn.apply(arg1, arg2);
	}
}
