package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.VoidFn7;
import clojure.lang.AFn;

@SuppressWarnings("unchecked")
class VoidIFn7 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn7 fn;

	VoidIFn7(VoidFn7 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7)
	{
		fn.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return null;
	}
}
