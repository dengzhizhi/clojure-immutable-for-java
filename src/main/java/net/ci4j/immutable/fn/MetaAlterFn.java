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

import net.ci4j.immutable.collections.ImmutableMap;
import clojure.lang.AFn;
import clojure.lang.APersistentMap;

import java.io.Serializable;
import java.util.function.Function;

public class MetaAlterFn extends AFn implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Function<ImmutableMap<Object, Object>, ImmutableMap<Object, Object>> fn;

	public MetaAlterFn(Function<ImmutableMap<Object, Object>, ImmutableMap<Object, Object>> fn)
	{
		this.fn = fn;
	}

	@Override
	public Object invoke(Object arg1)
	{
		return fn.apply(new ImmutableMap<>((APersistentMap) arg1)).getRaw();
	}
}
