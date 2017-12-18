package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.VoidFn2;
import clojure.lang.AFn;

@SuppressWarnings("unchecked")
class VoidIFn2 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn2 fn;

	VoidIFn2(VoidFn2 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1, Object arg2)
	{
		fn.apply(arg1, arg2);
		return null;
	}
}
