package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.VoidFn5;
import clojure.lang.AFn;

@SuppressWarnings("unchecked")
class VoidIFn5 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn5 fn;

	VoidIFn5(VoidFn5 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5)
	{
		fn.apply(arg1, arg2, arg3, arg4, arg5);
		return null;
	}
}
