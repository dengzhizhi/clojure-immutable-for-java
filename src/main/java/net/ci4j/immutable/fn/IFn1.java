package net.ci4j.immutable.fn;

import net.ci4j.fn.Fn1;
import clojure.lang.AFn;

import java.io.Serializable;

class IFn1 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn1 fn;

	IFn1(Fn1 fn)
	{
		this.fn = fn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object arg1)
	{
		return fn.apply(arg1);
	}
}
