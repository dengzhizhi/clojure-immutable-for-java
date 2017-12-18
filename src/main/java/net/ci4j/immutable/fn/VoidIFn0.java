package net.ci4j.immutable.fn;

import net.ci4j.fn.VoidFn0;
import clojure.lang.AFn;

import java.io.Serializable;

class VoidIFn0 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn0 fn;

	VoidIFn0(VoidFn0 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke()
	{
		fn.apply();
		return null;
	}
}
