package net.ci4j.immutable.fn;

import net.ci4j.fn.Fn0;
import clojure.lang.AFn;

import java.io.Serializable;

class IFn0 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn0 fn;

	IFn0(Fn0 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke()
	{
		return fn.apply();
	}
}
