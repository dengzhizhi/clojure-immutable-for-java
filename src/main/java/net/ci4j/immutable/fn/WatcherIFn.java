/**
 * Copyright (c) 2017 Zhizhi Deng
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.ci4j.immutable.fn;

import net.ci4j.fn.VoidFn4;
import net.ci4j.immutable.stm.Atom;
import clojure.lang.AFn;

public class WatcherIFn<T> extends AFn
{
	private final VoidFn4<Object, Atom<T>, T, T> fn;

	public WatcherIFn(VoidFn4<Object, Atom<T>, T, T> fn)
	{
		this.fn = fn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4)
	{
		fn.apply(arg1, new Atom<>((clojure.lang.Atom) arg2), (T) arg3, (T) arg4);
		return null;
	}
}
