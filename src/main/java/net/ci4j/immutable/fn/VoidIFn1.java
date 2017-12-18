package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.VoidFn1;
import clojure.lang.AFn;

@SuppressWarnings("unchecked")
class VoidIFn1 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn1 fn;

	VoidIFn1(VoidFn1 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1)
	{
		fn.apply(arg1);
		return null;
	}
}
