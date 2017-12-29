/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable.fn;

import java.io.Serializable;

import net.ci4j.fn.VoidFn4;
import clojure.lang.AFn;

@SuppressWarnings("unchecked")
class VoidIFn4 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final VoidFn4 fn;

	VoidIFn4(VoidFn4 fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4)
	{
		fn.apply(arg1, arg2, arg3, arg4);
		return null;
	}
}
