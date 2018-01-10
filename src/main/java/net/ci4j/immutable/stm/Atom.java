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
package net.ci4j.immutable.stm;

import clojure.lang.APersistentMap;
import clojure.lang.IFn;
import clojure.lang.IPersistentMap;
import clojure.lang.IPersistentVector;
import clojure.lang.IRef;
import clojure.lang.PersistentList;
import net.ci4j.fn.Fn1;
import net.ci4j.fn.VoidFn4;
import net.ci4j.immutable.collections.ImmutableMap;
import net.ci4j.immutable.fn.MetaAlterFn;
import net.ci4j.immutable.fn.WatcherIFn;

import java.io.Serializable;
import java.util.function.Function;

import static net.ci4j.immutable.fn.Fn.fn;

/**
 * A clojure.lang.Atom wrapper that supports generic type.
 *
 *
 * @param <T> type of the state
 */
/*
 * Development Note: Some operation may require to re-wrapping the internal state,
 * DO NOT store any other states in the wrapper.
 */
@SuppressWarnings("unchecked")
public class Atom<T> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private clojure.lang.Atom wrapped;

	public Atom()
	{
		this.wrapped = new clojure.lang.Atom(null);
	}

	public Atom(clojure.lang.Atom wrapped) {
		this.wrapped = wrapped;
	}

	public Atom(T state)
	{
		this.wrapped = new clojure.lang.Atom(state);
	}

	public T deref()
	{
		return (T) wrapped.deref();
	}

	public T swap(Fn1<T, T> f)
	{
		return (T) wrapped.swap(fn(f));
	}

	public IPersistentVector swapVals(Fn1<T, T> f)
	{
		return wrapped.swapVals(fn(f));
	}

	public boolean compareAndSet(T oldv, T newv)
	{
		return wrapped.compareAndSet(oldv, newv);
	}

	public T reset(T newval)
	{
		return (T) wrapped.reset(newval);
	}

	public IPersistentVector resetVals(T newv)
	{
		return wrapped.resetVals(newv);
	}

	public void setValidator(Fn1<T, Boolean> vf)
	{
		wrapped.setValidator(fn(vf));
	}

	public IFn getValidator()
	{
		return wrapped.getValidator();
	}

	public IPersistentMap getWatches()
	{
		return wrapped.getWatches();
	}

	public IRef addWatch(Object key, VoidFn4<Object, Atom<T>, T, T> callback)
	{
		return wrapped.addWatch(key, new WatcherIFn<>(callback));
	}

	public IRef removeWatch(Object key)
	{
		return wrapped.removeWatch(key);
	}

	public void notifyWatches(T oldval, T newval)
	{
		wrapped.notifyWatches(oldval, newval);
	}

	public ImmutableMap<Object, Object> meta()
	{
		return new ImmutableMap((APersistentMap) wrapped.meta()) ;
	}

	public ImmutableMap<Object, Object> alterMeta(Function<ImmutableMap<Object, Object>, ImmutableMap<Object, Object>> alter)
	{
		return new ImmutableMap((APersistentMap) wrapped.alterMeta(new MetaAlterFn(alter), PersistentList.EMPTY)) ;
	}

	public IPersistentMap resetMeta(ImmutableMap<?, ?> m)
	{
		return wrapped.resetMeta(m.getRaw());
	}
}
