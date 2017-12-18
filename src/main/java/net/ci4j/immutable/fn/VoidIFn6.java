package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.VoidFn6;
import clojure.lang.AFn;

@SuppressWarnings("unchecked")
class VoidIFn6 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn6 fn;

	VoidIFn6(VoidFn6 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6)
	{
		fn.apply(arg1, arg2, arg3, arg4, arg5, arg6);
		return null;
	}
}
