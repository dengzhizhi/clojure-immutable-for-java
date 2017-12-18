package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.VoidFn3;
import clojure.lang.AFn;

@SuppressWarnings("unchecked")
class VoidIFn3 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn3 fn;

	VoidIFn3(VoidFn3 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1, Object arg2, Object arg3)
	{
		fn.apply(arg1, arg2, arg3);
		return null;
	}
}
