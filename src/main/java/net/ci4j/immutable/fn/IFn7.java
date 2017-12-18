package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.Fn7;
import clojure.lang.AFn;

class IFn7 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn7 fn;

	IFn7(Fn7 fn)
	{
		this.fn = fn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7)
	{
		return fn.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
}
