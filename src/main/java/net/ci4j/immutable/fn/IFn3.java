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

import net.ci4j.fn.Fn3;
import clojure.lang.AFn;

class IFn3 extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Fn3 fn;

	IFn3(Fn3 fn)
	{
		this.fn = fn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object arg1, Object arg2, Object arg3)
	{
		return fn.apply(arg1, arg2, arg3);
	}
}
