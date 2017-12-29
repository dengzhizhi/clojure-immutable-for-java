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
